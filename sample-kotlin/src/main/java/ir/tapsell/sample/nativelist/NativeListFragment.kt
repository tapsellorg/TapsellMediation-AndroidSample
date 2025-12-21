package ir.tapsell.sample.nativelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wada811.viewbindingktx.viewBinding
import ir.tapsell.sample.databinding.FragmentNativeListBinding
import kotlinx.coroutines.launch
import timber.log.Timber

class NativeListFragment internal constructor() : Fragment() {
    private val binding by viewBinding { FragmentNativeListBinding.bind(it) }

    private val viewModel: NativeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentNativeListBinding.inflate(LayoutInflater.from(context), null, false).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var foodsAdapter: FoodsAdapter

        with(binding.recyclerView) {
            setHasFixedSize(true) // to improve performance
            layoutManager = LinearLayoutManager(context)
            adapter = FoodsAdapter().also { foodsAdapter = it }
        }

        // infinity scroll implementation
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lm = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val visibleItemsCount = lm.childCount
                val totalItemCount = lm.itemCount
                val firstVisible = lm.findFirstVisibleItemPosition()
                val lastVisible = lm.findLastVisibleItemPosition()

                if (dy <= 0 && firstVisible != 0) return
                val shouldLoadMore = lastVisible + NativeListState.LoadMoreThreshold > totalItemCount
                val isLoading = viewModel.state.value.isLoading

                if (shouldLoadMore && !isLoading) {
                    Timber.v("onScroll for more data. first:$firstVisible last:$lastVisible total:$totalItemCount visible:$visibleItemsCount dy:$dy")
                    viewModel.loadMoreData()
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    foodsAdapter.submitList(state.items)
                }
            }
        }
    }
}