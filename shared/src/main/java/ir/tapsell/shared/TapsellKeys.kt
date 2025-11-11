package ir.tapsell.shared

val TapsellAdNetworks: List<TapsellKeys> = listOf(
    TapsellKeys.TapsellMediationKeys,
    TapsellKeys.LegacyKeys,
    TapsellKeys.AdmobKeys,
    TapsellKeys.FyberKeys,
    TapsellKeys.ApplovinKeys,
    TapsellKeys.MintegralKeys,
    TapsellKeys.IronSourceKeys,
    TapsellKeys.LiftoffKeys,
    TapsellKeys.ChartBoostKeys,
    TapsellKeys.WortiseKeys,
    TapsellKeys.YandexKeys,
).sorted
val TapsellAppOpenAdNetworks = TapsellAdNetworks.filter { it.appOpen != null }
val TapsellNativeAdNetworks = TapsellAdNetworks.filter { it.native != null }
val TapsellPreRollAdNetworks = TapsellAdNetworks.filter { it.preRoll != null }



// Not supported yet
private const val NO_SUPPORT_KEY = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"


sealed interface TapsellAdTypes {
    val rewarded: String
    val interstitial: String
    val banner: String
    val appOpen: String? get() = null
    val native: String? get() = null
    val preRoll: String? get() = null
}

sealed interface TapsellKeys : TapsellAdTypes {
    val name: String

    /**
     * This object contains all the mediation ad networks for tapsell mediation
     */
    data object TapsellMediationKeys : TapsellKeys {
        override val name = "All AdNetworks"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val appOpen get() = APP_OPEN
        override val banner get() = BANNER
        override val native get() = NATIVE_BANNER
        val nativeVideo get() = NATIVE_VIDEO
        override val preRoll get() = PRE_ROLL
        private const val REWARDED = "1d710cc7-5e96-46ac-a3e9-8463300333e6"
        private const val INTERSTITIAL = "b3972749-f62a-475a-9ff2-cfc9e2a40f87"
        private const val APP_OPEN = NO_SUPPORT_KEY
        private const val NATIVE_BANNER = "d217e3e6-0070-4120-925d-5d39d0298893"
        private const val NATIVE_VIDEO = "f3fb937a-bb3d-4e54-8d4c-2f4aa963c74c"
        private const val BANNER = "e3d5999c-5990-4e31-8ce9-642ce040a7f4"
        private const val PRE_ROLL = "6977a96f-e265-4895-b219-33bb6ba3df85"
    }

    data object LegacyKeys : TapsellKeys {
        override val name = "Tapsell Legacy"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val banner get() = BANNER
        override val native get() = NATIVE_BANNER
        override val preRoll get() = PRE_ROLL
        val nativeVideo get() = NATIVE_VIDEO
        private const val REWARDED = "63b92f04-3d0f-4805-84a4-abeccf4edc18"
        private const val INTERSTITIAL = "0c4e2849-feea-4688-8280-0b0ae2ee0728"
        private const val NATIVE_BANNER = "b1b92bca-8a54-4b82-9f53-90ea2b1a912c"
        private const val NATIVE_VIDEO = "236e51ac-4fed-4263-b50a-7240558c73cb"
        private const val BANNER = "4bf4e11d-0967-41e6-91f6-a128d9462f2a"
        private const val PRE_ROLL = "628f60d7-8fd0-4aea-a840-2d2947121dd5"
    }

    data object AdmobKeys : TapsellKeys {
        override val name = "Admob"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val appOpen get() = APP_OPEN
        override val banner get() = BANNER
        override val native get() = NATIVE
        private const val REWARDED = "d8f4ff72-e2e9-4e67-9eb8-f6d75787ef09"
        private const val INTERSTITIAL = "48b73764-8025-4c9d-9507-ce7a2c7f32ef"
        private const val APP_OPEN = NO_SUPPORT_KEY
        private const val NATIVE = "15cacb1b-6598-4fe9-b2da-8b26b4c1bbc5"
        private const val BANNER = "f965455e-a37a-4732-a0b4-05fc39cae16e"
    }

