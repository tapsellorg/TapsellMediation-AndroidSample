package ir.tapsell.sample.rewarded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ir.tapsell.sample.databinding.FragmentRewardedVideoBinding
import ir.tapsell.sample.utils.addChip
import ir.tapsell.shared.TapsellAdNetworks
import ir.tapsell.shared.TapsellKeys.TapsellMediationKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RewardedVideoFragment : Fragment() {

    private var _binding: FragmentRewardedVideoBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RewardedVideoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRewardedVideoBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TapsellAdNetworks.map { adNetwork ->
            binding.chipAdNetworks.addChip(requireContext(), adNetwork.name) {
                binding.inputZone.setText(adNetwork.rewarded)
            }
        }

        binding.inputZone.setText(TapsellMediationKeys.rewarded)
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
        viewModel.requestAd(binding.inputZone.text.toString())
    }

    private fun showAd() {
        viewModel.showAd(requireActivity())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
