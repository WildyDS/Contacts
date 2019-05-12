package alexander.logunov.contacts.data.dao

import alexander.logunov.contacts.data.model.Contact
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ContactDao {
    @Query("SELECT * FROM Contacts")
    fun getAll(): Flowable<List<Contact>>

    @Insert(onConflict = REPLACE)
    fun insert(contact: Contact)

    @Insert(onConflict = REPLACE)
    fun insertAll(contacts: List<Contact>): Completable

    @Query("DELETE from Contacts")
    fun deleteAll()
}