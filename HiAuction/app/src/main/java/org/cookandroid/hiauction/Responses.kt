package org.cookandroid.hiauction

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class ResultLogin(
    @SerializedName("id")
    val id: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("address")
    val address: ArrayList<String>?,

    @SerializedName("tel")
<<<<<<< HEAD
    val tel: String?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeStringList(address)
        parcel.writeString(tel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultLogin> {
        override fun createFromParcel(parcel: Parcel): ResultLogin {
            return ResultLogin(parcel)
        }

        override fun newArray(size: Int): Array<ResultLogin?> {
            return arrayOfNulls(size)
        }
    }
}
=======
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
>>>>>>> 6dfc75cd661097b1a5d9bbc086ac13677183bd25
