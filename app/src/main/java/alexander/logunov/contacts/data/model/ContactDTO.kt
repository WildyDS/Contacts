package alexander.logunov.contacts.data.model

data class ContactDTO (
    var id: String,
    var name: String,
    var phone: String,
    var height: Float,
    var biography: String,
    var temperament: String,
    var educationPeriod: EducationPeriod
)
