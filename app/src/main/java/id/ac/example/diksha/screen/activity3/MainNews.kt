package id.ac.example.diksha.screen.activity3

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.example.diksha.R
import id.ac.example.diksha.databinding.MainNewsBinding
import id.ac.example.diksha.modal.GenerateTool
import id.ac.example.diksha.modal.MyApplication
import id.ac.example.diksha.modal.model.ModelNews
import id.ac.example.diksha.screen.activity4.MainWebView
import javax.inject.Inject

class MainNews : AppCompatActivity(), AdapterNews.BtnSourcesListener {
    companion object {
        const val extraIdSources = "idSources"
        const val extraStrSources = "strSources"
    }

    private val viewModel: NewsViewModel by viewModels {
        VMFactoryNews(application)
    }
    private var arrayNews = ArrayList<ModelNews>()
    private lateinit var binding: MainNewsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: Dialog
    private lateinit var adapterNews: AdapterNews

    private lateinit var idSources: String
    private lateinit var strSources: String
    private var queriestxt: String = ""
    private var maxArticle: Int = 0


    @Inject
    lateinit var generateTool: GenerateTool
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.main_news)
        binding.lifecycleOwner = this

        progressBar = generateTool.initialDialog(this)

        recyclerView = binding.recycleSources
        adapterNews = AdapterNews(this, arrayNews)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterNews

        val tangkap = intent
        idSources = tangkap.getStringExtra(extraIdSources).toString()
        strSources = tangkap.getStringExtra(extraStrSources).toString()
        binding.strCategory.text = strSources
        viewModel.runVolleyNews(idSources, "")

        viewModel.showMessage.observe(this) { pesan ->

            if (pesan.show) {
                viewModel.setShowMessage(false)
                if (pesan.isFinish)
                    generateTool.popUpMessageFinish(this, pesan.tittle, pesan.message)
                else
                    generateTool.popUpMessage(this, pesan.tittle, pesan.message)
            }
        }

        viewModel.volleyRun.observe(this) { show ->
            if (show)
                progressBar.show()
            else
                progressBar.dismiss()


        }

        viewModel.news.observe(this) { list ->
            val panjangListAwal = arrayNews.size

            arrayNews.addAll(list)
            adapterNews.notifyItemRangeInserted(panjangListAwal, arrayNews.size)
            setKeterangan()
        }

        viewModel.maxArticle.observe(this) {
            maxArticle = it
        }

        val nestedScrollView: NestedScrollView = binding.nestedScrollArticle
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            // Periksa apakah sudah berada di posisi terbawah
            val isAtBottom = viewModel.volleyRun.value == false &&
                    scrollY == (nestedScrollView.getChildAt(0).measuredHeight - nestedScrollView.measuredHeight)
            if (isAtBottom) {
                if (arrayNews.size != maxArticle)
                    viewModel.runVolleyNews(idSources, queriestxt)
                else
                    if (maxArticle > 0) {
                        generateTool.showToast("Semua artikel yang tersedia telah ditampilkan. ")
//                        Timber.e("test $aaa")
//                        aaa++
                    }
            }
        }

        binding.searchSources.addTextChangedListener(textWatcher)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

//    private var aaa = 1
    private val handler = Handler(Looper.getMainLooper())
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                queriestxt = p0.toString().trim()
                viewModel.resetPage()
                arrayNews.clear()
                adapterNews.notifyDataSetChanged()
                setKeterangan()
                viewModel.runVolleyNews(idSources, queriestxt)
            }, 1000)
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

    private fun setKeterangan() {
        val showingText = if (arrayNews.size == maxArticle)
            resources.getString(R.string.showingEntries, maxArticle)
        else
            resources.getString(
                R.string.showingEntriesOf,
                arrayNews.size,
                maxArticle
            )
        binding.statusSearch.text = showingText

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun clickNews(modelBerita: ModelNews) {
        val pindahbawa = Intent(this, MainWebView::class.java)
        pindahbawa.putExtra(MainWebView.extraTitle, modelBerita.title)
        pindahbawa.putExtra(MainWebView.extraURL, modelBerita.url)

        startActivity(pindahbawa)
    }
}