package alexander.logunov.contacts

import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.ActivityMainBinding
import alexander.logunov.contacts.view.ContactFragment
import alexander.logunov.contacts.view.ContactsFragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), ContactsFragment.OnListFragmentInteractionListener {
    companion object {
        const val TAG: String = "MainActivity"
    }

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

    override fun onListFragmentInteraction(contact: Contact) {
        Log.d(TAG, contact.toString())
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ContactFragment.newInstance(contact))
            .addToBackStack("ContactFragment")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        with (supportFragmentManager)  {
            if (backStackEntryCount > 0) {
                popBackStack()
            }  else {
                super.onBackPressed()
            }
        }
    }
}
