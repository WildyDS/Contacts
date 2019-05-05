package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.model.Contact
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactListModel : ViewModel() {

    private val contacts: MutableLiveData<List<Contact>?> = MutableLiveData()

    fun getContacts(): LiveData<List<Contact>?> {
        return contacts
    }

    fun clearContacts() {
        contacts.postValue(null)
    }

    fun loadContacts() {
        // TODO: load contacts
        val list = ArrayList<Contact>()
        for (i in 1..100) {
            list.add(
                Contact(
                    "test-id $i",
                    "Имя $i",
                    "Телефон $i",
                    i.toFloat(),
                    null,
                    null,
                    null
                )
            )
        }
        contacts.postValue(list)
    }

    init {
        loadContacts()
    }
}
