package ru.samtakoy.core.database.room.entities.other

import androidx.room.ColumnInfo
import ru.samtakoy.core.database.room.entities.CardEntity

class CardIds(
        @ColumnInfo(name = CardEntity._id) var id: Long,
        @ColumnInfo(name = CardEntity._qpack_id) var qPackId: Long
) {


}