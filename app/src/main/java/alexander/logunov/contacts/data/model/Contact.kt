package alexander.logunov.contacts.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "Contacts")
data class Contact (
    @PrimaryKey var id: String,
    @ColumnInfo(name = "name") var name: String,
    @Ignore @Json(name = "phone") var phoneString: String,
    @ColumnInfo(name = "height") var height: Float,
    @ColumnInfo(name = "biography") var biography: String,
    @ColumnInfo(name = "temperament") var temperament: String,
    @Ignore var educationPeriod: EducationPeriod
) {

    @ColumnInfo(name = "education_period_start") var educationPeriodStart: Long = educationPeriod.start.time
    @ColumnInfo(name = "education_period_end") var educationPeriodEnd: Long = educationPeriod.end.time
    @ColumnInfo(name =  "created_at") var createdAt: Long = Date().time
    @Transient @ColumnInfo(name = "phone_number") var phone: Phone = Phone(phoneString)

    constructor(
        id: String,
        name: String,
        phone: Phone,
        height: Float,
        biography: String,
        temperament: String,
        educationPeriodStart: Long,
        educationPeriodEnd: Long,
        createdAt: Long
    ) : this(
        id,
        name,
        phone.text,
        height,
        biography,
        temperament,
        EducationPeriod(Date(educationPeriodStart), Date(educationPeriodEnd))
    ) {
        this.createdAt = createdAt
        this.phone = phone
    }

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
