package alexander.logunov.contacts.view

import alexander.logunov.contacts.adapter.ContactsRecyclerAdapter
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.FragmentContactListBinding
import alexander.logunov.contacts.view_model.ContactListViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * A fragment representing a list of Contacts.
 * Activities containing this fragment MUST implement the
 * [ContactListFragment.OnListFragmentInteractionListener] interface.
 */
class ContactListFragment : androidx.fragment.app.Fragment() {
    private lateinit var viewModel: ContactListViewModel

    private lateinit var contactsAdapter: ContactsRecyclerAdapter

    private var binding: FragmentContactListBinding? = null

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ContactListViewModel::class.java)
        contactsAdapter = ContactsRecyclerAdapter(
            ArrayList(),
            listener
        )
        viewModel.getContacts().observe(
            this,
            contactsAdapter.contactsChangeHandler
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactListBinding.inflate(
            inflater,
            container,
            false
        )
        with(binding!!.recycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = contactsAdapter
        }
        binding!!.lifecycleOwner = this
        binding!!.viewModel = viewModel
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        binding = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(contact: Contact)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactListFragment()
        const val TAG: String = "ContactListFragment"
    }
}
