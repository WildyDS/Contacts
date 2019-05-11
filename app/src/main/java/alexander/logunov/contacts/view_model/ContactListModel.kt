package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.data.model.EducationPeriod
import alexander.logunov.contacts.data.model.Temperament
import alexander.logunov.contacts.network.Api
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ContactListModel : ViewModel() {

    val contacts: MutableLiveData<List<Contact>?> = MutableLiveData()

    fun getContacts(): LiveData<List<Contact>?> {
        return contacts
    }

    fun clearContacts() {
        contacts.postValue(null)
    }

    private val onContacts = object : Callback<List<Contact>> {
        override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
            Log.w(Api.TAG, t)
        }

        override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
            Log.d(Api.TAG, response.body()?.toString())
            val body = response.body()
            if (body !== null) {
                contactsList.addAll(body)
            }
            contacts.postValue(contactsList)
        }
    }

    fun loadContacts() {
        contacts.postValue(contactsList)
        Api.getInstance().getContacts(1, onContacts)
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
        loadContacts()
    }

    companion object {
        var contactsList: ArrayList<Contact> = ArrayList()
    }
}
