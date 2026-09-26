// routes/quest.js - 📋 Nhiệm vụ
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

// GET /api/quest - danh sách nhiệm vụ + trạng thái của nhân vật
router.get('/', auth, (req, res) => {
  const quests = db.prepare(`
    SELECT q.*, cq.status, cq.progress
    FROM quests q
    LEFT JOIN character_quests cq
      ON cq.quest_id = q.id AND cq.character_id = ?
  `).all(req.characterId);
  res.json({ success: true, data: quests });
});

// POST /api/quest/:id/accept - nhận nhiệm vụ
router.post('/:id/accept', auth, (req, res) => {
  const questId = req.params.id;
  const existing = db.prepare('SELECT * FROM character_quests WHERE character_id = ? AND quest_id = ?')
    .get(req.characterId, questId);
  if (existing) return res.json({ success: true, message: 'Đã nhận nhiệm vụ trước đó', data: existing });

  const info = db.prepare('INSERT INTO character_quests (character_id, quest_id) VALUES (?,?)')
    .run(req.characterId, questId);
  res.json({ success: true, id: info.lastInsertRowid });
});

// POST /api/quest/:id/complete - hoàn thành nhiệm vụ, nhận thưởng
router.post('/:id/complete', auth, (req, res) => {
  const questId = req.params.id;
  const quest = db.prepare('SELECT * FROM quests WHERE id = ?').get(questId);
  const cq = db.prepare('SELECT * FROM character_quests WHERE character_id = ? AND quest_id = ?')
    .get(req.characterId, questId);
  if (!quest || !cq) return res.status(400).json({ success: false, message: 'Nhiệm vụ chưa được nhận' });

  db.prepare('UPDATE character_quests SET status = \'completed\' WHERE id = ?').run(cq.id);
  db.prepare('UPDATE wallets SET coin = coin + ? WHERE character_id = ?').run(quest.reward_coin, req.characterId);
  db.prepare('UPDATE characters SET exp = exp + ? WHERE id = ?').run(quest.reward_exp, req.characterId);

  db.prepare('INSERT INTO rewards (character_id, type, title, coin) VALUES (?,?,?,?)')
    .run(req.characterId, 'quest', `Hoàn thành: ${quest.title}`, quest.reward_coin);

  res.json({ success: true, message: 'Hoàn thành nhiệm vụ', reward_coin: quest.reward_coin, reward_exp: quest.reward_exp });
});

module.exports = router;
