package org.cookandroid.hiauction

import com.google.gson.annotations.SerializedName

data class ResultLogin(
    @SerializedName("id")
    val id: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("address")
    val address: List<String>,

    @SerializedName("tel")
    val tel: String

)

data class SignUpBody(
    val id : String,
    val password : String,
    val email : String,
    val tel : String,
    val name : String,
    val description : String,
    val address : ArrayList<Int>
)