# 🏭 KCN Game

Game mô phỏng cuộc sống công nhân khu công nghiệp cho Android, kết nối tới backend API `game.foodkcn.com`.

```
📱 GAME KCN ANDROID
        │  HTTPS API
        ▼
🌐 game.foodkcn.com
        ├── 🔐 Đăng nhập / tài khoản      ├── 🏠 Nhà
        ├── 🏭 KCN                        ├── 🚲 Xe / phương tiện
        ├── 👤 Nhân vật                   ├── 🛒 Mua sắm
        ├── 💼 Nghề nghiệp / công ty      ├── 🏪 Cửa hàng / người bán
        ├── 📋 Nhiệm vụ                   ├── ❤️ Kết duyên
        ├── ⭐ Kỹ năng                    └── 🎁 Phần thưởng
        └── 🪙 Xu
```

## Cấu trúc thư mục

```
KCN_Game/
├── android/     → Source code app Android (Kotlin + Jetpack Compose)
├── backend/     → API backend mẫu (Node.js + Express + SQLite)
└── .github/workflows/build-apk.yml  → Tự động build APK bằng GitHub Actions
```

## 🚀 Build file APK bằng GitHub (không cần cài Android Studio)

1. Tạo một repository mới trên GitHub (ví dụ `kcn-game`), để **Public** hoặc **Private** đều được.
2. Đẩy (push) toàn bộ thư mục này lên repo đó:
   ```bash
   cd KCN_Game
   git init
   git add .
   git commit -m "Khởi tạo KCN Game"
   git branch -M main
   git remote add origin https://github.com/<ten-tai-khoan>/<ten-repo>.git
   git push -u origin main
   ```
3. Vào GitHub → repo vừa tạo → tab **Actions**. Workflow **"Build Android APK"** sẽ tự chạy ngay sau khi push (vì có thay đổi trong thư mục `android/`).
   - Nếu muốn chạy tay: vào tab Actions → chọn **Build Android APK** → **Run workflow**.
4. Đợi khoảng 3–6 phút cho lần build đầu tiên (Gradle tải về Android SDK/dependencies).
5. Khi chạy xong (dấu ✅ xanh), bấm vào lần chạy đó → kéo xuống mục **Artifacts** → tải file **`kcn-game-debug-apk`** (file `.zip` chứa `app-debug.apk` bên trong).
6. Giải nén, copy `app-debug.apk` vào điện thoại Android và cài đặt (cần bật "Cài từ nguồn không xác định" trong Settings).

> File APK này là bản **debug** (chưa ký release, chỉ để test). Khi nào bạn muốn phát hành lên Google Play, mình có thể thêm bước build **release APK/AAB có ký (signing)** — lúc đó cần bạn tạo keystore và lưu làm GitHub Secrets.

## ⚙️ Cấu hình địa chỉ API

App đang trỏ tới `https://game.foodkcn.com/` tại:
`android/app/src/main/java/com/foodkcn/game/data/remote/ApiClient.kt`

Nếu bạn chưa deploy backend thật lên domain đó, sửa `BASE_URL` thành địa chỉ server bạn đang chạy thử (ví dụ IP máy chủ, hoặc dịch vụ như Render/Railway) trước khi build APK.

## 🖥️ Chạy thử backend (tuỳ chọn, để test API trước khi deploy)

```bash
cd backend
npm install
cp .env.example .env
node seed.js        # chèn dữ liệu mẫu: KCN, nghề nghiệp, nhiệm vụ, kỹ năng, nhà, xe, sản phẩm
node server.js       # chạy tại http://localhost:3000
```

Backend đã được kiểm thử với các API: đăng ký/đăng nhập, danh sách KCN, thông tin nhân vật, xu, ứng tuyển/đi làm nhận lương, nhận/hoàn thành nhiệm vụ, nâng cấp kỹ năng — hoạt động đúng như mô tả.

## 📌 Việc cần làm tiếp (gợi ý mở rộng)

- Deploy backend lên server thật (Render, Railway, VPS...) và trỏ domain `game.foodkcn.com`.
- Thêm bước build **release APK có ký** trong GitHub Actions (dùng GitHub Secrets lưu keystore).
- Thêm ảnh/avatar thật, hiệu ứng, âm thanh cho game.
- Bổ sung realtime (WebSocket) nếu muốn nhiều người chơi thấy nhau cùng lúc trong KCN.
