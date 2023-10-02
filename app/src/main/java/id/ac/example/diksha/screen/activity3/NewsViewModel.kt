package id.ac.example.diksha.screen.activity3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkError
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import id.ac.example.diksha.modal.GenerateTool
import id.ac.example.diksha.modal.MyApplication
import id.ac.example.diksha.modal.model.ModelNews
import id.ac.example.diksha.modal.model.ModelPesan
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


class NewsViewModel(application: Application) : AndroidViewModel(application) {


    @Inject
    lateinit var generateTool: GenerateTool

    private var _news = MutableLiveData<List<ModelNews>>()
    val news: LiveData<List<ModelNews>>
        get() = _news

    private val _volleyRun = MutableLiveData<Boolean>()
    val volleyRun: LiveData<Boolean>
        get() = _volleyRun

    private val _showMessage = MutableLiveData<ModelPesan>()
    val showMessage: LiveData<ModelPesan>
        get() = _showMessage

    private val _maxArticle = MutableLiveData<Int>()
    val maxArticle: LiveData<Int>
        get() = _maxArticle

    fun resetPage() {
        page = 1
        _maxArticle.value = 0
    }

    fun setShowMessage(
        value: Boolean,
        tittle: String = "Informasi",
        message: String = "Message is Empty",
        finish: Boolean = true
    ) {
        _showMessage.value =
            ModelPesan(show = value, tittle = tittle, message = message, isFinish = finish)
    }


    init {
        (getApplication() as MyApplication).appComponent.inject(this)
        setShowMessage(false)
        _volleyRun.value = false
    }

    private var page = 1
    private val pageSize = 4
    var pernahAdaDatanya = false

    fun runVolleyNews(idNews: String, query: String) {
        if (_volleyRun.value == true) return

        _volleyRun.value = true
        val queriesnya = if (query != "") "&q=$query"
        else ""

        val apiURL =
            "https://newsapi.org/v2/top-headlines?pageSize=$pageSize&page=$page$queriesnya&sources=$idNews"
        Timber.e("APi = $apiURL")
        val stringRequest = object : StringRequest(Method.GET, apiURL, { response: String ->

            _volleyRun.value = false
            try {
                val gson = Gson()

                val jsonObject = JSONObject(response)
//                    Timber.e(jsonObject.toString(3))
                if (jsonObject.getString("status").equals("ok")) {
                    val jsonListNews = jsonObject.getJSONArray("articles")

                    val listNews = ArrayList<ModelNews>()
                    _maxArticle.value = jsonObject.getInt("totalResults")
                    if (jsonObject.getInt("totalResults") > 0) {

                        Timber.e(jsonListNews.getJSONObject(0).toString(3))
                        for (i in 0 until jsonListNews.length()) {

                            val jsonObjectNews = jsonListNews.getJSONObject(i)
                            val modelNews =
                                gson.fromJson(jsonObjectNews.toString(), ModelNews::class.java)

                            modelNews.urlToImage =
                                modelNews.urlToImage ?: "URL GAMBAR TIDAK DIKETAHUI"
                            modelNews.author = modelNews.author ?: "Anonymous"

                            listNews.add(modelNews)

                        }

                        _news.value = listNews
                        page++

                        Timber.e("zzz ${listNews.size}")
                        pernahAdaDatanya = true
                    } else {
                        setShowMessage(
                            true, message = "No Article", finish = !pernahAdaDatanya
                        )
                    }
                } else {

                    Timber.e(jsonObject.toString(3))

                    setShowMessage(
                        true,
                        tittle = jsonObject.getString("code"),
                        message = jsonObject.getString("message")
                    )
                }


            } catch (e: java.lang.Exception) {
                e.message?.let { message -> setShowMessage(value = true, message = message) }

            }

            //
        }, Response.ErrorListener { error ->
            _volleyRun.value = false

            if (!(error is NetworkError || error is TimeoutError)) {

                val networkResponse = error.networkResponse
                try {
                    val jsonObject = JSONObject(String(networkResponse.data))
                    Timber.e(jsonObject.toString(3))
                    setShowMessage(
                        value = true,
                        tittle = jsonObject.getString("code"),
                        message = jsonObject.getString("message")
                    )
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    setShowMessage(
                        value = true,
                        message = "Unexpected response code " + networkResponse.statusCode.toString()
                    )
                }

            } else {
                setShowMessage(
                    value = true,
                    message = "Gagal terhubung dengan server, silahkan coba beberapa saat lagi."
                )

            }
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                headers["Authorization"] = generateTool.token
//                headers["X-Api-Key"] = generateTool.token
                Timber.e(headers.toString())
                return headers
            }
        }
        stringRequest.retryPolicy = DefaultRetryPolicy(
            generateTool.requestTime,
            generateTool.requestRetries,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        Volley.newRequestQueue(getApplication()).add(stringRequest)
    }


}