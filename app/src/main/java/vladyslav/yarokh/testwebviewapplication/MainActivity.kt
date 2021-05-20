package vladyslav.yarokh.testwebviewapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import vladyslav.yarokh.testwebviewapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        this.onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(binding.webView.canGoBack()){
                    binding.webView.goBack()
                } else {
                    binding.btnOpen.visibility = View.VISIBLE
                    binding.etCookie.visibility = View.VISIBLE
                    binding.etUrl.visibility = View.VISIBLE
                    binding.webView.visibility = View.GONE
                }
            }
        })

        initViews()
    }

    private fun initViews(){
        binding.apply {
            btnOpen.setOnClickListener {
                if(etUrl.text.isEmpty()){
                    Toast.makeText(this@MainActivity, "Url field is empty", Toast.LENGTH_LONG).show()
                } else {
                    webView.apply {
                        webChromeClient = object : WebChromeClient() {
                            override fun onCreateWindow(
                                view: WebView?,
                                isDialog: Boolean,
                                isUserGesture: Boolean,
                                resultMsg: Message?
                            ): Boolean {
                                val transport = resultMsg?.obj as WebView.WebViewTransport
                                transport.webView = webView
                                resultMsg.sendToTarget()
                                return true
                            }
                        }
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {

                            }
                        }
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            setSupportMultipleWindows(false)
                            javaScriptCanOpenWindowsAutomatically = true
                        }
                    }

                    CookieManager.getInstance().apply {
                        acceptCookie()
                        setCookie(etUrl.text.toString(), etCookie.text.toString())
                        setAcceptThirdPartyCookies(webView, true)
                    }

                    webView.loadUrl(etUrl.text.toString())

                    btnOpen.visibility = View.GONE
                    etCookie.visibility = View.GONE
                    etUrl.visibility = View.GONE
                    webView.visibility = View.VISIBLE
                }
            }
        }
    }
}