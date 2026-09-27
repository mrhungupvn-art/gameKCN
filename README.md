# Game KCN Android V1 - Registration Fix

Bản sửa giao diện đăng ký tài khoản Game KCN.

- Form đăng ký dùng ScrollView để nút đăng ký không bị khuất khi chạy landscape.
- Nút chính: **ĐĂNG KÝ & TẠO NHÂN VẬT**.
- Giới tính, vai trò và tính cách dùng danh sách chọn.
- API: `https://game.foodkcn.com/api/`
- Tài khoản Game KCN độc lập với Food KCN.

## GitHub Actions
Workflow: `.github/workflows/build-apk.yml`

Build debug APK bằng JDK 17 / Gradle 8.10.2.