    data object FyberKeys : TapsellKeys {
        override val name = "Fyber"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val banner get() = BANNER
        private const val REWARDED = "42bd8edf-bdc9-4da5-ae6d-d465366b29f4"
        private const val INTERSTITIAL = "ec274046-5cc7-40e0-973b-405084682967"
        private const val BANNER = "10365955-147e-4fbc-9a08-15a930797902"
    }

    data object ApplovinKeys : TapsellKeys {
        override val name = "Applovin"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val banner get() = BANNER
        override val native get() = NATIVE
        private const val REWARDED = "1abf9ca9-4f93-4ead-a238-b2d0d3032a7a"
        private const val INTERSTITIAL = "8c33dc60-5911-4145-90b0-d8cce9594fed"
        private const val NATIVE = "2db08af5-54e3-458e-9399-1d365a7516c9"
        private const val BANNER = "1df4bf1e-4fef-4704-b776-7881bdad5303"
    }

    data object MintegralKeys : TapsellKeys {
        override val name = "Mintegral"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val banner get() = BANNER
        override val native get() = NATIVE
        private const val REWARDED = "b4c24eba-b715-47aa-8e6e-641d19936765"
        private const val INTERSTITIAL = "b2ef073b-f9a1-4555-8450-ce30cf3cee98"
        private const val NATIVE = "1a744a71-5abe-4d4a-8323-3832c89495eb"
        private const val BANNER = "6c0b2878-c743-4668-b224-00b07ed66550"
    }

    data object IronSourceKeys : TapsellKeys {
        override val name = "IronSource"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val banner get() = BANNER
        private const val REWARDED = "59ea106a-10c2-4711-9b50-38c73200f56a"
        private const val INTERSTITIAL = "e87e094b-1e51-468c-ba0c-b2752dc04d72"
        private const val BANNER = "33063aae-07c0-4571-aeac-842c0c7f6478"
    }

    data object LiftoffKeys : TapsellKeys {
        override val name = "Liftoff"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val banner get() = BANNER
        private const val REWARDED = "0f447ea1-6b11-4e39-b942-712e2d696d78"
        private const val INTERSTITIAL = "2316460a-fbcc-4e29-8a8e-f10ef5358b3b"
        private const val BANNER = "c066375e-da10-45a8-be3b-6edf5668a0e4"
    }

    data object ChartBoostKeys : TapsellKeys {
        override val name = "ChartBoost"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val banner get() = BANNER
        private const val REWARDED = "3dde311c-daf5-4a6b-8ce7-08a4103cfb7f"
        private const val INTERSTITIAL = "0d3e6f6d-8a11-4c32-a076-4415c03132a9"
        private const val BANNER = "0d3e6f6d-8a11-4c32-a076-4415c03132a9"
    }

    data object WortiseKeys : TapsellKeys {
        override val name = "Wortise"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val banner get() = BANNER
        override val native get() = NATIVE
        private const val REWARDED = "4fcf71a4-afd1-412d-9b19-46dd85644f9d"
        private const val INTERSTITIAL = "4d268df4-7e8a-43d8-a014-02b4aeff1e72"
        private const val NATIVE = "88eab80b-113b-4856-8dc2-4aa571c4d7c3"
        private const val BANNER = "ddd6c321-1f1f-4396-8524-a4f760063b2f"
    }

    data object YandexKeys : TapsellKeys {
        override val name = "Yandex"
        override val rewarded get() = REWARDED
        override val interstitial get() = INTERSTITIAL
        override val appOpen get() = APP_OPEN
        override val banner get() = BANNER
        override val native get() = NATIVE
        override val preRoll get() = PRE_ROLL
        private const val REWARDED = "27891b5b-1ff5-48f2-98ad-096cfc2dda1a"
        private const val INTERSTITIAL = "e0695171-a079-40c6-9dce-1b31ac2bce15"
        private const val APP_OPEN = NO_SUPPORT_KEY
        private const val NATIVE = "26ac2ef1-5968-43c2-889b-bf36d68368c4"
        private const val BANNER = "d088d32c-47ce-4df8-9b2c-184716c77ec0"
        private const val PRE_ROLL = NO_SUPPORT_KEY
    }
}

object TapsellManifestKeys {
    const val TAPSELL_APP_ID = "ir.tapsell.mediation.APPLICATION_KEY"
    const val ADMOB_APP_ID = "com.google.android.gms.ads.APPLICATION_ID"
}
