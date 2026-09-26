// routes/seller.js - 🏪 Cửa hàng / người bán
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

// GET /api/seller - danh sách người bán trong KCN
router.get('/', auth, (req, res) => {
  const sellers = db.prepare('SELECT * FROM sellers').all();
  res.json({ success: true, data: sellers });
});

// POST /api/seller/register - đăng ký làm người bán, mở cửa hàng
router.post('/register', auth, (req, res) => {
  const { shopName, kcnId } = req.body;
  const info = db.prepare('INSERT INTO sellers (shop_name, character_id, kcn_id) VALUES (?,?,?)')
    .run(shopName, req.characterId, kcnId || null);
  res.json({ success: true, sellerId: info.lastInsertRowid });
});

// POST /api/seller/:id/products - người bán đăng sản phẩm mới
router.post('/:id/products', auth, (req, res) => {
  const sellerId = req.params.id;
  const { name, price, category, image, stock } = req.body;
  const info = db.prepare(
    'INSERT INTO products (name, price, category, image, seller_id, stock) VALUES (?,?,?,?,?,?)'
  ).run(name, price, category || 'khac', image || '', sellerId, stock || 999);
  res.json({ success: true, productId: info.lastInsertRowid });
});

module.exports = router;
