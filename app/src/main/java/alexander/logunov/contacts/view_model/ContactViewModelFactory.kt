package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.model.Contact
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContactViewModelFactory(
    private val application: Application,
    private val contact: Contact
) : ViewModelProvider.Factory {

    companion object {
        private var selfInstance: ContactViewModelFactory? = null

        fun getInstance(application: Application, contact: Contact): ContactViewModelFactory {
            if (selfInstance !== null) {
                return selfInstance as ContactViewModelFactory
            } else {
                selfInstance = ContactViewModelFactory(application, contact)
                return selfInstance as ContactViewModelFactory
            }
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactViewModel(application, contact) as T
    }
}