package be.tftic.web2023.test_request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.tftic.web2023.test_request.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private  lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMainRequest.setOnClickListener { sendTestRequest() }
    }

    private fun sendTestRequest() {
        binding.tvMainResult.text = "Test..."
    }
}