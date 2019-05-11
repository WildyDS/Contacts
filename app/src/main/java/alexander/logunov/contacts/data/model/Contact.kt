package alexander.logunov.contacts.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
enum class Temperament {
    melancholic, phlegmatic, sanguine, choleric
}

@Entity
data class EducationPeriod (
    @ColumnInfo(name = "start") val start: Date,
    @ColumnInfo(name = "end") val end: Date
) {
    override fun toString(): String {
        return ("${SimpleDateFormat.getDateInstance().format(start)} - ${SimpleDateFormat.getDateInstance().format(end)}")
    }
}

@Entity
data class Contact (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "height") val height: Float,
    @ColumnInfo(name = "biography") val biography: String,
    @ColumnInfo(name = "temperament") val temperament: Temperament,
    @ColumnInfo(name = "education_period") val educationPeriod: EducationPeriod
) {
    override fun toString(): String {
        return "{\n" +
                "name: $name,\n" +
                "phone:  $phone,\n" +
                "height: $height\n" +
                "height: $biography\n" +
                "temperament: $temperament\n" +
                "educationPeriod: $educationPeriod\n" +
                "}"
    }
}

/*
id (string) — ID контакта
name (string) — Имя человека
height (float) — Рост человека
biography (string) — Биография человека
temperament (enum) — Темперамент человека (melancholic, phlegmatic, sanguine, choleric)
educationPeriod (object) — Период прохождения учебы. Состоит из дат start и end.
 */