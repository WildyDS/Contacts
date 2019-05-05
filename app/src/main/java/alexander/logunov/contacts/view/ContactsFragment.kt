package alexander.logunov.contacts.view

import alexander.logunov.contacts.adapter.ContactsRecyclerAdapter
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.FragmentContactsListBinding
import alexander.logunov.contacts.view_model.ContactListModel
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ContactsFragment.OnListFragmentInteractionListener] interface.
 */
class ContactsFragment : androidx.fragment.app.Fragment() {
    private lateinit var viewModel: ContactListModel

    private lateinit var contactsAdapter: ContactsRecyclerAdapter

    private var binding: FragmentContactsListBinding? = null

    private val refreshListener: SwipeRefreshLayout.OnRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        viewModel.clearContacts()
        Timer().schedule(1000) {
            viewModel.loadContacts()
            // TODO: в биндинг
            binding?.swipeRefresh?.isRefreshing = false
        }
    }

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ContactListModel::class.java)
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
        binding = FragmentContactsListBinding.inflate(
            inflater,
            container,
            false
        )
        binding!!.swipeRefresh.setOnRefreshListener(refreshListener)
        with(binding!!.recycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = contactsAdapter
        }
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
        fun onListFragmentInteraction(contact: Contact?)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactsFragment()
    }
}
