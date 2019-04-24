package alexander.logunov.contacts.view

import alexander.logunov.contacts.adapter.ContactsRecyclerAdapter
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.FragmentContactsListBinding
import alexander.logunov.contacts.view_model.ContactListModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ContactsFragment.OnListFragmentInteractionListener] interface.
 */
class ContactsFragment : androidx.fragment.app.Fragment() {
    private lateinit var viewModel: ContactListModel

    private lateinit var list: ArrayList<Contact>

    private lateinit var contactsAdapter: ContactsRecyclerAdapter

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ContactListModel::class.java)
        list = ArrayList()
        list.add(Contact(
            "test-id 0",
            "Имя 0",
            "Телефон 0",
            0.toFloat(),
            null,
            null,
            null
        ))
        contactsAdapter = ContactsRecyclerAdapter(
            list,
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
        val view = FragmentContactsListBinding.inflate(inflater, container, false).root
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = contactsAdapter
            }
        }
        return view
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
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(contact: Contact?)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactsFragment()
    }
}
