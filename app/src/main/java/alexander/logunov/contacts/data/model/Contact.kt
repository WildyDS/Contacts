package alexander.logunov.contacts.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Contacts")
data class Contact (
        @PrimaryKey var id: String,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "phone_number") var phone: Phone,
        @ColumnInfo(name = "height") var height: Float,
        @ColumnInfo(name = "biography") var biography: String,
        @ColumnInfo(name = "temperament") var temperament: Temperament,
        @ColumnInfo(name = "education_period_start") var educationPeriodStart: Long,
        @ColumnInfo(name = "education_period_end") var educationPeriodEnd: Long,
        @ColumnInfo(name = "created_at") var createdAt: Long = Date().time
) : Parcelable {

    @Ignore
    var educationPeriod: EducationPeriod = EducationPeriod(Date(educationPeriodStart), Date(educationPeriodEnd))

    // TODO: ???
    constructor(parcel: Parcel) : this(
            id = parcel.readString(),
            name = parcel.readString(),
            phone = Phone(parcel.readLong()),
            height = parcel.readFloat(),
            biography = parcel.readString(),
            temperament = Temperament.fromString(parcel.readString()),
            educationPeriodStart = parcel.readLong(),
            educationPeriodEnd = parcel.readLong(),
            createdAt = parcel.readLong()
    )

    constructor(contactDTO: ContactDTO): this(
            id = contactDTO.id,
            name = contactDTO.name,
            phone = Phone(contactDTO.phone),
            height = contactDTO.height,
            biography = contactDTO.biography,
            temperament = Temperament.fromString(contactDTO.temperament),
            educationPeriodStart = contactDTO.educationPeriod.start.time,
            educationPeriodEnd = contactDTO.educationPeriod.end.time
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeLong(phone.number)
        parcel.writeFloat(height)
        parcel.writeString(biography)
        parcel.writeString(temperament.toString())
        parcel.writeLong(educationPeriodStart)
        parcel.writeLong(educationPeriodEnd)
        parcel.writeLong(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR = object : Parcelable.Creator<Contact> {
            override fun createFromParcel(parcel: Parcel): Contact {
                return Contact(parcel)
            }

            override fun newArray(size: Int): Array<Contact?> {
                return arrayOfNulls(size)
            }
        }
    }
}
