// routes/house.js - 🏠 Nhà
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

router.get('/', auth, (req, res) => {
  const houses = db.prepare('SELECT * FROM houses').all();
  res.json({ success: true, data: houses });
});

router.get('/my', auth, (req, res) => {
  const myHouses = db.prepare(`
    SELECT h.*, ch.purchased_at FROM character_houses ch
    JOIN houses h ON h.id = ch.house_id
    WHERE ch.character_id = ?
  `).all(req.characterId);
  res.json({ success: true, data: myHouses });
});

router.post('/:id/buy', auth, (req, res) => {
  const houseId = req.params.id;
  const house = db.prepare('SELECT * FROM houses WHERE id = ?').get(houseId);
  if (!house) return res.status(404).json({ success: false, message: 'Không tìm thấy nhà' });

  const wallet = db.prepare('SELECT * FROM wallets WHERE character_id = ?').get(req.characterId);
  if (wallet.coin < house.price) return res.status(400).json({ success: false, message: 'Không đủ xu' });

  db.prepare('UPDATE wallets SET coin = coin - ? WHERE character_id = ?').run(house.price, req.characterId);
  db.prepare('INSERT INTO character_houses (character_id, house_id) VALUES (?,?)').run(req.characterId, houseId);

  res.json({ success: true, message: 'Mua nhà thành công' });
});

module.exports = router;
