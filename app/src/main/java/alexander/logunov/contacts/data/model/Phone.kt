package alexander.logunov.contacts.data.model

data class Phone(val text: String) {
    val number = (text.replace(phoneRegex, "")).toLong()

    constructor(number: Long) : this(number.toString().replace(inputFormat, outputFormat))

    override fun toString(): String {
        return text
    }

    companion object {
        val phoneRegex = "[^0-9]".toRegex()
        val outputFormat = "+$1 ($3) $3-$4"
        val inputFormat = "(\\d)(\\d{3})(\\d{3})(\\d{4})".toRegex()
    }
}