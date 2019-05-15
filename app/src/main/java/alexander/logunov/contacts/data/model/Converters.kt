package alexander.logunov.contacts.data.model

import androidx.room.TypeConverter

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromPhone(value: Phone): Long {
            return value.number
        }

        @TypeConverter
        @JvmStatic
        fun toPhone(value: Long): Phone {
            return Phone(value)
        }

        @TypeConverter
        @JvmStatic
        fun fromTemperament(value: Temperament): String {
            return value.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toTemperament(value: String): Temperament {
            return Temperament.valueOf(value)
        }
    }
}