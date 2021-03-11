package androidpro.com.morareach.apicalls

import android.R.attr.apiKey
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder

object ConnectionGeocoding {
    fun getCoordenadas(url: String): List<Double>? {
        val geocodingApiKey = ""

        val query: String = URLEncoder.encode(url, "utf-8")
        val allUrl =
            "https://maps.googleapis.com/maps/api/geocode/json?address=$query&key=$geocodingApiKey"

        return getCoordenadasFromJson(allUrl)
    }

    private fun getCoordenadasFromJson(url: String): List<Double>? {
        val obj = getJSON(url)

        try {
            val objLocation =
                obj?.getJSONArray("results")?.getJSONObject(0)?.getJSONObject("geometry")
                    ?.getJSONObject("location")
            val lat: String? = objLocation?.getString("lat")
            val lng: String? = objLocation?.getString("lng")

            if (lat == null || lng == null)
                Log.d("ConnectionGeocoding", "Caminho não encontrado!")


            Log.d(
                "ConnectionActivity",
                "As coordenadas encontradas ao criar a moradia foram: $lat, $lng"
            )
            return listOf(lat!!.toDouble(), lng!!.toDouble())

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    private fun getJSON(url: String): JSONObject? {
        val json = arrayOf(JSONObject())
        val thread = Thread {
            try {
                val urlConn: URLConnection
                val url = URL(url)
                urlConn = url.openConnection()
                val bufferedReader = BufferedReader(InputStreamReader(urlConn.getInputStream()))
                val stringBuffer = StringBuffer()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) stringBuffer.append(
                    line
                )
                json[0] = JSONObject(stringBuffer.toString())
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }
        thread.start()
        try {
            thread.join()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return json[0]
    }
}