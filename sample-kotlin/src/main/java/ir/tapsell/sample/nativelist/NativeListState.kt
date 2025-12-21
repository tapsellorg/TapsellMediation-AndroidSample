package ir.tapsell.sample.nativelist

data class NativeListState(
    val items: List<FoodItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    companion object {
        const val LoadMoreThreshold = 3
    }
}