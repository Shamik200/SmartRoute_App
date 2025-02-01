import com.example.smartroute.Trip
import com.google.android.gms.common.api.Response
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitService {
    @Multipart
    @POST("upload")
    fun uploadFiles(
        @Part file1: MultipartBody.Part,
        @Part file2: MultipartBody.Part
    ): Call<List<Trip>>
}

object RetrofitClient {
    private const val BASE_URL = "http://<your-server-ip>:5000/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RetrofitService = retrofit.create(RetrofitService::class.java)
}