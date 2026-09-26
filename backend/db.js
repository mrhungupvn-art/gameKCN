// db.js - Khởi tạo SQLite database và các bảng dữ liệu cho game KCN
const Database = require('better-sqlite3');
const path = require('path');

const db = new Database(path.join(__dirname, 'data', 'kcn_game.db'));
db.pragma('journal_mode = WAL');

db.exec(`
CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT UNIQUE NOT NULL,
  password_hash TEXT NOT NULL,
  email TEXT,
  created_at TEXT DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS characters (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  user_id INTEGER UNIQUE NOT NULL,
  name TEXT NOT NULL,
  gender TEXT DEFAULT 'nam',
  level INTEGER DEFAULT 1,
  exp INTEGER DEFAULT 0,
  hp INTEGER DEFAULT 100,
  energy INTEGER DEFAULT 100,
  avatar_url TEXT DEFAULT '',
  kcn_id INTEGER,
  FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS kcns (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  description TEXT,
  map_image TEXT,
  max_players INTEGER DEFAULT 500
);

CREATE TABLE IF NOT EXISTS careers (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  company_name TEXT NOT NULL,
  base_salary INTEGER DEFAULT 0,
  required_level INTEGER DEFAULT 1
);

CREATE TABLE IF NOT EXISTS character_careers (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  character_id INTEGER NOT NULL,
  career_id INTEGER NOT NULL,
  work_level INTEGER DEFAULT 1,
  last_work_at TEXT,
  FOREIGN KEY(character_id) REFERENCES characters(id),
  FOREIGN KEY(career_id) REFERENCES careers(id)
);

CREATE TABLE IF NOT EXISTS quests (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  description TEXT,
  reward_coin INTEGER DEFAULT 0,
  reward_exp INTEGER DEFAULT 0,
  required_level INTEGER DEFAULT 1
);

CREATE TABLE IF NOT EXISTS character_quests (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  character_id INTEGER NOT NULL,
  quest_id INTEGER NOT NULL,
  status TEXT DEFAULT 'in_progress',
  progress INTEGER DEFAULT 0,
  FOREIGN KEY(character_id) REFERENCES characters(id),
  FOREIGN KEY(quest_id) REFERENCES quests(id)
);

CREATE TABLE IF NOT EXISTS skills (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  description TEXT,
  max_level INTEGER DEFAULT 10
);

CREATE TABLE IF NOT EXISTS character_skills (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  character_id INTEGER NOT NULL,
  skill_id INTEGER NOT NULL,
  level INTEGER DEFAULT 1,
  FOREIGN KEY(character_id) REFERENCES characters(id),
  FOREIGN KEY(skill_id) REFERENCES skills(id)
);

CREATE TABLE IF NOT EXISTS wallets (
  character_id INTEGER PRIMARY KEY,
  coin INTEGER DEFAULT 0,
  gem INTEGER DEFAULT 0,
  FOREIGN KEY(character_id) REFERENCES characters(id)
);

CREATE TABLE IF NOT EXISTS houses (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  price INTEGER NOT NULL,
  image TEXT
);

CREATE TABLE IF NOT EXISTS character_houses (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  character_id INTEGER NOT NULL,
  house_id INTEGER NOT NULL,
  purchased_at TEXT DEFAULT (datetime('now')),
  FOREIGN KEY(character_id) REFERENCES characters(id),
  FOREIGN KEY(house_id) REFERENCES houses(id)
);

CREATE TABLE IF NOT EXISTS vehicles (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  type TEXT DEFAULT 'xe_dap',
  price INTEGER NOT NULL,
  speed_bonus INTEGER DEFAULT 0,
  image TEXT
);

CREATE TABLE IF NOT EXISTS character_vehicles (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  character_id INTEGER NOT NULL,
  vehicle_id INTEGER NOT NULL,
  purchased_at TEXT DEFAULT (datetime('now')),
  FOREIGN KEY(character_id) REFERENCES characters(id),
  FOREIGN KEY(vehicle_id) REFERENCES vehicles(id)
);

CREATE TABLE IF NOT EXISTS products (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL,
  price INTEGER NOT NULL,
  category TEXT,
  image TEXT,
  seller_id INTEGER,
  stock INTEGER DEFAULT 999
);

CREATE TABLE IF NOT EXISTS sellers (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  shop_name TEXT NOT NULL,
  character_id INTEGER,
  kcn_id INTEGER
);

CREATE TABLE IF NOT EXISTS purchases (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  character_id INTEGER NOT NULL,
  product_id INTEGER NOT NULL,
  quantity INTEGER DEFAULT 1,
  total_price INTEGER NOT NULL,
  purchased_at TEXT DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS relationships (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  character_id_1 INTEGER NOT NULL,
  character_id_2 INTEGER NOT NULL,
  status TEXT DEFAULT 'pending',
  affinity INTEGER DEFAULT 0,
  created_at TEXT DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS rewards (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  character_id INTEGER NOT NULL,
  type TEXT NOT NULL,
  title TEXT NOT NULL,
  coin INTEGER DEFAULT 0,
  gem INTEGER DEFAULT 0,
  claimed INTEGER DEFAULT 0,
  created_at TEXT DEFAULT (datetime('now'))
);
`);

module.exports = db;
