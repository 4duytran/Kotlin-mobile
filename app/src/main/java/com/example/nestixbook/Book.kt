package com.example.nestixbook

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Book(
    val id: Int?,
    val title: String?,
    @SerializedName("ISBN") // Name from JSON
    val isbn: String?,
    @SerializedName("annee") // Name from JSON
    val year: String?,
    val genre: String?,
    @SerializedName("Description") // Name from JSON
    val desc:String?,
    val author:String?,
    val url:String?
):Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(isbn)
        parcel.writeString(year)
        parcel.writeString(genre)
        parcel.writeString(desc)
        parcel.writeString(author)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }

}
