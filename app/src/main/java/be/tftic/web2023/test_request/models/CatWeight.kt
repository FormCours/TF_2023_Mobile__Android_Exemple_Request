package be.tftic.web2023.test_request.models

import com.google.gson.annotations.SerializedName


data class CatWeight (

    @SerializedName("imperial" ) var imperial : String? = null,
    @SerializedName("metric"   ) var metric   : String? = null

)