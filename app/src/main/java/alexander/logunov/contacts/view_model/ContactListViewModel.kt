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

    val contacts: MutableLiveData<List<Contact>?> = MutableLiveData()

    val snackbarText: MutableLiveData<String> = MutableLiveData()

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
                    contactsList.addAll(it)
                    contacts.postValue(contactsList)
                    val count = it.count()
                    val last = it.last()
                    isLoading.postValue(false)
                    isRefreshing.postValue(false)
                    if (count > 1 && Date(last.createdAt + 1000 * 60).after(Date())) {
                        Log.d(TAG, "Contacts loaded from DB")
                    } else {
                        Log.d(TAG, "Loading from GitHub")
                        loadContacts()
                    }

                },
                { error ->
                    Log.e(TAG, "Unable to load contacts from DB", error)
                    snackbarText.postValue("Ошибка БД")
                }
            ))
    }

    fun loadContacts() {
        isLoading.postValue(true)
        val list: ArrayList<Contact> = ArrayList()
        disposable.add(Api.getInstance().getContacts(1)
            .flatMap {
                    page1 ->
                list.addAll(page1)
                Api.getInstance().getContacts(2)
            }
            .flatMap { page2 ->
                list.addAll(page2)
                Api.getInstance().getContacts(3)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
                {
                    list.addAll(it)
                    saveContactsToDB(list)
                    isLoading.postValue(false)
                    isRefreshing.postValue(false)
                    Log.d(TAG, "Contacts loaded from GitHub")

                },
                { error ->
                    Log.e(TAG, "Unable to load contacts from GitHub", error)
                    snackbarText.postValue("Ошибка сети")
                }
            )
        )
    }

    private val phoneRegex = "[^0-9]".toRegex()

    // TODO: может debounce?
    // TODO: переделать Mutable на Computable, а то фильтр страдает
    // TODO: починить поиск по имени
    fun filterByNameOrPhone(query: String?)  {
        if (query == null || query.isEmpty()) {
            contacts.postValue(contactsList)
        } else {
            contacts.postValue(contactsList.filter { contact  ->
                contact.name.toLowerCase().contains(query.toLowerCase()) ||
                        contact.phone.replace(phoneRegex, "").contains(query.replace(phoneRegex, ""))
            })
        }
    }

    val refreshListener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshContacts()
        // TODO: очистить строку поиска
    }

    init {
        snackbarText.observeForever{ snackbarText.postValue(null)}
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
