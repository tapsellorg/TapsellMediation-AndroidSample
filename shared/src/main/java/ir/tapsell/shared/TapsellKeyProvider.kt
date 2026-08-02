package ir.tapsell.shared

import android.content.Context
import android.util.Log
import org.json.JSONObject

data class Zone(
    val name: String,
    val type: ZoneType,
    val id: String,
)

// Not supported yet
private const val NO_SUPPORT_KEY = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"

enum class ZoneType {
    REWARDED,
    INTERSTITIAL,
    BANNER,
    APP_OPEN,
    NATIVE,
    PRE_ROLL,
}

private val TapsellMediationKeys = listOf(
    Zone("All AdNetworks", ZoneType.REWARDED, "1d710cc7-5e96-46ac-a3e9-8463300333e6"),
    Zone("All AdNetworks", ZoneType.INTERSTITIAL, "b3972749-f62a-475a-9ff2-cfc9e2a40f87"),
    Zone("All AdNetworks", ZoneType.APP_OPEN, NO_SUPPORT_KEY),
    Zone("All AdNetworks", ZoneType.BANNER, "e3d5999c-5990-4e31-8ce9-642ce040a7f4"),
    Zone("All AdNetworks", ZoneType.NATIVE, "d217e3e6-0070-4120-925d-5d39d0298893"),
    Zone("All AdNetworks (VIDEO)", ZoneType.NATIVE, "f3fb937a-bb3d-4e54-8d4c-2f4aa963c74c"),
    Zone("All AdNetworks", ZoneType.PRE_ROLL, "6977a96f-e265-4895-b219-33bb6ba3df85"),
)

private val LegacyKeys = listOf(
    Zone("Tapsell Legacy", ZoneType.REWARDED, "63b92f04-3d0f-4805-84a4-abeccf4edc18"),
    Zone("Tapsell Legacy", ZoneType.INTERSTITIAL, "0c4e2849-feea-4688-8280-0b0ae2ee0728"),
    Zone("Tapsell Legacy", ZoneType.BANNER, "4bf4e11d-0967-41e6-91f6-a128d9462f2a"),
    Zone("Tapsell Legacy", ZoneType.NATIVE, "b1b92bca-8a54-4b82-9f53-90ea2b1a912c"),
    Zone("Tapsell Legacy (VIDEO)", ZoneType.NATIVE, "236e51ac-4fed-4263-b50a-7240558c73cb"),
    Zone("Tapsell Legacy", ZoneType.PRE_ROLL, "ca7355fa-2b59-436f-9f73-70b408d54b6a"),
)

private val AdmobKeys = listOf(
    Zone("Admob", ZoneType.REWARDED, "d8f4ff72-e2e9-4e67-9eb8-f6d75787ef09"),
    Zone("Admob", ZoneType.INTERSTITIAL, "48b73764-8025-4c9d-9507-ce7a2c7f32ef"),
    Zone("Admob", ZoneType.APP_OPEN, NO_SUPPORT_KEY),
    Zone("Admob", ZoneType.BANNER, "f965455e-a37a-4732-a0b4-05fc39cae16e"),
    Zone("Admob", ZoneType.NATIVE, "15cacb1b-6598-4fe9-b2da-8b26b4c1bbc5"),
)

private val FyberKeys = listOf(
    Zone("Fyber", ZoneType.REWARDED, "42bd8edf-bdc9-4da5-ae6d-d465366b29f4"),
    Zone("Fyber", ZoneType.INTERSTITIAL, "ec274046-5cc7-40e0-973b-405084682967"),
    Zone("Fyber", ZoneType.BANNER, "10365955-147e-4fbc-9a08-15a930797902"),
)

private val ApplovinKeys = listOf(
    Zone("Applovin", ZoneType.REWARDED, "1abf9ca9-4f93-4ead-a238-b2d0d3032a7a"),
    Zone("Applovin", ZoneType.INTERSTITIAL, "8c33dc60-5911-4145-90b0-d8cce9594fed"),
    Zone("Applovin", ZoneType.BANNER, "1df4bf1e-4fef-4704-b776-7881bdad5303"),
    Zone("Applovin", ZoneType.NATIVE, "2db08af5-54e3-458e-9399-1d365a7516c9"),
)

private val MintegralKeys = listOf(
    Zone("Mintegral", ZoneType.REWARDED, "b4c24eba-b715-47aa-8e6e-641d19936765"),
    Zone("Mintegral", ZoneType.INTERSTITIAL, "b2ef073b-f9a1-4555-8450-ce30cf3cee98"),
    Zone("Mintegral", ZoneType.BANNER, "6c0b2878-c743-4668-b224-00b07ed66550"),
    Zone("Mintegral", ZoneType.NATIVE, "1a744a71-5abe-4d4a-8323-3832c89495eb"),
)

