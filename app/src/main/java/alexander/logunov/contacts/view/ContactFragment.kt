package alexander.logunov.contacts.view

import alexander.logunov.contacts.MainActivity
import alexander.logunov.contacts.R
import alexander.logunov.contacts.data.model.Contact
import alexander.logunov.contacts.databinding.FragmentContactBinding
import alexander.logunov.contacts.view_model.ContactViewModel
import alexander.logunov.contacts.view_model.ContactViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

class ContactFragment(val contact: Contact) : Fragment() {

    private lateinit var binding: FragmentContactBinding

    companion object {
        fun newInstance(contact: Contact) = ContactFragment(contact)
    }

    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)
        if (context is MainActivity) {
            with (context as MainActivity) {
                setSupportActionBar(binding.toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowHomeEnabled(true)
            }
        }
        viewModel = ViewModelProviders.of(
            this,
            ContactViewModelFactory(activity!!.application, contact)
        ).get(ContactViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}
