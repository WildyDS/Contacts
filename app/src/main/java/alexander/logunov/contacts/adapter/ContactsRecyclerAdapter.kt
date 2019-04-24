package alexander.logunov.contacts.adapter

import alexander.logunov.contacts.R
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.ContactBinding
import alexander.logunov.contacts.view.ContactsFragment.OnListFragmentInteractionListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact.view.*

// TODO: сделать binding, binding adapter

/**
 * [RecyclerView.Adapter] that can display a [Contact] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class ContactsRecyclerAdapter(
    private var mContacts: List<Contact>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Contact
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val contactBinding = ContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(contactBinding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = mContacts[position]

        holder.mNameView.text = contact.name
        holder.mPhoneView.text = contact.phone
        holder.mHeightView.text = contact.height.toString()

        with(holder.mView) {
            tag = contact
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mContacts.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val mNameView: TextView = mView.name
        val mPhoneView: TextView = mView.phone
        val mHeightView: TextView = mView.findViewById(R.id.height)

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }

    val contactsChangeHandler: Observer<List<Contact>> = Observer { contacts ->
        mContacts = contacts
        notifyDataSetChanged()
    }
}
