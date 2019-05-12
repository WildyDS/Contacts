package alexander.logunov.contacts.data.database

import alexander.logunov.contacts.data.dao.ContactDao
import alexander.logunov.contacts.data.model.Contact
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version = 1)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {

        @Volatile private var INSTANCE: ContactsDatabase? = null

        fun initialize(context: Context): ContactsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        fun getInstance(): ContactsDatabase {
            if (INSTANCE == null) {
                throw RuntimeException("Contacts database is not initialized.")
            }
            return INSTANCE as ContactsDatabase
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                ContactsDatabase::class.java, "Contacts.db")
                .build()
    }
}