// routes/kcn.js - 🏭 Khu công nghiệp (KCN)
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

// GET /api/kcn - danh sách các khu công nghiệp
router.get('/', auth, (req, res) => {
  const list = db.prepare('SELECT * FROM kcns').all();
  res.json({ success: true, data: list });
});

// POST /api/kcn/:id/join - nhân vật vào KCN
router.post('/:id/join', auth, (req, res) => {
  const kcnId = req.params.id;
  db.prepare('UPDATE characters SET kcn_id = ? WHERE id = ?').run(kcnId, req.characterId);
  res.json({ success: true, message: 'Đã vào KCN', kcnId });
});

module.exports = router;
