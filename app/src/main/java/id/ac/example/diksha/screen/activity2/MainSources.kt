package id.ac.example.diksha.screen.activity2

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
import id.ac.example.diksha.databinding.MainSourcesBinding
import id.ac.example.diksha.modal.GenerateTool
import id.ac.example.diksha.modal.MyApplication
import id.ac.example.diksha.modal.model.ModelSources
import id.ac.example.diksha.screen.activity3.MainNews
import javax.inject.Inject

class MainSources : AppCompatActivity(), AdapterSources.BtnSourcesListener {
    companion object {
        const val extraIdCategory = "idCategory"
        const val extraStrCategory = "strCategory"
    }

    private var arraySources = ArrayList<ModelSources>()
    private val viewModel: SourcesViewModel by viewModels {
        VMFactorySources(application)
    }
    private lateinit var binding: MainSourcesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: Dialog
    private lateinit var sourcesAdapter: AdapterSources
    private lateinit var idCategory: String
    private lateinit var strCategory: String

    private var maxArticle: Int = 0

    @Inject
    lateinit var generateTool: GenerateTool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.main_sources)
        binding.lifecycleOwner = this

        recyclerView = binding.recycleSources
        sourcesAdapter = AdapterSources(this, arraySources)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = sourcesAdapter


        val tangkap = intent
        idCategory = tangkap.getStringExtra(extraIdCategory).toString()
        strCategory = tangkap.getStringExtra(extraStrCategory).toString()

        binding.strCategory.text = strCategory
        viewModel.runVolleySources(idCategory, "")

        progressBar = generateTool.initialDialog(this)

        viewModel.showMessage.observe(this) { pesan ->

            if (pesan.show) {
                viewModel.setShowMessage(false)
                generateTool.popUpMessageFinish(this, pesan.tittle, pesan.message)
            }
        }
        viewModel.volleyRun.observe(this) { show ->
            if (show)
                progressBar.show()
            else
                progressBar.dismiss()


        }
        viewModel.sources.observe(this) {
            val panjangListAwal = arraySources.size


            arraySources.addAll(it)
            sourcesAdapter.notifyItemRangeInserted(panjangListAwal, arraySources.size)

            setKeterangan()
        }

        binding.searchSources.addTextChangedListener(textWatcher)

        viewModel.maxArticle.observe(this) {
            maxArticle = it
        }

        val nestedScrollView: NestedScrollView = binding.nestedScrollSources
        nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            // Periksa apakah sudah berada di posisi terbawah
            val isAtBottom = viewModel.volleyRun.value == false &&
                    scrollY == (nestedScrollView.getChildAt(0).measuredHeight - nestedScrollView.measuredHeight)
            if (isAtBottom) {
                if (arraySources.size != maxArticle)
                    viewModel.getMoreData()
                else
                    if (maxArticle > 0) {
                        generateTool.showToast("Semua sumber berita yang tersedia telah ditampilkan. ")
                    }
            }
        }

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private val handler = Handler(Looper.getMainLooper())

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                arraySources.clear()
                viewModel.reset()
                sourcesAdapter.notifyDataSetChanged()

                viewModel.runVolleySources(idCategory, p0.toString().trim())
                setKeterangan()
            }, 1000)
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }

    private fun setKeterangan() {
        val showingText = if (arraySources.size == maxArticle)
            resources.getString(R.string.showingEntries, arraySources.size)
        else
            resources.getString(
                R.string.showingEntriesOf,
                arraySources.size,
                maxArticle
            )
        binding.statusSearch.text = showingText

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun pindahActivity(modelBerita: ModelSources) {

        val pindahbawa = Intent(this, MainNews::class.java)
        pindahbawa.putExtra(MainNews.extraIdSources, modelBerita.id)
        pindahbawa.putExtra(MainNews.extraStrSources, modelBerita.name)
        startActivity(pindahbawa)

    }
}