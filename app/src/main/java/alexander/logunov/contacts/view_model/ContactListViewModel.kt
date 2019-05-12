package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.database.ContactsDatabase
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.network.Api
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ContactListViewModel : ViewModel() {
    private val disposable = CompositeDisposable()

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
            Log.w(TAG, t)
            isLoading.postValue(false)
            isRefreshing.postValue(false)
        }

        override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
            val body = response.body()
            if (body !== null) {
                contactsList.addAll(body)
                saveContactsToDB(body)
                contacts.postValue(body)
            }
            isLoading.postValue(false)
            isRefreshing.postValue(false)
        }
    }

    fun saveContactsToDB(contacts: List<Contact>) {
        disposable.add(
            ContactsDatabase.getInstance().contactDao().insertAll(contacts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d(TAG, "Contacts saved to DB") },
                    { error -> Log.e(TAG, "Unable to save contacts", error) }
                )
        )
    }

    fun loadFromDB() {
        isLoading.postValue(true)
        disposable.add(ContactsDatabase.getInstance().contactDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.count() > 1 && Date(it.last().createdAt + 1000 * 60).after(Date())) {
                        contactsList.addAll(it)
                        contacts.postValue(contactsList)
                        isLoading.postValue(false)
                        isRefreshing.postValue(false)
                        Log.d(TAG, "Contacts loaded from DB")
                    } else {
                        Log.d(TAG, "Loading from GitHub")
                        loadContacts()
                    }

                },
                { error ->
                    Log.e(TAG, "Unable to load contacts from DB", error)
                    loadContacts()
                }
            ))
    }

    fun loadContacts() {
        isLoading.postValue(true)
        Api.getInstance().getContacts(1, handleContactsLoad)
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
        loadFromDB()
    }

    companion object {
        var contactsList: ArrayList<Contact> = ArrayList()
        const val TAG = "ContactListViewModel"
    }
}
