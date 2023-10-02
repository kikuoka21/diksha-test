package id.ac.example.diksha.screen.activity4

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import id.ac.example.diksha.R
import id.ac.example.diksha.modal.GenerateTool
import id.ac.example.diksha.modal.MyApplication
import timber.log.Timber
import javax.inject.Inject

class MainWebView : AppCompatActivity() {

    companion object {
        const val extraURL = "url"
        const val extraTitle = "title"
    }

    private var articleUrl =
        "https://www.google.com/"


    @Inject
    lateinit var generateTool: GenerateTool

    //        private lateinit var articleUrl: String
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as MyApplication).appComponent.inject(this)
        title = intent.getStringExtra(extraTitle).toString()
        setContentView(R.layout.web_view)
        val webView: WebView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
//        webView.settings.setSupportZoom(true)
        webView.webChromeClient = WebChromeClient()

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true


        articleUrl = intent.getStringExtra(extraURL).toString()
        Timber.e(articleUrl)

        if (articleUrl.startsWith("http://")) {
            articleUrl =  "https://" + articleUrl.substring(7)
        }
        webView.loadUrl(articleUrl)

        webView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(articleUrl)
            startActivity(intent)
        }

        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.openUrl -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                startActivity(intent)
                true
            }
            R.id.copyUrl -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("URL", articleUrl)
                clipboard.setPrimaryClip(clip)

                generateTool.showToast("The URL has been copied to the clipboard")

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_web_view, menu)
        return true
    }



}