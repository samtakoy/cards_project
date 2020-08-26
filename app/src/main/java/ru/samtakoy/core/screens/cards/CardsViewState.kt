package ru.samtakoy.core.screens.cards

import android.os.Parcel
import android.os.Parcelable
import java.util.*

//@Parcelize
class CardsViewState(
        var isOnAnswer: Boolean = false,
        var viewedCardIds: Deque<Long> = ArrayDeque<Long>()
) : Parcelable {

    companion object {
        @JvmStatic
        val CREATOR = object : Parcelable.Creator<CardsViewState> {
            override fun createFromParcel(source: Parcel?): CardsViewState = CardsViewState(source)
            override fun newArray(size: Int) = arrayOfNulls<CardsViewState>(size)
        }
    }

    constructor(parcel: Parcel?) : this() {

        viewedCardIds = ArrayDeque<Long>();
        isOnAnswer = false

        if (parcel != null) {

            val size = parcel.readInt()
            val arrLongs = LongArray(size)
            parcel.readLongArray(arrLongs)
            arrLongs.toCollection(viewedCardIds)

            isOnAnswer = (parcel.readInt() == 1)
        }
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {

        dest.writeInt(viewedCardIds.size)
        dest.writeLongArray(viewedCardIds.toLongArray())
        dest.writeInt(if (isOnAnswer) 1 else 0)
    }
}