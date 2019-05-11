package alexander.logunov.contacts.view_model

import alexander.logunov.contacts.data.model.Contact
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel


class ContactViewModel (application: Application, val contact: Contact) : AndroidViewModel(application) {

    fun dial() {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        dialIntent.data = Uri.parse("tel:" + contact.phone)
        startActivity(getApplication(), dialIntent, null)
    }

    val handlePressPhone = View.OnClickListener {
        dial()
    }

}
