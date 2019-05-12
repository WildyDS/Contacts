package alexander.logunov.contacts.view

import alexander.logunov.contacts.R
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), ContactListFragment.OnListFragmentInteractionListener {
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
                .replace(activityMainBinding.container.id, ContactListFragment.newInstance())
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
