package ir.tapsell.sample.nativelist

import ir.tapsell.shared.TapsellKeys

data class NativeListState(
    val items: List<FoodItem> = emptyList(),
    val adIds: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    companion object {
        val ZoneId = TapsellKeys.TapsellMediationKeys.native
        const val BatchAdRequestCount = 5
        const val LoadMoreThreshold = 3
        const val AdInterval = 5
    }
}