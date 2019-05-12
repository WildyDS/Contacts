package alexander.logunov.contacts

import alexander.logunov.contacts.data.database.ContactsDatabase
import android.app.Application

const val TAG: String = "ContactsApplication"

class ContactsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ContactsDatabase.initialize(this)
    }
}