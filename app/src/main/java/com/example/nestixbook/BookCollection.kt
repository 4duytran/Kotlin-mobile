package com.example.nestixbook

import android.os.Parcel
import android.os.Parcelable

data class BookCollection(
    val id:Int?,
    val name:String?
) : Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookCollection> {
        override fun createFromParcel(parcel: Parcel): BookCollection {
            return BookCollection(parcel)
        }

        override fun newArray(size: Int): Array<BookCollection?> {
            return arrayOfNulls(size)
        }
    }
}