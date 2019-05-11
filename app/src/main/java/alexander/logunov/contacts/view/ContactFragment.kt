package alexander.logunov.contacts.view

import alexander.logunov.contacts.MainActivity
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import alexander.logunov.contacts.R
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.ContactFragmentBinding
import alexander.logunov.contacts.view_model.ContactViewModel
import alexander.logunov.contacts.view_model.ContactViewModelFactory
import android.text.util.Linkify
import androidx.databinding.DataBindingUtil

class ContactFragment(val contact: Contact) : Fragment() {

    private lateinit var binding: ContactFragmentBinding

    companion object {
        fun newInstance(contact: Contact) = ContactFragment(contact)
    }

    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.contact_fragment, container, false)
        if (context is MainActivity) {
            with (context as MainActivity) {
                setSupportActionBar(binding.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowHomeEnabled(true)
            }
        }
        viewModel = ViewModelProviders.of(
            this,
            ContactViewModelFactory.getInstance(activity!!.application, contact)
        ).get(ContactViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }
}
