package com.egorovfond.artillery.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.egorovfond.artillery.R
import com.egorovfond.artillery.databinding.FragmentMapBinding
import com.egorovfond.artillery.presenter.Presenter
import org.json.JSONObject


class MapFragment() : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private var typemap = 0
    private lateinit var binding: FragmentMapBinding

    val items = mutableListOf<String>()

    val mapAdapter by lazy{ ArrayAdapter(this, R.layout.list_item, items)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_map)
        binding = FragmentMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        items.clear()
        for (i in presenter.maps){
            items.add(i.name)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onStart() {
        super.onStart()

        binding.btnMapOk.setOnClickListener {
            presenter.setCoordinate(x / 1000, y / 1000)

            this.finish()
        }

        binding.mapEdt?.let {
            it.setAdapter(mapAdapter)
            it.setOnItemClickListener { parent, view, position, id ->
                run {
                    presenter.url = mapAdapter.getItem(position).toString()
                    presenter.setMapHeight(map = mapAdapter.getItem(position).toString())
                    updateWebView()
                }
            }
            it.setText(presenter.url)
        }

        if (presenter.map_settings == 1){
            x = presenter.getCurrentWeapon().x * 1000
            y = presenter.getCurrentWeapon().y * 1000
            typemap = 0
        }
        if (presenter.map_settings == 2){
            x = presenter.getCurrentWeapon().x_Dot * 1000
            y = presenter.getCurrentWeapon().y_Dot * 1000
            typemap = 1
        }
        if (presenter.map_settings == 3){
            x = presenter.currentEnemy.x * 1000
            y = presenter.currentEnemy.y * 1000
            typemap = 2
        }
        if (presenter.map_settings == 4){
            if (presenter.currentEnemy.x_prilet == 0f && presenter.currentEnemy.y_prilet == 0f){
                x = presenter.currentEnemy.x * 1000
                y = presenter.currentEnemy.y * 1000
            }else {
                x = presenter.currentEnemy.x_prilet * 1000
                y = presenter.currentEnemy.y_prilet * 1000
            }
            typemap = 2
        }

        w_x = presenter.getCurrentWeapon().x * 1000
        w_y = presenter.getCurrentWeapon().y * 1000
        w_r = presenter.getMaxRange(presenter.map_settings == 3 || presenter.map_settings == 4).toFloat()
        w_r_min = presenter.getMinRange(presenter.map_settings == 3 || presenter.map_settings == 4).toFloat()

        val webSetting = binding.webviewMap.getSettings()
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true)
        webSetting.javaScriptEnabled = true
        binding.webviewMap.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        binding.webviewMap.webViewClient = Callback()
        binding.webviewMap.webChromeClient = MyWebChromeClient()

        updateWebView()
    }

    fun updateWebView(){
        if (presenter.url.isNotEmpty()) binding.webviewMap.loadUrl("file:///android_asset/${if (presenter.localmap) {presenter.url + "_local"} else {presenter.url} }.html?lat=${y}&lng=${x}&w_lat=${w_y}&w_lng=${w_x}&range=${w_r}&dot=${w_dot_r}&r_min=${w_r_min}&typemap=${typemap}")
    }

    class Callback: WebViewClient(){
        override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
            return false
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            url?.let {
                view?.loadUrl(it)
            }
            return true
        }
    }

    private class MyWebChromeClient() : WebChromeClient() {
        override fun onJsAlert(
            view: WebView,
            url: String,
            message: String,
            result: JsResult
        ): Boolean {
            return true
        }

        override fun onJsConfirm(
            view: WebView,
            url: String,
            message: String,
            result: JsResult
        ): Boolean {
            return true
        }

        override fun onJsPrompt(
            view: WebView, url: String, message: String, defaultValue: String,
            result: JsPromptResult
        ): Boolean {
            return true
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            consoleMessage?.let {
                if (it.messageLevel() != ConsoleMessage.MessageLevel.ERROR) {
                    val jObject = JSONObject(it.message())
                    x = jObject.getDouble("lng").toFloat()
                    y = jObject.getDouble("lat").toFloat()
                }
            }

            return super.onConsoleMessage(consoleMessage)
        }
    }

    companion object {
        var x = 0f
        var y = 0f
        var w_x = 0f
        var w_y = 0f
        var w_dot_r = 60f
        var w_r = 0f
        var w_r_min = 0f
    }
}