// routes/wallet.js - 🪙 Xu
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

router.get('/', auth, (req, res) => {
  const wallet = db.prepare('SELECT * FROM wallets WHERE character_id = ?').get(req.characterId);
  res.json({ success: true, data: wallet });
});

module.exports = router;
