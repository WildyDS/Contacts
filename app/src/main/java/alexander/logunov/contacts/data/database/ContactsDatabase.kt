package alexander.logunov.contacts.data.database

import alexander.logunov.contacts.data.dao.ContactDao
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

// TODO: ContactsDatabase
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {

        @Volatile private var INSTANCE: ContactsDatabase? = null

        fun getInstance(context: Context): ContactsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                ContactsDatabase::class.java, "Sample.db")
                .build()
    }
}