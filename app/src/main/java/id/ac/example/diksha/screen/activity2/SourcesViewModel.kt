package id.ac.example.diksha.screen.activity2

import android.app.Application
import android.os.Handler
import android.os.Looper
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
import id.ac.example.diksha.modal.model.ModelPesan
import id.ac.example.diksha.modal.model.ModelSources
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


class SourcesViewModel(application: Application) : AndroidViewModel(application) {


    @Inject
    lateinit var generateTool: GenerateTool

    private var listSources = ArrayList<ModelSources>()

    private var _sources = MutableLiveData<List<ModelSources>>()
    val sources: LiveData<List<ModelSources>>
        get() = _sources

    private val _volleyRun = MutableLiveData<Boolean>()
    val volleyRun: LiveData<Boolean>
        get() = _volleyRun


    private val _showMessage = MutableLiveData<ModelPesan>()
    val showMessage: LiveData<ModelPesan>
        get() = _showMessage

    private val _maxArticle = MutableLiveData<Int>()
    val maxArticle: LiveData<Int>
        get() = _maxArticle

    private val pageSize = 6


    private var dataKe = 0;
    fun setShowMessage(
        value: Boolean,
        tittle: String = "Informasi",
        message: String = "Message is Empty"
    ) {
        _showMessage.value =
            ModelPesan(show = value, tittle = tittle, message = message, isFinish = true)
    }


    init {

        (getApplication() as MyApplication).appComponent.inject(this)

        reset()
    }

    fun reset() {
        dataKe = 0
        _maxArticle.value = 0
        _volleyRun.value = false
        setShowMessage(false)
    }

    private val apiURL =
        "https://newsapi.org//v2/top-headlines/sources?category="

    fun runVolleySources(category: String, q: String) {
        if (_volleyRun.value == true) return


        _volleyRun.value = true

        val stringRequest = object : StringRequest(
            Method.GET,
            apiURL + category,
            { response: String ->

                _volleyRun.value = false
                try {
                    val gson = Gson()

                    val jsonObject = JSONObject(response)
                    if (jsonObject.getString("status").equals("ok")) {
                        val jsonListSources = jsonObject.getJSONArray("sources")


                        listSources.clear()
                        for (i in 0 until jsonListSources.length()) {

                            val jsonObjectSources = jsonListSources.getJSONObject(i)
                            val sources = gson.fromJson(
                                jsonObjectSources.toString(),
                                ModelSources::class.java
                            )
//                            Timber.e(sources.toString())
//                            Timber.e("${sources.toString().contains(q)}")
                            if (sources.toString().contains(q)) {
                                listSources.add(sources)
                            }

                        }
                        _maxArticle.value = listSources.size
                        getMoreData()

                    } else {

                        Timber.e(jsonObject.toString(3))

                        setShowMessage(
                            true,
                            jsonObject.getString("code"),
                            jsonObject.getString("message")
                        )
                    }


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    e.message?.let { message -> setShowMessage(value = true, message = message) }

                }

                //
            },
            Response.ErrorListener { error ->
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
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = mutableMapOf<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                headers["Authorization"] = generateTool.token

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

    private val handler = Handler(Looper.getMainLooper())
    fun getMoreData() {
        if (_volleyRun.value == true) return

        _volleyRun.value = true
        handler.postDelayed({
            Timber.e("listSources.size >= pageSize ${(listSources.size - dataKe) >= pageSize}")
            Timber.e("${(listSources.size - dataKe)} > $pageSize")
            val sisa = listSources.size - dataKe
            if (sisa > pageSize) {

                inputKeLiveData(pageSize)
            } else {
                inputKeLiveData(sisa)
            }
        }, 1500)

    }

    private fun inputKeLiveData(size: Int) {
        val tempArray = ArrayList<ModelSources>()
        for (i in 0 until size) {
            Timber.e("print i $i $size $dataKe")
            tempArray.add(listSources[dataKe])
            dataKe++
        }

        _sources.value = tempArray

        _volleyRun.value = false
    }


}