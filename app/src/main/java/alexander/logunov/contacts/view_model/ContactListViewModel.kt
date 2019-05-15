package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.database.ContactsDatabase
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.network.Api
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private val disposable = CompositeDisposable()

    val searchQuery: MutableLiveData<String?> = MutableLiveData()

    val contacts: MutableLiveData<List<Contact>?> = MutableLiveData()

    val snackbarText: MutableLiveData<String> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    val refreshListener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshContacts()
        searchQuery.postValue(null)
    }

    fun getContacts(): LiveData<List<Contact>?> {
        return contacts
    }

    fun clearContacts() {
        contactsList.clear()
        contacts.postValue(contactsList)
    }

    fun refreshContacts() {
        isRefreshing.postValue(true)
        clearContacts()
        loadContacts(true)
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

    fun loadFromDB(allowGithub: Boolean = true) {
        isLoading.postValue(true)
        disposable.add(ContactsDatabase.getInstance().contactDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (allowGithub && it.isEmpty()) {
                        Log.d(TAG, "Loading from GitHub")
                        loadContacts()
                    } else {
                        contactsList.addAll(it)
                        contacts.postValue(contactsList)
                        if (allowGithub && Date(it.last().createdAt + 60000).before(Date())) {
                            loadContacts()
                        } else {
                            isLoading.postValue(false)
                            isRefreshing.postValue(false)
                        }
                    }
                },
                { error ->
                    Log.e(TAG, "Unable to load contacts from DB", error)
                    snackbarText.postValue("Ошибка БД: ${error.localizedMessage}")
                    isLoading.postValue(false)
                    isRefreshing.postValue(false)
                    if (allowGithub) {
                        loadContacts()
                    }
                }
            ))
    }

    fun loadContacts(allowDB: Boolean = false) {
        isLoading.postValue(true)
        val list: ArrayList<Contact> = ArrayList()
        disposable.add(
            Api.getInstance().getContacts(1)
                .flatMap { page1 ->
                    list.addAll(page1)
                    Api.getInstance().getContacts(2)
                }
                .flatMap { page2 ->
                    list.addAll(page2)
                    Api.getInstance().getContacts(3)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        list.addAll(it)
                        saveContactsToDB(list)
                        isLoading.postValue(false)
                        isRefreshing.postValue(false)
                        Log.d(TAG, "Contacts loaded from GitHub")

                    },
                    { error ->
                        Log.e(TAG, "Unable to load contacts from GitHub", error)
                        snackbarText.postValue("Ошибка сети: ${error.localizedMessage}")
                        if (allowDB) {
                            loadFromDB(false)
                        } else {
                            isLoading.postValue(false)
                            isRefreshing.postValue(false)
                        }
                    }
                )
        )
    }

    // TODO: может debounce?
    fun filterByNameOrPhone(query: String?) {
        if (query == null || query.isEmpty()) {
            contacts.postValue(contactsList)
        } else {
            contacts.postValue(contactsList.filter { contact  ->
                contact.name.toLowerCase().contains(query.toLowerCase()) ||
                        contact.phone.number.toString().contains(query)
            })
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    init {
        searchQuery.observeForever { q -> filterByNameOrPhone(q) }
        loadFromDB()
    }

    companion object {
        var contactsList: ArrayList<Contact> = ArrayList()
        const val TAG = "ContactListViewModel"
    }
}
