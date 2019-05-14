package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.database.ContactsDatabase
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.network.Api
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

// TODO: почистить все лишние вызовы снекбара
class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private val disposable = CompositeDisposable()

    private val flowableContacts = ContactsDatabase.getInstance().contactDao().getAll()

    val allContacts: LiveData<List<Contact>> = LiveDataReactiveStreams.fromPublisher(flowableContacts)

    val contacts: MutableLiveData<Contact> = MediatorLiveData()

    val snackbarText: MutableLiveData<String> = MutableLiveData()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    fun refreshContacts() {
        isRefreshing.postValue(true)
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

    fun loadContacts() {
        isLoading.postValue(true)
        val list = ArrayList<Contact>()
        disposable.add(
            Api.getInstance().getContacts(1)
                .flatMap {
                    page1 -> list.addAll(page1)
                    Api.getInstance().getContacts(2)
                }
                .flatMap {
                    page2 -> list.addAll(page2)
                    Api.getInstance().getContacts(3)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        list.addAll(it)
                        saveContactsToDB(list)
                        val count = list.count()
                        isLoading.postValue(false)
                        isRefreshing.postValue(false)
                        Log.d(TAG, "Contacts loaded from GitHub")

                    },
                    { error -> Log.e(TAG, "Unable to load contacts from GitHub", error) }
                )
        )

    }

    // TODO: fix filterByNameOrPhone
    fun filterByNameOrPhone(query: String)  {
        snackbarText.postValue("TODO: сделать фильтрацию")
//        if (query.isEmpty()) {
//            contacts.postValue(contactsList)
//        } else {
//            // TODO: регистронезависимость, отфильтровать скобки и другие лишние символы
//            contacts.postValue(contactsList.filter {
//                    contact  -> contact.name.contains(query) || contact.phone.contains(query)
//            })
//        }
    }

    val refreshListener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        refreshContacts()
        // TODO: очистить строку поиска
    }

    private val obs: Observer<List<Contact>?> =
        Observer { list ->
            if (
                list == null ||
                list.isEmpty() ||
                !Date(list.last().createdAt + 1000 * 60).after(Date())
            ) {
                loadContacts()
                removeObs()
            }
        }

    private fun removeObs() {
        allContacts.removeObserver(obs)
    }

    init {
        snackbarText.postValue(null)
        isLoading.postValue(false)
        isRefreshing.postValue(false)
        allContacts.observeForever(obs)
    }

    companion object {
        const val TAG = "ContactListViewModel"
    }
}
