package com.foodkcn.game.data.model

// ===== 🔐 Đăng nhập / tài khoản =====
data class LoginRequest(val username: String, val password: String)
data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String?,
    val characterName: String,
    val gender: String
)
data class AuthResponse(
    val success: Boolean,
    val token: String?,
    val characterId: Int?,
    val message: String?
)

// ===== 🏭 KCN =====
data class Kcn(
    val id: Int,
    val name: String,
    val description: String?,
    val map_image: String?,
    val max_players: Int
)

// ===== 👤 Nhân vật =====
data class Character(
    val id: Int,
    val user_id: Int,
    val name: String,
    val gender: String,
    val level: Int,
    val exp: Int,
    val hp: Int,
    val energy: Int,
    val avatar_url: String?,
    val kcn_id: Int?
)

// ===== 💼 Nghề nghiệp / công ty =====
data class Career(
    val id: Int,
    val name: String,
    val company_name: String,
    val base_salary: Int,
    val required_level: Int
)

// ===== 📋 Nhiệm vụ =====
data class Quest(
    val id: Int,
    val title: String,
    val description: String?,
    val reward_coin: Int,
    val reward_exp: Int,
    val required_level: Int,
    val status: String?,
    val progress: Int?
)

// ===== ⭐ Kỹ năng =====
data class Skill(
    val id: Int,
    val name: String,
    val description: String?,
    val max_level: Int,
    val my_level: Int
)

// ===== 🪙 Xu =====
data class Wallet(
    val character_id: Int,
    val coin: Int,
    val gem: Int
)

// ===== 🏠 Nhà =====
data class House(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String?
)

// ===== 🚲 Xe / phương tiện =====
data class Vehicle(
    val id: Int,
    val name: String,
    val type: String,
    val price: Int,
    val speed_bonus: Int,
    val image: String?
)

// ===== 🛒 Mua sắm =====
data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val category: String?,
    val image: String?,
    val seller_id: Int?,
    val stock: Int
)
data class BuyProductRequest(val productId: Int, val quantity: Int)

// ===== 🏪 Cửa hàng / người bán =====
data class Seller(
    val id: Int,
    val shop_name: String,
    val character_id: Int?,
    val kcn_id: Int?
)
data class RegisterSellerRequest(val shopName: String, val kcnId: Int?)

// ===== ❤️ Kết duyên =====
data class RomanceCandidate(
    val id: Int,
    val name: String,
    val gender: String,
    val level: Int,
    val avatar_url: String?
)
data class ProposeRequest(val targetCharacterId: Int)
data class Relationship(
    val id: Int,
    val character_id_1: Int,
    val character_id_2: Int,
    val status: String,
    val affinity: Int
)

// ===== 🎁 Phần thưởng =====
data class Reward(
    val id: Int,
    val character_id: Int,
    val type: String,
    val title: String,
    val coin: Int,
    val gem: Int,
    val claimed: Int
)

// ===== Envelope chung cho mọi response =====
data class ApiEnvelope<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)
