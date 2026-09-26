// routes/skill.js - ⭐ Kỹ năng
const express = require('express');
const db = require('../db');
const auth = require('../middleware/auth');
const router = express.Router();

router.get('/', auth, (req, res) => {
  const skills = db.prepare(`
    SELECT s.*, COALESCE(cs.level, 0) AS my_level
    FROM skills s
    LEFT JOIN character_skills cs
      ON cs.skill_id = s.id AND cs.character_id = ?
  `).all(req.characterId);
  res.json({ success: true, data: skills });
});

// POST /api/skill/:id/upgrade - nâng cấp kỹ năng bằng xu
router.post('/:id/upgrade', auth, (req, res) => {
  const skillId = req.params.id;
  const skill = db.prepare('SELECT * FROM skills WHERE id = ?').get(skillId);
  if (!skill) return res.status(404).json({ success: false, message: 'Không tìm thấy kỹ năng' });

  let cs = db.prepare('SELECT * FROM character_skills WHERE character_id = ? AND skill_id = ?')
    .get(req.characterId, skillId);

  const currentLevel = cs ? cs.level : 0;
  if (currentLevel >= skill.max_level) {
    return res.status(400).json({ success: false, message: 'Kỹ năng đã đạt cấp tối đa' });
  }

  const cost = (currentLevel + 1) * 100;
  const wallet = db.prepare('SELECT * FROM wallets WHERE character_id = ?').get(req.characterId);
  if (wallet.coin < cost) return res.status(400).json({ success: false, message: 'Không đủ xu' });

  db.prepare('UPDATE wallets SET coin = coin - ? WHERE character_id = ?').run(cost, req.characterId);

  if (cs) {
    db.prepare('UPDATE character_skills SET level = level + 1 WHERE id = ?').run(cs.id);
  } else {
    db.prepare('INSERT INTO character_skills (character_id, skill_id, level) VALUES (?,?,1)')
      .run(req.characterId, skillId);
  }

  res.json({ success: true, message: 'Nâng cấp kỹ năng thành công', cost });
});

module.exports = router;
