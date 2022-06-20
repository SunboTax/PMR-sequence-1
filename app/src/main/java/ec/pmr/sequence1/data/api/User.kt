package ec.pmr.sequence1.data.api

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("success")
    val isLogin : Boolean,
    @SerializedName("hash")
    val token : String
)
