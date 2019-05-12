package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.model.Contact
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContactViewModelFactory(
    private val application: Application,
    private var contact: Contact
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactViewModel(application, contact) as T
    }
}