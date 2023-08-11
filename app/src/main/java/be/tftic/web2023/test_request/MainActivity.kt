package be.tftic.web2023.test_request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import be.tftic.web2023.test_request.databinding.ActivityMainBinding
import be.tftic.web2023.test_request.models.Cat
import be.tftic.web2023.test_request.tools.CatRequestTool

class MainActivity : AppCompatActivity(), CatRequestTool.OnResponseListener {

    private  lateinit var binding : ActivityMainBinding
    private val catRequestTool = CatRequestTool(this, lifecycleScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMainRequest.setOnClickListener { sendTestRequest() }
    }

    private fun sendTestRequest() {
        catRequestTool.searchByBreed("char", this)
    }

    override fun onRequestResponseSuccess(result: List<Cat>) {
        binding.tvMainResult.text = result.toString()
    }

    override fun onRequestResponseError(message: String?) {
        binding.tvMainResult.text = message
    }
}