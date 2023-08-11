package be.tftic.web2023.test_request.models

import com.google.gson.annotations.SerializedName

data class Cat (

        @SerializedName("breeds" ) var breeds : ArrayList<CatBreed> = arrayListOf(),
        @SerializedName("id"     ) var id     : String?             = null,
        @SerializedName("url"    ) var url    : String?             = null,
        @SerializedName("width"  ) var width  : Int?                = null,
        @SerializedName("height" ) var height : Int?                = null

)