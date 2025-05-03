package ir.tapsell.sample.native

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ir.tapsell.mediation.ad.views.ntv.NativeAdViewContainer
import ir.tapsell.sample.R
import ir.tapsell.sample.databinding.FragmentNativeBinding
import ir.tapsell.sample.utils.addChip
import ir.tapsell.shared.MULTIPLE_NATIVE_REQUESTS_COUNT
import ir.tapsell.shared.TapsellKeys
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys
import ir.tapsell.shared.TapsellNativeAdNetworks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NativeFragment : Fragment() {

    private var _binding: FragmentNativeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NativeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNativeBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TapsellNativeAdNetworks.map { adNetwork ->
            binding.chipAdNetworks.addChip(requireContext(), adNetwork.name) {
                viewModel.updateSelectedAdNetwork(adNetwork)
            }
        }

        lifecycleScope.launch {
            viewModel.selectedAdNetwork.collect { adNetwork ->
                updateZoneInput(adNetwork, binding.switchNativeType.isChecked)
                binding.switchNativeType.setOnCheckedChangeListener { _, isChecked ->
                    updateZoneInput(adNetwork, isChecked)
                }
            }
        }

        binding.tvSwitchNativeVideo.setOnClickListener {
            binding.switchNativeType.isChecked = binding.switchNativeType.isChecked.not()
        }

        binding.btnRequest.setOnClickListener {
            requestAd()
        }
        binding.btnRequestMultiple.setOnClickListener {
            requestAd(MULTIPLE_NATIVE_REQUESTS_COUNT)
        }
        binding.btnShow.setOnClickListener {
            showAd()
        }
        binding.btnDestroy.setOnClickListener {
            destroyAd()
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.logMessage.collect {
                binding.tvLog.text = it
            }
        }
    }

    private fun updateZoneInput(adNetwork: TapsellKeys, isNativeVideo: Boolean = false) {
        if (isNativeVideo) {
            // Native video is only available for `TapsellMediationKeys` and `LegacyKeys`
            when (adNetwork) {
                is TapsellMediationKeys -> binding.inputZone.setText(adNetwork.nativeVideo)
                is TapsellKeys.LegacyKeys -> binding.inputZone.setText(adNetwork.nativeVideo)
                else -> binding.inputZone.setText(adNetwork.native)
            }
        } else binding.inputZone.setText(adNetwork.native)
    }

    private fun requestAd(count: Int = 1) {
        viewModel.requestAd(binding.inputZone.text.toString(), count)
    }

    private fun showAd() = NativeAdViewContainer(requireContext()).let {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.banner_container, it, true)
        binding.nativeBannerContainer.addView(view)
        viewModel.showAd(requireActivity(), it)
    }

    private fun destroyAd() {
        viewModel.destroyAd()
    }
}
