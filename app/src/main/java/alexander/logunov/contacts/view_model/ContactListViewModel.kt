package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.network.Api
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactListViewModel : ViewModel() {
    val contacts: MutableLiveData<List<Contact>?> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    fun getContacts(): LiveData<List<Contact>?> {
        return contacts
    }

    fun clearContacts() {
        contacts.postValue(null)
    }

    fun refreshContacts() {
        isRefreshing.postValue(true)
        clearContacts()
        loadContacts()
    }

    private val handleContactsLoad = object : Callback<List<Contact>> {
        override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
            Log.w(Api.TAG, t)
            isLoading.postValue(false)
            isRefreshing.postValue(false)
        }

        override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
            val body = response.body()
            if (body !== null) {
                contactsList.addAll(body)
            }
            contacts.postValue(contactsList)
            isLoading.postValue(false)
            isRefreshing.postValue(false)
        }
    }

    fun loadContacts() {
        isLoading.postValue(true)
        Api.getInstance().getContacts(1, handleContactsLoad)
        // Api.getInstance().getContacts(2, handleContactsLoad)
        // Api.getInstance().getContacts(3, handleContactsLoad)
    }

    fun saveContacts() {
        // TODO: save contacts
    }

    fun filterByNameOrPhone(query: String)  {
        if (query.isEmpty()) {
            contacts.postValue(contactsList)
        } else {
            // TODO: регистронезависимость, отфильтровать скобки и другие лишние символы
            contacts.postValue(contactsList.filter {
                    contact  -> contact.name.contains(query) || contact.phone.contains(query)
            })
        }
    }

    val refreshListener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshContacts()
        // TODO: очистить строку поиска
    }

    init {
        isLoading.postValue(false)
        isRefreshing.postValue(false)
        contacts.postValue(contactsList)
        loadContacts()
    }

    companion object {
        var contactsList: ArrayList<Contact> = ArrayList()
    }
}
