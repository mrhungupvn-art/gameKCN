// routes/career.js - 💼 Nghề nghiệp / công ty
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

// GET /api/career - danh sách nghề nghiệp / công ty
router.get('/', auth, (req, res) => {
  const list = db.prepare('SELECT * FROM careers').all();
  res.json({ success: true, data: list });
});

// POST /api/career/:id/apply - ứng tuyển vào nghề nghiệp
router.post('/:id/apply', auth, (req, res) => {
  const careerId = req.params.id;
  const career = db.prepare('SELECT * FROM careers WHERE id = ?').get(careerId);
  if (!career) return res.status(404).json({ success: false, message: 'Không tìm thấy nghề nghiệp' });

  const existing = db.prepare('SELECT * FROM character_careers WHERE character_id = ? AND career_id = ?')
    .get(req.characterId, careerId);
  if (existing) return res.json({ success: true, message: 'Đã ứng tuyển trước đó', data: existing });

  const info = db.prepare('INSERT INTO character_careers (character_id, career_id) VALUES (?,?)')
    .run(req.characterId, careerId);
  res.json({ success: true, message: 'Ứng tuyển thành công', id: info.lastInsertRowid });
});

// POST /api/career/:id/work - đi làm để nhận lương (xu)
router.post('/:id/work', auth, (req, res) => {
  const careerId = req.params.id;
  const cc = db.prepare('SELECT * FROM character_careers WHERE character_id = ? AND career_id = ?')
    .get(req.characterId, careerId);
  if (!cc) return res.status(400).json({ success: false, message: 'Bạn chưa làm việc tại công ty này' });

  const career = db.prepare('SELECT * FROM careers WHERE id = ?').get(careerId);
  const salary = career.base_salary * cc.work_level;

  db.prepare('UPDATE wallets SET coin = coin + ? WHERE character_id = ?').run(salary, req.characterId);
  db.prepare('UPDATE character_careers SET last_work_at = datetime(\'now\') WHERE id = ?').run(cc.id);

  res.json({ success: true, message: `Nhận lương thành công`, salary });
});

module.exports = router;
