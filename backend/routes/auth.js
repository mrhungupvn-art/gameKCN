// routes/auth.js - 🔐 Đăng nhập / tài khoản
const express = require('express');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const db = require('../db');
const auth = require('../middleware/auth');

const router = express.Router();

function signToken(userId, characterId) {
  return jwt.sign(
    { userId, characterId },
    process.env.JWT_SECRET || 'kcn_secret_key',
    { expiresIn: process.env.JWT_EXPIRES_IN || '7d' }
  );
}

// POST /api/auth/register - Đăng ký tài khoản mới + tạo nhân vật mặc định
router.post('/register', (req, res) => {
  const { username, password, email, characterName, gender } = req.body;
  if (!username || !password || !characterName) {
    return res.status(400).json({ success: false, message: 'Thiếu username, password hoặc characterName' });
  }

  const exists = db.prepare('SELECT id FROM users WHERE username = ?').get(username);
  if (exists) {
    return res.status(409).json({ success: false, message: 'Tài khoản đã tồn tại' });
  }

  const passwordHash = bcrypt.hashSync(password, 10);
  const userInfo = db.prepare('INSERT INTO users (username, password_hash, email) VALUES (?,?,?)')
    .run(username, passwordHash, email || null);

  const characterInfo = db.prepare(
    'INSERT INTO characters (user_id, name, gender) VALUES (?,?,?)'
  ).run(userInfo.lastInsertRowid, characterName, gender || 'nam');

  db.prepare('INSERT INTO wallets (character_id, coin, gem) VALUES (?,?,?)')
    .run(characterInfo.lastInsertRowid, 1000, 10); // xu khởi đầu

  const token = signToken(userInfo.lastInsertRowid, characterInfo.lastInsertRowid);
  res.json({ success: true, token, characterId: characterInfo.lastInsertRowid });
});

// POST /api/auth/login - Đăng nhập
router.post('/login', (req, res) => {
  const { username, password } = req.body;
  const user = db.prepare('SELECT * FROM users WHERE username = ?').get(username);
  if (!user || !bcrypt.compareSync(password, user.password_hash)) {
    return res.status(401).json({ success: false, message: 'Sai tài khoản hoặc mật khẩu' });
  }
  const character = db.prepare('SELECT id FROM characters WHERE user_id = ?').get(user.id);
  const token = signToken(user.id, character ? character.id : null);
  res.json({ success: true, token, characterId: character ? character.id : null });
});

// GET /api/auth/me - Thông tin tài khoản hiện tại
router.get('/me', auth, (req, res) => {
  const user = db.prepare('SELECT id, username, email, created_at FROM users WHERE id = ?').get(req.userId);
  res.json({ success: true, data: user });
});

module.exports = router;
