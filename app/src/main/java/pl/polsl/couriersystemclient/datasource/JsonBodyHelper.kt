package pl.polsl.couriersystemclient.datasource

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject

object JsonBodyHelper {
    fun getJsonBody(vararg params: Pair<String, Long>): JsonObject {
        val jsonObject = JSONObject()
        params.forEach {
            jsonObject.put(it.first, it.second)
        }
        val jsonParser = JsonParser()
        return jsonParser.parse(jsonObject.toString()) as JsonObject
    }
}