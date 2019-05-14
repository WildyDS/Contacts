package alexander.logunov.contacts.network

import alexander.logunov.contacts.data.model.Contact
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Flowable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

const val BASE_URL = "https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/"

class Api(baseUrl: String) {
    companion object {
        const val TAG: String = "Api"
        private var selfInstance: Api? = null
        fun getInstance(): Api {
            if (selfInstance == null) {
                selfInstance = Api(BASE_URL)
            }
            return selfInstance as Api
        }
    }

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(Date::class.java, DateAdapter())
        .build()

    private val service: ContactsService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(ContactsService::class.java)


    fun getContacts(page: Number): Flowable<List<Contact>> {
        return service.getContacts(page)
    }
}