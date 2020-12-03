package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.database.ContactsDatabase
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.network.Api
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class ContactListViewModel(application: Application) : DisposableViewModel(application) {
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

    private fun refreshContacts() {
        isRefreshing.postValue(true)
        contactsList.clear()
        contacts.postValue(contactsList)
        loadContacts()
    }

    private fun saveContactsToDB(contacts: List<Contact>) {
        ContactsDatabase.getInstance().contactDao().insertAll(contacts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { Log.d(TAG, "Contacts saved to DB") },
                        { error -> Log.e(TAG, "Unable to save contacts", error) }
                )
                .run { addDisposable(this) }
    }

    private fun loadFromDB() {
        addDisposable(ContactsDatabase.getInstance().contactDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    isLoading.postValue(true)
                }
                .doOnEach {
                    isLoading.postValue(false)
                    isRefreshing.postValue(false)
                }
            .subscribe(
                {
                    contactsList.addAll(it)
                    contacts.postValue(contactsList)
                    if ((it.isEmpty()) || Date(it.last().createdAt + 60000).before(Date())) {
                        loadContacts()
                    }
                },
                { error ->
                    Log.e(TAG, "Unable to load contacts from DB", error)
                    snackbarText.postValue("Ошибка БД: ${error.localizedMessage}")
                    loadContacts()
                }
            ))
    }

    private fun loadContacts() {
        Log.d(TAG, "Loading from GitHub")
        Api.getInstance().getContacts(1)
                .flatMap { Api.getInstance().getContacts(2) }
                .flatMap { Api.getInstance().getContacts(3) }
                .doOnSubscribe {
                    isLoading.postValue(true)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { pages ->
                            val contactList: List<Contact> = pages.map { contactDTO -> Contact(contactDTO) }
                            saveContactsToDB(contactList)
                            Log.d(TAG, "Contacts loaded from GitHub")
                        },
                        { error ->
                            Log.e(TAG, "Unable to load contacts from GitHub", error)
                            snackbarText.postValue("Ошибка сети: ${error.localizedMessage}")
                        }
                ).run { addDisposable(this) }
    }

    private fun filterByNameOrPhone(query: String?) {
        if (query == null || query.isEmpty()) {
            contacts.postValue(contactsList)
        } else {
            contacts.postValue(contactsList.filter { contact  ->
                contact.name.toLowerCase().contains(query.toLowerCase()) ||
                        contact.phone.number.toString().contains(query)
            })
        }
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
