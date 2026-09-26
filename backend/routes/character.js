// routes/character.js - 👤 Nhân vật
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

// GET /api/character/me - thông tin nhân vật của tôi
router.get('/me', auth, (req, res) => {
  const character = db.prepare('SELECT * FROM characters WHERE id = ?').get(req.characterId);
  if (!character) return res.status(404).json({ success: false, message: 'Không tìm thấy nhân vật' });
  res.json({ success: true, data: character });
});

// PUT /api/character/me - cập nhật thông tin nhân vật (tên, avatar...)
router.put('/me', auth, (req, res) => {
  const { name, avatar_url } = req.body;
  db.prepare('UPDATE characters SET name = COALESCE(?, name), avatar_url = COALESCE(?, avatar_url) WHERE id = ?')
    .run(name, avatar_url, req.characterId);
  const updated = db.prepare('SELECT * FROM characters WHERE id = ?').get(req.characterId);
  res.json({ success: true, data: updated });
});

module.exports = router;
