package be.tftic.web2023.test_request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import be.tftic.web2023.test_request.databinding.ActivityMainBinding
import be.tftic.web2023.test_request.models.Cat
import be.tftic.web2023.test_request.tools.CatRequestTool

class MainActivity : AppCompatActivity(), CatRequestTool.OnResponseListener<List<Cat>> {

    private  lateinit var binding : ActivityMainBinding
    private val catRequestTool = CatRequestTool(this, lifecycleScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMainRequest.setOnClickListener {
            sendListRequest()
            sendDetailRequest()
        }
    }

    private fun sendListRequest() {
        catRequestTool.searchByBreed("char", this)
    }

    override fun <T> onRequestResponseSuccess(result: T) {
        binding.tvMainResult.text = result.toString()
    }

    override fun onRequestResponseError(message: String?) {
        binding.tvMainResult.text = message
    }

    private fun sendDetailRequest() {
        catRequestTool.searchById("Pqcy8pOZG", object : CatRequestTool.OnResponseListener<Cat> {
            override fun <T> onRequestResponseSuccess(result: T) {
                Log.d("REQUEST_DEMO", result.toString())
            }

            override fun onRequestResponseError(message: String?) {
                Log.d("REQUEST_DEMO", message ?: "Unknown error")
            }
        })
    }
}