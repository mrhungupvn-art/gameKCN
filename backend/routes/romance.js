// routes/romance.js - ❤️ Kết duyên
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

// GET /api/romance/candidates - danh sách nhân vật độc thân để kết duyên
router.get('/candidates', auth, (req, res) => {
  const candidates = db.prepare(`
    SELECT id, name, gender, level, avatar_url FROM characters
    WHERE id != ?
  `).all(req.characterId);
  res.json({ success: true, data: candidates });
});

// POST /api/romance/propose - tỏ tình với nhân vật khác
router.post('/propose', auth, (req, res) => {
  const { targetCharacterId } = req.body;
  const existing = db.prepare(`
    SELECT * FROM relationships
    WHERE (character_id_1 = ? AND character_id_2 = ?) OR (character_id_1 = ? AND character_id_2 = ?)
  `).get(req.characterId, targetCharacterId, targetCharacterId, req.characterId);

  if (existing) return res.json({ success: true, message: 'Mối quan hệ đã tồn tại', data: existing });

  const info = db.prepare(
    'INSERT INTO relationships (character_id_1, character_id_2, status) VALUES (?,?,\'pending\')'
  ).run(req.characterId, targetCharacterId);

  res.json({ success: true, message: 'Đã gửi lời tỏ tình', id: info.lastInsertRowid });
});

// POST /api/romance/:id/respond - chấp nhận / từ chối lời tỏ tình
router.post('/:id/respond', auth, (req, res) => {
  const { accept } = req.body;
  const status = accept ? 'accepted' : 'rejected';
  db.prepare('UPDATE relationships SET status = ? WHERE id = ?').run(status, req.params.id);
  res.json({ success: true, message: `Đã ${accept ? 'chấp nhận' : 'từ chối'} lời tỏ tình` });
});

// GET /api/romance/my - mối quan hệ hiện tại của nhân vật
router.get('/my', auth, (req, res) => {
  const rel = db.prepare(`
    SELECT * FROM relationships
    WHERE (character_id_1 = ? OR character_id_2 = ?) AND status = 'accepted'
  `).get(req.characterId, req.characterId);
  res.json({ success: true, data: rel || null });
});

module.exports = router;
