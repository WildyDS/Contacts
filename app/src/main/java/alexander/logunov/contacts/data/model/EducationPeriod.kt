package alexander.logunov.contacts.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class EducationPeriod (
    @ColumnInfo(name = "start") val start: Date,
    @ColumnInfo(name = "end") val end: Date
) {
    override fun toString(): String {
        return ("${SimpleDateFormat.getDateInstance().format(start)} - ${SimpleDateFormat.getDateInstance().format(end)}")
    }
}
