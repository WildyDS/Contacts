package alexander.logunov.contacts.data.model

import androidx.room.Entity

@Entity
sealed class Temperament {
    companion object {
        const val MELANCHOLIC = "melancholic"
        const val PHLEGMATIC = "phlegmatic"
        const val SANGUINE = "sanguine"
        const val CHOLERIC = "choleric"

        fun fromString(temperament: String): Temperament {
            when (temperament) {
                MELANCHOLIC -> return Melancholic
                PHLEGMATIC -> return Phlegmatic
                SANGUINE -> return Sanguine
                CHOLERIC -> return Choleric
            }
            return Choleric
        }
    }
}

object Melancholic : Temperament() {
    override fun toString(): String {
        return MELANCHOLIC
    }
}

object Phlegmatic : Temperament() {
    override fun toString(): String {
        return PHLEGMATIC
    }
}

object Sanguine : Temperament() {
    override fun toString(): String {
        return SANGUINE
    }
}

object Choleric : Temperament() {
    override fun toString(): String {
        return CHOLERIC
    }
}
