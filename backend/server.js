// server.js - Entry point cho API game.foodkcn.com
require('dotenv').config();
const express = require('express');
const cors = require('cors');

const authRoutes = require('./routes/auth');
const kcnRoutes = require('./routes/kcn');
const characterRoutes = require('./routes/character');
const careerRoutes = require('./routes/career');
const questRoutes = require('./routes/quest');
const skillRoutes = require('./routes/skill');
const walletRoutes = require('./routes/wallet');
const houseRoutes = require('./routes/house');
const vehicleRoutes = require('./routes/vehicle');
const shopRoutes = require('./routes/shop');
const sellerRoutes = require('./routes/seller');
const romanceRoutes = require('./routes/romance');
const rewardRoutes = require('./routes/reward');

const app = express();
app.use(cors());
app.use(express.json());

// Ghép route đúng theo sơ đồ kiến trúc:
app.use('/api/auth', authRoutes);         // 🔐 Đăng nhập / tài khoản
app.use('/api/kcn', kcnRoutes);           // 🏭 KCN
app.use('/api/character', characterRoutes); // 👤 Nhân vật
app.use('/api/career', careerRoutes);     // 💼 Nghề nghiệp / công ty
app.use('/api/quest', questRoutes);       // 📋 Nhiệm vụ
app.use('/api/skill', skillRoutes);       // ⭐ Kỹ năng
app.use('/api/wallet', walletRoutes);     // 🪙 Xu
app.use('/api/house', houseRoutes);       // 🏠 Nhà
app.use('/api/vehicle', vehicleRoutes);   // 🚲 Xe / phương tiện
app.use('/api/shop', shopRoutes);         // 🛒 Mua sắm
app.use('/api/seller', sellerRoutes);     // 🏪 Cửa hàng / người bán
app.use('/api/romance', romanceRoutes);   // ❤️ Kết duyên
app.use('/api/reward', rewardRoutes);     // 🎁 Phần thưởng

app.get('/', (req, res) => {
  res.json({ success: true, message: 'KCN Game API đang chạy — game.foodkcn.com' });
});

app.use((req, res) => {
  res.status(404).json({ success: false, message: 'Không tìm thấy endpoint' });
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`✅ KCN Game API đang chạy tại http://localhost:${PORT}`);
});
