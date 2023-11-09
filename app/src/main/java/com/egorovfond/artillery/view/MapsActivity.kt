package com.egorovfond.artillery.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.R
import com.egorovfond.artillery.view.rvAdapter.MapRvAdapter
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.AppUpdaterUtils.UpdateListener
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallConfirmResult
import kotlinx.android.synthetic.main.activity_maps.*


const val INSTALL_REQUEST_CODE = 123
class MapsActivity : AppCompatActivity() {
    private var adapter: MapRvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        btn_maps_update.setOnClickListener {
//            val appUpdater = AppUpdater(this)
//                .setUpdateFrom(UpdateFrom.GITHUB)
//                .setGitHubUserAndRepo("egorov-pavel-dev", "Artillery_tu")
//                //.setUpdateFrom(UpdateFrom.XML)
//                .setUpdateXML("https://github.com/egorov-pavel-dev/Artillery_tu/blob/master/app/version.xml")
//                .setDisplay(Display.DIALOG)
//                .showAppUpdated(true)
//                .setTitleOnUpdateAvailable("Доступно обновление")
//                .setContentOnUpdateAvailable("Проверить обновления")
//                .setTitleOnUpdateNotAvailable("Обновлений не найдено")
//                .setContentOnUpdateNotAvailable("В данный момент у вас установлена последняя версия программы!")
//                .setButtonUpdate("Обновить?")
//                .setButtonDismiss("Не обновлять")
//
//            appUpdater.start()
            val appUpdaterUtils = AppUpdaterUtils(this)
                //.setUpdateFrom(UpdateFrom.AMAZON)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("egorov-pavel-dev", "Artillery_tu")
                //...
                .withListener(object : UpdateListener {
                    override fun onSuccess(update: Update, isUpdateAvailable: Boolean?) {
                        Log.d("Latest Version", update.getLatestVersion())
                        Log.d("Latest Version Code", update.getLatestVersionCode().toString())
                        Log.d("Release notes", update.getReleaseNotes())
                        Log.d("URL", update.getUrlToDownload().toString())
                        Log.d(
                            "Is update available?",
                            java.lang.Boolean.toString(isUpdateAvailable!!)
                        )
                    }

                    override fun onFailed(error: AppUpdaterError) {
                        Log.d("AppUpdater Error", "Something went wrong")
                    }
                })
            appUpdaterUtils.start()
        }
    }

    override fun onStart() {
        super.onStart()

        updateList()
    }

    private fun updateList() {
        adapter ?: initRecyclerView()
        adapter?.let {
            it.updateDataUser()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INSTALL_REQUEST_CODE
            && data?.hasExtra(GlobalSplitInstallConfirmResult.EXTRA_RESULT) == true
        ) {
            val confirmResult = data.getIntExtra(
                GlobalSplitInstallConfirmResult.EXTRA_RESULT,
                GlobalSplitInstallConfirmResult.RESULT_DENIED
            )

            if (confirmResult == GlobalSplitInstallConfirmResult.RESULT_CONFIRMED) {
                // User granted permission, install again!
            }
        }
    }

    private fun initRecyclerView() {

        adapter = MapRvAdapter()

        maps_rv.layoutManager = LinearLayoutManager(this)
        maps_rv.adapter = adapter
        maps_rv.setHasFixedSize(true)
    }
}