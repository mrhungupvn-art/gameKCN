package com.foodkcn.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodkcn.game.data.model.*
import com.foodkcn.game.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Trạng thái dùng chung cho UI: đang tải / lỗi / dữ liệu
data class UiState<T>(
    val loading: Boolean = false,
    val error: String? = null,
    val data: T? = null
)

// 1 ViewModel duy nhất bao toàn bộ các màn hình - đơn giản hoá cho bản khởi đầu.
// Có thể tách nhỏ thành nhiều ViewModel theo màn hình khi mở rộng game.
class GameViewModel(private val repo: GameRepository) : ViewModel() {

    // ---- Auth ----
    private val _authState = MutableStateFlow(UiState<AuthResponse>())
    val authState: StateFlow<UiState<AuthResponse>> = _authState.asStateFlow()

    fun login(username: String, password: String) = viewModelScope.launch {
        _authState.value = UiState(loading = true)
        val result = repo.login(username, password)
        _authState.value = result.fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun register(username: String, password: String, email: String?, characterName: String, gender: String) =
        viewModelScope.launch {
            _authState.value = UiState(loading = true)
            val result = repo.register(username, password, email, characterName, gender)
            _authState.value = result.fold(
                onSuccess = { UiState(data = it) },
                onFailure = { UiState(error = it.message) }
            )
        }

    // ---- Nhân vật + Xu (hiển thị trên thanh trạng thái luôn) ----
    private val _character = MutableStateFlow(UiState<Character>())
    val character: StateFlow<UiState<Character>> = _character.asStateFlow()

    private val _wallet = MutableStateFlow(UiState<Wallet>())
    val wallet: StateFlow<UiState<Wallet>> = _wallet.asStateFlow()

    fun loadProfile() = viewModelScope.launch {
        _character.value = UiState(loading = true)
        _character.value = repo.getMyCharacter().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
        _wallet.value = repo.getWallet().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    // ---- KCN ----
    private val _kcnList = MutableStateFlow(UiState<List<Kcn>>())
    val kcnList: StateFlow<UiState<List<Kcn>>> = _kcnList.asStateFlow()

    fun loadKcnList() = viewModelScope.launch {
        _kcnList.value = UiState(loading = true)
        _kcnList.value = repo.getKcnList().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun joinKcn(id: Int) = viewModelScope.launch { repo.joinKcn(id) }

    // ---- Nghề nghiệp ----
    private val _careers = MutableStateFlow(UiState<List<Career>>())
    val careers: StateFlow<UiState<List<Career>>> = _careers.asStateFlow()

    fun loadCareers() = viewModelScope.launch {
        _careers.value = UiState(loading = true)
        _careers.value = repo.getCareers().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun applyCareer(id: Int) = viewModelScope.launch { repo.applyCareer(id) }
    fun workCareer(id: Int) = viewModelScope.launch {
        repo.workCareer(id)
        loadProfile() // cập nhật lại xu sau khi đi làm
    }

    // ---- Nhiệm vụ ----
    private val _quests = MutableStateFlow(UiState<List<Quest>>())
    val quests: StateFlow<UiState<List<Quest>>> = _quests.asStateFlow()

    fun loadQuests() = viewModelScope.launch {
        _quests.value = UiState(loading = true)
        _quests.value = repo.getQuests().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun acceptQuest(id: Int) = viewModelScope.launch { repo.acceptQuest(id); loadQuests() }
    fun completeQuest(id: Int) = viewModelScope.launch {
        repo.completeQuest(id)
        loadQuests()
        loadProfile()
    }

    // ---- Kỹ năng ----
    private val _skills = MutableStateFlow(UiState<List<Skill>>())
    val skills: StateFlow<UiState<List<Skill>>> = _skills.asStateFlow()

    fun loadSkills() = viewModelScope.launch {
        _skills.value = UiState(loading = true)
        _skills.value = repo.getSkills().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun upgradeSkill(id: Int) = viewModelScope.launch {
        repo.upgradeSkill(id)
        loadSkills()
        loadProfile()
    }

    // ---- Nhà ----
    private val _houses = MutableStateFlow(UiState<List<House>>())
    val houses: StateFlow<UiState<List<House>>> = _houses.asStateFlow()

    fun loadHouses() = viewModelScope.launch {
        _houses.value = UiState(loading = true)
        _houses.value = repo.getHouses().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun buyHouse(id: Int) = viewModelScope.launch { repo.buyHouse(id); loadProfile() }

    // ---- Xe / phương tiện ----
    private val _vehicles = MutableStateFlow(UiState<List<Vehicle>>())
    val vehicles: StateFlow<UiState<List<Vehicle>>> = _vehicles.asStateFlow()

    fun loadVehicles() = viewModelScope.launch {
        _vehicles.value = UiState(loading = true)
        _vehicles.value = repo.getVehicles().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun buyVehicle(id: Int) = viewModelScope.launch { repo.buyVehicle(id); loadProfile() }

    // ---- Mua sắm ----
    private val _products = MutableStateFlow(UiState<List<Product>>())
    val products: StateFlow<UiState<List<Product>>> = _products.asStateFlow()

    fun loadProducts(category: String? = null) = viewModelScope.launch {
        _products.value = UiState(loading = true)
        _products.value = repo.getProducts(category).fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun buyProduct(productId: Int, quantity: Int = 1) = viewModelScope.launch {
        repo.buyProduct(productId, quantity)
        loadProfile()
    }

    // ---- Cửa hàng / người bán ----
    private val _sellers = MutableStateFlow(UiState<List<Seller>>())
    val sellers: StateFlow<UiState<List<Seller>>> = _sellers.asStateFlow()

    fun loadSellers() = viewModelScope.launch {
        _sellers.value = UiState(loading = true)
        _sellers.value = repo.getSellers().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun registerSeller(shopName: String, kcnId: Int?) = viewModelScope.launch {
        repo.registerSeller(shopName, kcnId)
        loadSellers()
    }

    // ---- Kết duyên ----
    private val _romanceCandidates = MutableStateFlow(UiState<List<RomanceCandidate>>())
    val romanceCandidates: StateFlow<UiState<List<RomanceCandidate>>> = _romanceCandidates.asStateFlow()

    fun loadRomanceCandidates() = viewModelScope.launch {
        _romanceCandidates.value = UiState(loading = true)
        _romanceCandidates.value = repo.getRomanceCandidates().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun proposeRomance(targetCharacterId: Int) = viewModelScope.launch { repo.proposeRomance(targetCharacterId) }

    // ---- Phần thưởng ----
    private val _rewards = MutableStateFlow(UiState<List<Reward>>())
    val rewards: StateFlow<UiState<List<Reward>>> = _rewards.asStateFlow()

    fun loadRewards() = viewModelScope.launch {
        _rewards.value = UiState(loading = true)
        _rewards.value = repo.getRewards().fold(
            onSuccess = { UiState(data = it) },
            onFailure = { UiState(error = it.message) }
        )
    }

    fun claimReward(id: Int) = viewModelScope.launch {
        repo.claimReward(id)
        loadRewards()
        loadProfile()
    }
}
