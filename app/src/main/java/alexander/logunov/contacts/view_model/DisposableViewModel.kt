package alexander.logunov.contacts.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class DisposableViewModel(application: Application) : AndroidViewModel(application) {
    private val disposable = CompositeDisposable()

    fun addDisposable(d: Disposable) = disposable.add(d)

    fun addAllDisposables(vararg ds: Disposable) = disposable.addAll(*ds)

    fun clearDisposable() = disposable.clear()

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}