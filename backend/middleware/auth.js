// middleware/auth.js - Xác thực JWT token cho các API cần đăng nhập
const jwt = require('jsonwebtoken');

function authMiddleware(req, res, next) {
  const header = req.headers['authorization'];
  const token = header && header.startsWith('Bearer ') ? header.slice(7) : null;

  if (!token) {
    return res.status(401).json({ success: false, message: 'Thiếu token đăng nhập' });
  }

  try {
    const payload = jwt.verify(token, process.env.JWT_SECRET || 'kcn_secret_key');
    req.userId = payload.userId;
    req.characterId = payload.characterId;
    next();
  } catch (err) {
    return res.status(401).json({ success: false, message: 'Token không hợp lệ hoặc đã hết hạn' });
  }
}

module.exports = authMiddleware;
