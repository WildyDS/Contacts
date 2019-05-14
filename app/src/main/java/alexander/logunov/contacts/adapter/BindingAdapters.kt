package alexander.logunov.contacts.adapter

import alexander.logunov.contacts.data.model.EducationPeriod
import alexander.logunov.contacts.data.model.Temperament
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

const val TAG = "BindingAdapter"

@BindingAdapter("android:text")
fun TextView.setText(educationPeriod: EducationPeriod) {
    text = educationPeriod.toString()
}

@BindingAdapter("android:text")
fun TextView.setText(temperament: Temperament) {
    text = temperament.toString()
}

@BindingAdapter("app:visibility")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("app:isRefreshing")
fun SwipeRefreshLayout.setIsRefreshing(refreshing: Boolean) {
    isRefreshing = refreshing
}

@BindingAdapter("app:onRefresh")
fun SwipeRefreshLayout.setOnRefreshListener(refreshListener: SwipeRefreshLayout.OnRefreshListener) {
    setOnRefreshListener(refreshListener)
}

@BindingAdapter("app:snackbarLong")
fun View.showSnackBar(text: String?) {
    if (text != null) {
        try {
            Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
        } catch (e: RuntimeException) {
            Log.w(TAG, "Can not show snackbar!", e)
        }
    }
}
