package alexander.logunov.contacts.network

import alexander.logunov.contacts.data.model.Contact
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*


const val BASE_URL = "https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/"

class Api(baseUrl: String) {
    companion object {
        const val TAG: String = "Api"
        private var selfInstance: Api? = null
        fun getInstance(): Api {
            if (selfInstance === null) {
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
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(ContactsService::class.java)


    fun getContacts(page: Number, handler: Callback<List<Contact>>) {
        service.getContacts(page).enqueue(handler)
    }
}