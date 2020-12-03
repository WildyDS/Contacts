package alexander.logunov.contacts.network

import alexander.logunov.contacts.data.model.ContactDTO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ContactsService {
    @GET("generated-0{page}.json")
    fun getContacts(@Path("page") page: Number): Single<List<ContactDTO>>
}
