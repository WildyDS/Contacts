package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.data.model.EducationPeriod
import alexander.logunov.contacts.data.model.Temperament
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class ContactListModel : ViewModel() {

    public val contacts: MutableLiveData<List<Contact>?> = MutableLiveData()

    fun getContacts(): LiveData<List<Contact>?> {
        return contacts
    }

    fun clearContacts() {
        contacts.postValue(null)
    }

    fun loadContacts() {
        // TODO: load contacts
        contacts.postValue(contactsList)
    }

    fun saveContacts() {
        // TODO: save contacts
    }

    fun filterByNameOrPhone(query: String)  {
        if (query.isEmpty()) {
            contacts.postValue(contactsList)
        } else {
            contacts.postValue(contactsList.filter {
                    contact  -> contact.name.contains(query) || contact.phone.contains(query)
            })
        }
    }

    init {
        for (i in 1..100) {
            contactsList.add(
                Contact(
                    "test-id $i",
                    "Имя $i",
                    "+7 (900) 000-0$i",
                    i.toFloat(),
                    "Biograrphy",
                    Temperament.Choleric,
                    EducationPeriod(Date(), Date())
                )
            )
        }
        loadContacts()
    }

    public companion object {
        val contactsList: ArrayList<Contact> = ArrayList()
    }
}
