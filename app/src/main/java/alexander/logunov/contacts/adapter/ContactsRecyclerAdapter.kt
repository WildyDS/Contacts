package alexander.logunov.contacts.adapter

import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.ContactBinding
import alexander.logunov.contacts.view.ContactListFragment.OnListFragmentInteractionListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

class ContactsRecyclerAdapter(
    private var mContacts: List<Contact>?,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val contact = v.tag as Contact
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(contact)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contactBinding = ContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(contactBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mContact = mContacts!![position]
        with(holder.binding) {
            contact = mContact
            root.tag = mContact
            root.setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mContacts?.size ?: 0

    inner class ViewHolder(val binding: ContactBinding) : RecyclerView.ViewHolder(binding.root) {
        override fun toString(): String {
            return super.toString() + " '" + binding.name + "'"
        }
    }

    val contactsChangeHandler: Observer<List<Contact>?> = Observer { contacts ->
        mContacts = contacts
        notifyDataSetChanged()
    }
}
