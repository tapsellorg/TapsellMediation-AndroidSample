package ir.tapsell.sample.standard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ir.tapsell.mediation.Tapsell
import ir.tapsell.mediation.ad.request.BannerSize
import ir.tapsell.mediation.ad.views.banner.BannerContainer
import ir.tapsell.sample.databinding.FragmentStandardBannerBinding
import ir.tapsell.sample.utils.addChip
import ir.tapsell.shared.TapsellAdNetworks
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StandardBannerFragment : Fragment() {

    private var _binding: FragmentStandardBannerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<StandardBannerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStandardBannerBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TapsellAdNetworks.map { adNetwork ->
            binding.chipAdNetworks.addChip(requireContext(), adNetwork.name) {
                binding.inputZone.setText(adNetwork.banner)
            }
        }
        binding.spinnerBannerSizes.apply {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                viewModel.tapsellBannerSizes
            )
            onItemSelectedListener = viewModel.spinnerItemSelectedListener
            setSelection(BannerSize.valueOf(BannerSize.BANNER_300_250.name).ordinal)
        }
        binding.inputZone.setText(TapsellMediationKeys.banner)
        binding.btnRequest.setOnClickListener {
            requestAd()
        }
        binding.btnShow.setOnClickListener {
            showAd()
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.logMessage.collect {
                binding.tvLog.text = it
            }
        }
    }

    private fun requestAd() {
        viewModel.requestAd(
            binding.inputZone.text.toString()
        )
    }

    private fun showAd() = BannerContainer(requireContext()).let {
        binding.bannerContainerView.addView(it)
        viewModel.showAd(requireActivity(), it)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Tapsell.destroyBannerAd(viewModel.responseId)
    }
}
