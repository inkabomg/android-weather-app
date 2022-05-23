package fi.tuni.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var editText: EditText
    lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        editText = findViewById(R.id.editText)
        button.setOnClickListener {
            /* Capitalizing the first letter automatically */
            cityName = editText.text.toString().replaceFirstChar(Char::titlecase)
            fetchData() {
                this.runOnUiThread( Runnable {
                    onPostExecute(it)
                    editText.setText("")
                })
            }
        }
    }

    fun fetchData(result: (String?) -> Unit) {
        val apiKey = "e60a2984bb06e80c4f8e034c049ece51"
        thread {
            var response : String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$cityName&units=metric&appid=$apiKey").readText(
                    Charsets.UTF_8
                )
            } catch (e: Exception) {
                println(e)
                response = null
            }
            result(response)
        }
    }

    @SuppressLint("SetTextI18n")
    fun onPostExecute(result: String?) {
        try {
            val jsonObj = JSONObject(result!!)
            val main = jsonObj.getJSONObject("main")
            val wind = jsonObj.getJSONObject("wind")
            val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
            val sys = jsonObj.getJSONObject("sys")

            val windSpeed = wind.getString("speed") + " m/s"
            val desc = weather.getString("description")
            val temp = main.getString("temp") + " °C"

            val sunrise:Long = sys.getLong("sunrise")
            val sunset:Long = sys.getLong("sunset")

            //val icon = weather.getString("icon")
            //val iconUrl = "http://openweathermap.org/img/w/$icon.png"

            findViewById<TextView>(R.id.loc).text = cityName
            findViewById<TextView>(R.id.status).text = desc.replaceFirstChar(Char::titlecase)
            findViewById<TextView>(R.id.temp).text = temp
            findViewById<TextView>(R.id.wind).text = windSpeed
            findViewById<TextView>(R.id.sunrise).text = "Sunrise: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
            findViewById<TextView>(R.id.sunset).text = "Sunset: " + SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))

        } catch (e: Exception) {
            println(e)
            Toast.makeText(applicationContext,"Incorrect city name, try again", Toast.LENGTH_LONG).show()
        }
    }

}