private val IronSourceKeys = listOf(
    Zone("IronSource", ZoneType.REWARDED, "59ea106a-10c2-4711-9b50-38c73200f56a"),
    Zone("IronSource", ZoneType.INTERSTITIAL, "e87e094b-1e51-468c-ba0c-b2752dc04d72"),
    Zone("IronSource", ZoneType.BANNER, "33063aae-07c0-4571-aeac-842c0c7f6478"),
)

private val LiftoffKeys = listOf(
    Zone("Liftoff", ZoneType.REWARDED, "0f447ea1-6b11-4e39-b942-712e2d696d78"),
    Zone("Liftoff", ZoneType.INTERSTITIAL, "2316460a-fbcc-4e29-8a8e-f10ef5358b3b"),
    Zone("Liftoff", ZoneType.BANNER, "c066375e-da10-45a8-be3b-6edf5668a0e4"),
)

private val ChartBoostKeys = listOf(
    Zone("ChartBoost", ZoneType.REWARDED, "3dde311c-daf5-4a6b-8ce7-08a4103cfb7f"),
    Zone("ChartBoost", ZoneType.INTERSTITIAL, "0d3e6f6d-8a11-4c32-a076-4415c03132a9"),
    Zone("ChartBoost", ZoneType.BANNER, "0d3e6f6d-8a11-4c32-a076-4415c03132a9"),
)

private val WortiseKeys = listOf(
    Zone("Wortise", ZoneType.REWARDED, "4fcf71a4-afd1-412d-9b19-46dd85644f9d"),
    Zone("Wortise", ZoneType.INTERSTITIAL, "4d268df4-7e8a-43d8-a014-02b4aeff1e72"),
    Zone("Wortise", ZoneType.BANNER, "ddd6c321-1f1f-4396-8524-a4f760063b2f"),
    Zone("Wortise", ZoneType.NATIVE, "88eab80b-113b-4856-8dc2-4aa571c4d7c3"),
)

private val YandexKeys = listOf(
    Zone("Yandex", ZoneType.REWARDED, "27891b5b-1ff5-48f2-98ad-096cfc2dda1a"),
    Zone("Yandex", ZoneType.INTERSTITIAL, "e0695171-a079-40c6-9dce-1b31ac2bce15"),
    Zone("Yandex", ZoneType.APP_OPEN, NO_SUPPORT_KEY),
    Zone("Yandex", ZoneType.BANNER, "d088d32c-47ce-4df8-9b2c-184716c77ec0"),
    Zone("Yandex", ZoneType.NATIVE, "26ac2ef1-5968-43c2-889b-bf36d68368c4"),
    Zone("Yandex", ZoneType.PRE_ROLL, NO_SUPPORT_KEY),
)

object TapsellKeyProvider {

    private const val ZONES_FILE_NAME = "tapsell.json"

    private val hardcodedZones = listOf(
        TapsellMediationKeys,
        LegacyKeys,
        AdmobKeys,
        FyberKeys,
        ApplovinKeys,
        MintegralKeys,
        IronSourceKeys,
        LiftoffKeys,
        ChartBoostKeys,
        WortiseKeys,
        YandexKeys,
    ).flatten()

    private var cachedZones: List<Zone>? = null
    private var zonesLoaded = false

    @JvmStatic
    fun zonesFor(context: Context, type: ZoneType): List<Zone> {
        if (!zonesLoaded) {
            cachedZones = readFromJson(context)
            zonesLoaded = true
        }
        return cachedZones?.filter { it.type == type }
            ?: hardcodedZones.filter { it.type == type }
    }

    private fun readFromJson(context: Context): List<Zone>? {
        return try {
            val json = context.assets.open(ZONES_FILE_NAME)
                .bufferedReader()
                .use { it.readText() }
            val root = JSONObject(json)
            val arr = root.optJSONArray("zones") ?: return emptyList()
            buildList {
                for (i in 0 until arr.length()) {
                    val obj = arr.optJSONObject(i) ?: continue
                    val typeStr = obj.optString("type").uppercase().trim()
                    val type = runCatching { ZoneType.valueOf(typeStr) }.getOrNull()
                    val name = obj.optString("name").trim()
                        .split(" ").filterNot { it.contentEquals(typeStr, ignoreCase = true) }
                        .joinToString(" ")
                    val id = obj.optString("id").trim()
                    if (name.isNotBlank() && id.isNotBlank() && type != null) {
                        add(Zone(name, type, id))
                    }
                }
            }
        } catch (e: Exception) {
            Log.i("TapsellKeyProvider", "Cannot read $ZONES_FILE_NAME; using hardcoded zone ids", e)
            null
        }
    }
}
