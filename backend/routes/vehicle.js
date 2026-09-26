// routes/vehicle.js - 🚲 Xe / phương tiện
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

router.get('/', auth, (req, res) => {
  const vehicles = db.prepare('SELECT * FROM vehicles').all();
  res.json({ success: true, data: vehicles });
});

router.get('/my', auth, (req, res) => {
  const myVehicles = db.prepare(`
    SELECT v.*, cv.purchased_at FROM character_vehicles cv
    JOIN vehicles v ON v.id = cv.vehicle_id
    WHERE cv.character_id = ?
  `).all(req.characterId);
  res.json({ success: true, data: myVehicles });
});

router.post('/:id/buy', auth, (req, res) => {
  const vehicleId = req.params.id;
  const vehicle = db.prepare('SELECT * FROM vehicles WHERE id = ?').get(vehicleId);
  if (!vehicle) return res.status(404).json({ success: false, message: 'Không tìm thấy phương tiện' });

  const wallet = db.prepare('SELECT * FROM wallets WHERE character_id = ?').get(req.characterId);
  if (wallet.coin < vehicle.price) return res.status(400).json({ success: false, message: 'Không đủ xu' });

  db.prepare('UPDATE wallets SET coin = coin - ? WHERE character_id = ?').run(vehicle.price, req.characterId);
  db.prepare('INSERT INTO character_vehicles (character_id, vehicle_id) VALUES (?,?)').run(req.characterId, vehicleId);

  res.json({ success: true, message: 'Mua phương tiện thành công' });
});

module.exports = router;
