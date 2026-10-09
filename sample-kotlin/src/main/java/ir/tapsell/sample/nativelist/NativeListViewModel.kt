package ir.tapsell.sample.nativelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.request.RequestResultListener
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
        fetchMoreAdsIfRequired()
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

    fun consumeAd(adId: String) {
        _state.update { it.copy(adIds = it.adIds - adId) }

        fetchMoreAdsIfRequired()
    }

    fun pullAdId(): String? {
        return _state.value.adIds.firstOrNull()
    }

    private fun fetchMoreAdsIfRequired() {
        if (_state.value.adIds.size >= NativeListState.BatchAdRequestCount) {
            Timber.d("fetching ads skipped. current ads count: ${_state.value.adIds.size}")
            return
        }

        fetchMoreAds(NativeListState.ZoneId)
    }

    private fun fetchMoreAds(zoneId: String) {
        Timber.d("requesting for ad")
        Tapsell.requestMultipleNativeAds(
            zoneId,
            /* maximumCount = */NativeListState.BatchAdRequestCount,
            object : RequestResultListener {
                override fun onFailure(message: String) {
                    Timber.d("request ad failed: $message")
                }

                override fun onSuccess(adId: String) {
                    Timber.d("request ad success: id=$adId")
                    _state.update { it.copy(adIds = it.adIds + adId) }
                }
            }
        )
    }
}
