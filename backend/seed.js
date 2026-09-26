// seed.js - Chèn dữ liệu mẫu để test nhanh (chạy: node seed.js)
const db = require('./db');

const insertKcn = db.prepare('INSERT INTO kcns (name, description, map_image, max_players) VALUES (?,?,?,?)');
insertKcn.run('KCN Bình Dương', 'Khu công nghiệp lớn nhất khu vực phía Nam', 'kcn_binhduong.png', 1000);
insertKcn.run('KCN Đồng Nai', 'Khu công nghiệp cơ khí - điện tử', 'kcn_dongnai.png', 800);

const insertCareer = db.prepare('INSERT INTO careers (name, company_name, base_salary, required_level) VALUES (?,?,?,?)');
insertCareer.run('Công nhân may', 'Công ty May Việt Thắng', 50, 1);
insertCareer.run('Kỹ thuật viên', 'Công ty Điện Tử Sài Gòn', 120, 5);
insertCareer.run('Quản lý ca', 'Công ty Thực Phẩm KCN', 300, 10);

const insertQuest = db.prepare('INSERT INTO quests (title, description, reward_coin, reward_exp, required_level) VALUES (?,?,?,?,?)');
insertQuest.run('Làm quen KCN', 'Đi dạo quanh khu công nghiệp lần đầu', 100, 10, 1);
insertQuest.run('Ngày lương đầu tiên', 'Hoàn thành 1 ca làm việc', 200, 20, 1);
insertQuest.run('Kết bạn', 'Kết bạn với 3 nhân vật khác', 150, 15, 2);

const insertSkill = db.prepare('INSERT INTO skills (name, description, max_level) VALUES (?,?,?)');
insertSkill.run('Tay nghề may', 'Tăng tốc độ hoàn thành công việc may mặc', 10);
insertSkill.run('Giao tiếp', 'Tăng khả năng kết duyên và mở cửa hàng', 10);
insertSkill.run('Quản lý tài chính', 'Giảm chi phí khi mua sắm', 10);

const insertHouse = db.prepare('INSERT INTO houses (name, price, image) VALUES (?,?,?)');
insertHouse.run('Phòng trọ bình dân', 500, 'house_basic.png');
insertHouse.run('Căn hộ chung cư', 5000, 'house_apartment.png');
insertHouse.run('Nhà phố KCN', 20000, 'house_villa.png');

const insertVehicle = db.prepare('INSERT INTO vehicles (name, type, price, speed_bonus, image) VALUES (?,?,?,?,?)');
insertVehicle.run('Xe đạp cũ', 'xe_dap', 100, 5, 'bike_basic.png');
insertVehicle.run('Xe máy số', 'xe_may', 3000, 20, 'moto_basic.png');
insertVehicle.run('Xe máy tay ga', 'xe_may', 8000, 35, 'moto_scooter.png');

const insertProduct = db.prepare('INSERT INTO products (name, price, category, image, stock) VALUES (?,?,?,?,?)');
insertProduct.run('Cơm phần công nhân', 20, 'food', 'food_comphop.png', 999);
insertProduct.run('Áo đồng phục KCN', 80, 'clothes', 'shirt_uniform.png', 999);
insertProduct.run('Nước tăng lực', 15, 'food', 'drink_energy.png', 999);

console.log('✅ Seed dữ liệu mẫu hoàn tất.');
