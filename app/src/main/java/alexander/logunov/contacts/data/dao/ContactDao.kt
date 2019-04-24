package alexander.logunov.contacts.data.dao

import alexander.logunov.contacts.data.model.Contact
import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Flowable

// TODO: ContactDao
/**
 * Data Access Object for the contacts table.
 */
@Dao
interface ContactDao {
    /**
     * Get all contacts.
     * @return contact list.
     */
    @Query("SELECT * FROM Contacts")
    fun getContacts(id: String): Flowable<List<Contact>>
}