package ir.tapsell.sample.nativelist

import android.content.Context
import ir.tapsell.sample.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.random.Random

object DataProvider {
    // simulate an async operation
    suspend fun fetchMenuItems(
        context: Context,
        lastId: Long,
        count: Int,
    ): List<FoodItem> = withContext(Dispatchers.IO) {
        delay(Random.nextLong(500, 2000))
        val list = mutableListOf<FoodItem>()
        try {
            val menuItemsJsonArray = JSONArray(readJsonDataFromFile(context))
            for (i in 0 until menuItemsJsonArray.length()) {
                val menuItemObject = menuItemsJsonArray.getJSONObject(i)
                val menuItem = FoodItem(
                    id = lastId + i,
                    name = menuItemObject.getString("name"),
                    description = menuItemObject.getString("description"),
                    price = menuItemObject.getString("price"),
                    category = menuItemObject.getString("category"),
                    imageName = menuItemObject.getString("photo")
                )
                if (list.size >= count) break
                list.add(menuItem)
            }
        } catch (exception: IOException) {
            Timber.e(exception, "Unable to parse JSON file.")
        } catch (exception: JSONException) {
            Timber.e(exception, "Unable to parse JSON file.")
        }

        list
    }

    /**
     * Reads the JSON file and converts the JSON data to a [String].
     *
     * @return A [String] representation of the JSON data.
     * @throws IOException if unable to read the JSON file.
     */
    @Throws(IOException::class)
    private fun readJsonDataFromFile(context: Context): String {
        var inputStream: InputStream? = null
        val builder = StringBuilder()
        try {
            var jsonDataString: String?
            inputStream = context.resources.openRawResource(R.raw.food_menu)
            val bufferedReader = BufferedReader(
                InputStreamReader(inputStream, "UTF-8")
            )
            while (bufferedReader.readLine().also { jsonDataString = it } != null) {
                builder.append(jsonDataString)
            }
        } finally {
            inputStream?.close()
        }
        return String(builder)
    }
}