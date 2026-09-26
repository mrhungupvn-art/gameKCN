// routes/shop.js - 🛒 Mua sắm (danh sách sản phẩm + mua hàng)
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

// GET /api/shop/products?category=... - danh sách sản phẩm
router.get('/products', auth, (req, res) => {
  const { category } = req.query;
  let products;
  if (category) {
    products = db.prepare('SELECT * FROM products WHERE category = ?').all(category);
  } else {
    products = db.prepare('SELECT * FROM products').all();
  }
  res.json({ success: true, data: products });
});

// POST /api/shop/buy - mua sản phẩm
router.post('/buy', auth, (req, res) => {
  const { productId, quantity } = req.body;
  const qty = quantity || 1;
  const product = db.prepare('SELECT * FROM products WHERE id = ?').get(productId);
  if (!product) return res.status(404).json({ success: false, message: 'Không tìm thấy sản phẩm' });
  if (product.stock < qty) return res.status(400).json({ success: false, message: 'Sản phẩm không đủ hàng' });

  const total = product.price * qty;
  const wallet = db.prepare('SELECT * FROM wallets WHERE character_id = ?').get(req.characterId);
  if (wallet.coin < total) return res.status(400).json({ success: false, message: 'Không đủ xu' });

  db.prepare('UPDATE wallets SET coin = coin - ? WHERE character_id = ?').run(total, req.characterId);
  db.prepare('UPDATE products SET stock = stock - ? WHERE id = ?').run(qty, productId);
  db.prepare('INSERT INTO purchases (character_id, product_id, quantity, total_price) VALUES (?,?,?,?)')
    .run(req.characterId, productId, qty, total);

  res.json({ success: true, message: 'Mua hàng thành công', total });
});

// GET /api/shop/history - lịch sử mua hàng
router.get('/history', auth, (req, res) => {
  const history = db.prepare(`
    SELECT pu.*, p.name AS product_name FROM purchases pu
    JOIN products p ON p.id = pu.product_id
    WHERE pu.character_id = ?
    ORDER BY pu.purchased_at DESC
  `).all(req.characterId);
  res.json({ success: true, data: history });
});

module.exports = router;
