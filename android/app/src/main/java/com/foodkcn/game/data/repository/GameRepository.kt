package com.foodkcn.game.data.repository

import com.foodkcn.game.data.local.TokenManager
import com.foodkcn.game.data.model.*
import com.foodkcn.game.data.remote.ApiService

// Repository duy nhất bao toàn bộ 12 module nghiệp vụ của game.
// Mỗi hàm trả về Result<T> để ViewModel dễ xử lý thành công / lỗi.
class GameRepository(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    // ---- 🔐 Đăng nhập / tài khoản ----
    suspend fun login(username: String, password: String): Result<AuthResponse> = safeCall {
        val res = api.login(LoginRequest(username, password))
        val body = res.body()
        if (res.isSuccessful && body?.token != null) {
            tokenManager.saveSession(body.token, body.characterId)
        }
        body
    }

    suspend fun register(
        username: String, password: String, email: String?, characterName: String, gender: String
    ): Result<AuthResponse> = safeCall {
        val res = api.register(RegisterRequest(username, password, email, characterName, gender))
        val body = res.body()
        if (res.isSuccessful && body?.token != null) {
            tokenManager.saveSession(body.token, body.characterId)
        }
        body
    }

    suspend fun logout() = tokenManager.clearSession()

    // ---- 🏭 KCN ----
    suspend fun getKcnList(): Result<List<Kcn>> = safeCall { api.getKcnList().body()?.data }
    suspend fun joinKcn(id: Int): Result<Unit> = safeCall { api.joinKcn(id).body()?.data; Unit }

    // ---- 👤 Nhân vật ----
    suspend fun getMyCharacter(): Result<Character> = safeCall { api.getMyCharacter().body()?.data }

    // ---- 💼 Nghề nghiệp / công ty ----
    suspend fun getCareers(): Result<List<Career>> = safeCall { api.getCareers().body()?.data }
    suspend fun applyCareer(id: Int): Result<Unit> = safeCall { api.applyCareer(id).body()?.data; Unit }
    suspend fun workCareer(id: Int): Result<Unit> = safeCall { api.workCareer(id).body()?.data; Unit }

    // ---- 📋 Nhiệm vụ ----
    suspend fun getQuests(): Result<List<Quest>> = safeCall { api.getQuests().body()?.data }
    suspend fun acceptQuest(id: Int): Result<Unit> = safeCall { api.acceptQuest(id).body()?.data; Unit }
    suspend fun completeQuest(id: Int): Result<Unit> = safeCall { api.completeQuest(id).body()?.data; Unit }

    // ---- ⭐ Kỹ năng ----
    suspend fun getSkills(): Result<List<Skill>> = safeCall { api.getSkills().body()?.data }
    suspend fun upgradeSkill(id: Int): Result<Unit> = safeCall { api.upgradeSkill(id).body()?.data; Unit }

    // ---- 🪙 Xu ----
    suspend fun getWallet(): Result<Wallet> = safeCall { api.getWallet().body()?.data }

    // ---- 🏠 Nhà ----
    suspend fun getHouses(): Result<List<House>> = safeCall { api.getHouses().body()?.data }
    suspend fun getMyHouses(): Result<List<House>> = safeCall { api.getMyHouses().body()?.data }
    suspend fun buyHouse(id: Int): Result<Unit> = safeCall { api.buyHouse(id).body()?.data; Unit }

    // ---- 🚲 Xe / phương tiện ----
    suspend fun getVehicles(): Result<List<Vehicle>> = safeCall { api.getVehicles().body()?.data }
    suspend fun getMyVehicles(): Result<List<Vehicle>> = safeCall { api.getMyVehicles().body()?.data }
    suspend fun buyVehicle(id: Int): Result<Unit> = safeCall { api.buyVehicle(id).body()?.data; Unit }

    // ---- 🛒 Mua sắm ----
    suspend fun getProducts(category: String? = null): Result<List<Product>> =
        safeCall { api.getProducts(category).body()?.data }
    suspend fun buyProduct(productId: Int, quantity: Int): Result<Unit> =
        safeCall { api.buyProduct(BuyProductRequest(productId, quantity)).body()?.data; Unit }

    // ---- 🏪 Cửa hàng / người bán ----
    suspend fun getSellers(): Result<List<Seller>> = safeCall { api.getSellers().body()?.data }
    suspend fun registerSeller(shopName: String, kcnId: Int?): Result<Unit> =
        safeCall { api.registerSeller(RegisterSellerRequest(shopName, kcnId)).body()?.data; Unit }

    // ---- ❤️ Kết duyên ----
    suspend fun getRomanceCandidates(): Result<List<RomanceCandidate>> =
        safeCall { api.getRomanceCandidates().body()?.data }
    suspend fun proposeRomance(targetCharacterId: Int): Result<Unit> =
        safeCall { api.proposeRomance(ProposeRequest(targetCharacterId)).body()?.data; Unit }
    suspend fun getMyRelationship(): Result<Relationship?> = safeCall { api.getMyRelationship().body()?.data }

    // ---- 🎁 Phần thưởng ----
    suspend fun getRewards(): Result<List<Reward>> = safeCall { api.getRewards().body()?.data }
    suspend fun claimReward(id: Int): Result<Unit> = safeCall { api.claimReward(id).body()?.data; Unit }

    // Hàm tiện ích: bọc mọi lời gọi API vào try/catch để tránh crash app khi mất mạng / lỗi server
    private inline fun <T> safeCall(block: () -> T?): Result<T> {
        return try {
            val result = block()
            if (result != null) Result.success(result) else Result.failure(Exception("Không có dữ liệu trả về"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
