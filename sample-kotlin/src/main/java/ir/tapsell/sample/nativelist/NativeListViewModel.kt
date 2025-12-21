package ir.tapsell.sample.nativelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class NativeListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(NativeListState())
    val state = _state.asStateFlow()

    init {
        loadMoreData()
    }

    fun loadMoreData() {
        fetchMoreFoodItems()
    }

    private fun fetchMoreFoodItems() {
        if (_state.value.isLoading) return

        Timber.d("request for loading more items. current size: ${_state.value.items.size}")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val newItems = DataProvider.fetchMenuItems(
                context = application,
                lastId = state.value.items.size.toLong(),
                count = 10,
            )
            _state.update {
                it.copy(
                    isLoading = false,
                    items = it.items + newItems
                )
            }
        }
    }
}
