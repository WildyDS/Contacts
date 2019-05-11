package alexander.logunov.contacts.adapter

import alexander.logunov.contacts.data.model.EducationPeriod
import alexander.logunov.contacts.data.model.Temperament
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("android:text")
fun TextView.setText(educationPeriod: EducationPeriod) {
    text = educationPeriod.toString()
}

@BindingAdapter("android:text")
fun TextView.setText(temperament: Temperament) {
    text = temperament.toString()
}
