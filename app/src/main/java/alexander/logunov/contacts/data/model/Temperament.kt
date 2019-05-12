package alexander.logunov.contacts.data.model

import androidx.room.Entity

@Entity
enum class Temperament(val string: String) {
    Melancholic("melancholic"),
    Phlegmatic("phlegmatic"),
    Sanguine("sanguine"),
    Choleric("choleric")
}