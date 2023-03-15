package dev.x001.foodies.model.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dishes-table")
data class Dish(
    val image: String,
    val imageSource: String,
    val dish: String,
    val type: String,
    val category: String,
    val ingredients: String,
    @ColumnInfo(name ="cooking_time")
    val cookingTime: String,
    @ColumnInfo(name = "instructions")
    val directionToCook: String,
    @ColumnInfo(name = "favorite_dish")
    var favoriteDish: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(imageSource)
        parcel.writeString(dish)
        parcel.writeString(type)
        parcel.writeString(category)
        parcel.writeString(ingredients)
        parcel.writeString(cookingTime)
        parcel.writeString(directionToCook)
        parcel.writeByte(if (favoriteDish) 1 else 0)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dish> {
        override fun createFromParcel(parcel: Parcel): Dish {
            return Dish(parcel)
        }

        override fun newArray(size: Int): Array<Dish?> {
            return arrayOfNulls(size)
        }
    }
}