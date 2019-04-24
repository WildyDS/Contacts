package alexander.logunov.contacts.view

import alexander.logunov.contacts.R
import alexander.logunov.contacts.view_model.ContactListModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ContactListFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = ContactListFragment()
    }

    private lateinit var viewModel: ContactListModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_contacts_list, container, false)
    }



}
