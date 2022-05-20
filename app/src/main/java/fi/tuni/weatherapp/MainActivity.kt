package fi.tuni.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
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
            cityName = editText.text.toString()
            fetchData() {
                this.runOnUiThread(Runnable{
                    ??(it)
                })
            }
        }
    }
    
    fun fetchData(result: (String?) -> Unit): Unit {
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

}

