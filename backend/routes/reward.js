// routes/reward.js - 🎁 Phần thưởng
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

// GET /api/reward - danh sách phần thưởng (đã nhận / chưa nhận)
router.get('/', auth, (req, res) => {
  const rewards = db.prepare('SELECT * FROM rewards WHERE character_id = ? ORDER BY created_at DESC')
    .all(req.characterId);
  res.json({ success: true, data: rewards });
});

// POST /api/reward/:id/claim - nhận phần thưởng
router.post('/:id/claim', auth, (req, res) => {
  const reward = db.prepare('SELECT * FROM rewards WHERE id = ? AND character_id = ?')
    .get(req.params.id, req.characterId);
  if (!reward) return res.status(404).json({ success: false, message: 'Không tìm thấy phần thưởng' });
  if (reward.claimed) return res.status(400).json({ success: false, message: 'Phần thưởng đã được nhận' });

  db.prepare('UPDATE wallets SET coin = coin + ?, gem = gem + ? WHERE character_id = ?')
    .run(reward.coin, reward.gem, req.characterId);
  db.prepare('UPDATE rewards SET claimed = 1 WHERE id = ?').run(reward.id);

  res.json({ success: true, message: 'Đã nhận phần thưởng', coin: reward.coin, gem: reward.gem });
});

module.exports = router;
