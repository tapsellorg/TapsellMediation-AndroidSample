package ir.tapsell.sample.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ir.tapsell.sample.R
import ir.tapsell.sample.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding

    /**
     * [onCreate] is called only once. So you can use it to pass user consent.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setUserConsent(requireActivity(), true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        binding.btnRewardedVideo.setOnClickListener {
            navController.navigate(R.id.action_fragment_home_to_fragment_rewarded_video)
        }
        binding.btnInterstitial.setOnClickListener {
            navController.navigate(R.id.action_fragment_home_to_fragment_interstitial)
        }
        binding.btnAppOpen.setOnClickListener {
            navController.navigate(R.id.action_fragment_home_to_fragment_app_open)
        }
        binding.btnStandardBanner.setOnClickListener {
            navController.navigate(R.id.action_fragment_home_to_fragment_standard_banner)
        }
        binding.btnNativeBanner.setOnClickListener {
            navController.navigate(R.id.action_fragment_home_to_fragment_native_banner)
        }
        binding.btnNativeListBanner.setOnClickListener {
            navController.navigate(R.id.action_fragment_home_to_fragment_native_list)
        }
        binding.btnPreroll.setOnClickListener {
            navController.navigate(R.id.action_fragment_home_to_fragment_preroll)
        }
    }
}
