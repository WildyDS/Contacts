package alexander.logunov.contacts.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter : JsonAdapter<Date>() {
    private var mDateFormat: SimpleDateFormat

    init {
        val timeZone: TimeZone = TimeZone.getTimeZone("UTC")
        mDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        mDateFormat.timeZone = timeZone
    }

    override fun fromJson(reader: JsonReader): Date? {
        return mDateFormat.parse(reader.nextString())
    }

    override fun toJson(writer: JsonWriter, value: Date?) {
        // TODO: ?
        writer.value(mDateFormat.format(value))
    }
}