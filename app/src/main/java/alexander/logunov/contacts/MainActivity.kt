package alexander.logunov.contacts

import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.ActivityMainBinding
import alexander.logunov.contacts.view.ContactsFragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

class MainActivity : AppCompatActivity(), ContactsFragment.OnListFragmentInteractionListener {
    val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityMainBinding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ContactsFragment.newInstance())
                .commitNow()
        }
    }

    override fun onListFragmentInteraction(contact: Contact?) {
        Log.d(TAG, contact?.toString())
    }

}
