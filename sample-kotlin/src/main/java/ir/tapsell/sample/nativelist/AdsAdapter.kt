package ir.tapsell.sample.nativelist

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.AdStateListener
import ir.tapsell.mediation.ad.show.AdShowCompletionState
import ir.tapsell.mediation.ad.views.ntv.NativeAdView
import ir.tapsell.sample.databinding.ItemNativeListAdBinding
import org.jetbrains.annotations.TestOnly
import timber.log.Timber

internal class AdsAdapter(
    private val delegatedAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    private val adInterval: Int = 5,
    private val callback: Callback,
    private val activity: Activity,
    private val onAdIdRequest: () -> String?,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val delegatedObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            notifyDataSetChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            notifyDataSetChanged()
        }
    }

    init {
        delegatedAdapter.registerAdapterDataObserver(delegatedObserver)
    }

    override fun getItemCount(): Int {
        val foodCount = delegatedAdapter.itemCount
        if (foodCount == 0) return 0

        val adCount = foodCount / adInterval
        return foodCount + adCount
    }

    override fun getItemViewType(position: Int): Int =
        if (isAdPosition(position)) AD_VIEW_TYPE
        else delegatedAdapter.getItemViewType(toDelegatedPosition(position))

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == AD_VIEW_TYPE)
            AdViewHolder(viewGroup)
        else
            delegatedAdapter.onCreateViewHolder(viewGroup, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == AD_VIEW_TYPE) {
            val adId = onAdIdRequest()
            if (adId != null)
                (holder as AdViewHolder).bind(activity, adId)
            else {
                (holder as AdViewHolder).bindEmpty()
            }
        } else {
            delegatedAdapter.onBindViewHolder(holder, toDelegatedPosition(position))
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is AdViewHolder) {
            holder.unbind()
        } else
            delegatedAdapter.onViewRecycled(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        delegatedAdapter.unregisterAdapterDataObserver(delegatedObserver)
    }

    companion object {
        private const val AD_VIEW_TYPE = Int.MAX_VALUE
    }

    inner class AdViewHolder private constructor(
        private val binding: ItemNativeListAdBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        constructor(viewGroup: ViewGroup) : this(
            ItemNativeListAdBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )
        )

        fun bind(activity: Activity, adId: String) {
            binding.root.tag = adId // keep for destroying
            binding.adContainer.isVisible = true

            Timber.w("bind for $bindingAdapterPosition")
            binding.adLabel.text = "AD for $bindingAdapterPosition"

            Tapsell.showNativeAd(
                adId,
                NativeAdView.Builder(binding.adContainer)
                    .withMedia(binding.adMedia)
                    .withTitle(binding.adTitle)
                    .withDescription(binding.adDescription)
                    .withLogo(binding.adIcon)
                    .withCtaButton(binding.adCallToAction)
                    .withSponsored(binding.adSponsored)
                    .build(),
                activity,
                object : AdStateListener.Native {
                    override fun onAdClicked() {
                        Timber.w("onAdClicked")
                    }

                    override fun onAdFailed(message: String) {
                        Timber.e("onAdFailed: $message")
                    }

                    override fun onAdImpression() {
                        Timber.d("onAdImpression $adId")
                        callback.onAdImpressed(adId)
                    }

                    override fun onAdClosed(completionState: AdShowCompletionState) {
                        Timber.d("onAdClosed ${completionState.name}")
                    }
                }
            )
        }

        fun bindEmpty() {
            binding.adContainer.isVisible = false
        }

        fun unbind() {
            val adId = binding.root.tag as? String
            Tapsell.destroyNativeAd(adId)
        }
    }

    interface Callback {
        fun onAdImpressed(adId: String)
    }

    @TestOnly
    fun isAdPosition(position: Int): Boolean {
        return (position + 1) % (adInterval + 1) == 0
    }

    @TestOnly
    fun toDelegatedPosition(adapterPosition: Int): Int {
        if (isAdPosition(adapterPosition)) return -1
        val adsBefore = (adapterPosition + 1) / (adInterval + 1)
        return adapterPosition - adsBefore
    }
}