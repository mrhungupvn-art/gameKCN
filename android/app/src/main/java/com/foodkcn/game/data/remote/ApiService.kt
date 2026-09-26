package com.foodkcn.game.data.remote

import com.foodkcn.game.data.model.*
import retrofit2.Response
import retrofit2.http.*

// Tất cả endpoint tương ứng với sơ đồ kiến trúc game.foodkcn.com
interface ApiService {

    // 🔐 Đăng nhập / tài khoản
    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    // 🏭 KCN
    @GET("api/kcn")
    suspend fun getKcnList(): Response<ApiEnvelope<List<Kcn>>>

    @POST("api/kcn/{id}/join")
    suspend fun joinKcn(@Path("id") id: Int): Response<ApiEnvelope<Unit>>

    // 👤 Nhân vật
    @GET("api/character/me")
    suspend fun getMyCharacter(): Response<ApiEnvelope<Character>>

    // 💼 Nghề nghiệp / công ty
    @GET("api/career")
    suspend fun getCareers(): Response<ApiEnvelope<List<Career>>>

    @POST("api/career/{id}/apply")
    suspend fun applyCareer(@Path("id") id: Int): Response<ApiEnvelope<Unit>>

    @POST("api/career/{id}/work")
    suspend fun workCareer(@Path("id") id: Int): Response<ApiEnvelope<Unit>>

    // 📋 Nhiệm vụ
    @GET("api/quest")
    suspend fun getQuests(): Response<ApiEnvelope<List<Quest>>>

    @POST("api/quest/{id}/accept")
    suspend fun acceptQuest(@Path("id") id: Int): Response<ApiEnvelope<Unit>>

    @POST("api/quest/{id}/complete")
    suspend fun completeQuest(@Path("id") id: Int): Response<ApiEnvelope<Unit>>

    // ⭐ Kỹ năng
    @GET("api/skill")
    suspend fun getSkills(): Response<ApiEnvelope<List<Skill>>>

    @POST("api/skill/{id}/upgrade")
    suspend fun upgradeSkill(@Path("id") id: Int): Response<ApiEnvelope<Unit>>

    // 🪙 Xu
    @GET("api/wallet")
    suspend fun getWallet(): Response<ApiEnvelope<Wallet>>

    // 🏠 Nhà
    @GET("api/house")
    suspend fun getHouses(): Response<ApiEnvelope<List<House>>>

    @GET("api/house/my")
    suspend fun getMyHouses(): Response<ApiEnvelope<List<House>>>

    @POST("api/house/{id}/buy")
    suspend fun buyHouse(@Path("id") id: Int): Response<ApiEnvelope<Unit>>

    // 🚲 Xe / phương tiện
    @GET("api/vehicle")
    suspend fun getVehicles(): Response<ApiEnvelope<List<Vehicle>>>

    @GET("api/vehicle/my")
    suspend fun getMyVehicles(): Response<ApiEnvelope<List<Vehicle>>>

    @POST("api/vehicle/{id}/buy")
    suspend fun buyVehicle(@Path("id") id: Int): Response<ApiEnvelope<Unit>>

    // 🛒 Mua sắm
    @GET("api/shop/products")
    suspend fun getProducts(@Query("category") category: String? = null): Response<ApiEnvelope<List<Product>>>

    @POST("api/shop/buy")
    suspend fun buyProduct(@Body body: BuyProductRequest): Response<ApiEnvelope<Unit>>

    // 🏪 Cửa hàng / người bán
    @GET("api/seller")
    suspend fun getSellers(): Response<ApiEnvelope<List<Seller>>>

    @POST("api/seller/register")
    suspend fun registerSeller(@Body body: RegisterSellerRequest): Response<ApiEnvelope<Unit>>

    // ❤️ Kết duyên
    @GET("api/romance/candidates")
    suspend fun getRomanceCandidates(): Response<ApiEnvelope<List<RomanceCandidate>>>

    @POST("api/romance/propose")
    suspend fun proposeRomance(@Body body: ProposeRequest): Response<ApiEnvelope<Unit>>

    @GET("api/romance/my")
    suspend fun getMyRelationship(): Response<ApiEnvelope<Relationship?>>

    // 🎁 Phần thưởng
    @GET("api/reward")
    suspend fun getRewards(): Response<ApiEnvelope<List<Reward>>>

    @POST("api/reward/{id}/claim")
    suspend fun claimReward(@Path("id") id: Int): Response<ApiEnvelope<Unit>>
}
