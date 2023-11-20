package com.egorovfond.artillery.view.db

import android.app.Activity
import android.app.DownloadManager
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.egorovfond.artillery.R
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.INSTALL_REQUEST_CODE
import com.egorovfond.artillery.view.MapsActivity
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.github.javiersantos.appupdater.objects.Update
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallConfirmResult
import kotlinx.android.synthetic.main.activity_dbsettings.*
import java.io.File

const val REQUEST_INSTALL = 124
const val gitUser = "egorov-pavel-dev"
const val repo ="Artillery_tu"
const val appName = "Artillery.apk"
const val APP_ID = "com.egorovfond.artillery"

class DBSettingsActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbsettings)

        button_settingsdb_weapon.setOnClickListener {
            val intent = Intent(this, DBWeaponActivity::class.java)
            startActivity(intent)
        }
        button_settingsdb_bullet.setOnClickListener {
            val intent = Intent(this, DBBulletActivity::class.java)
            startActivity(intent)
        }
        button_settingsdb_table.setOnClickListener {
            val intent = Intent(this, DBTableActivity::class.java)
            startActivity(intent)
        }
        button_settingsdb_maps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        button_settingsdb_reset.setOnClickListener {
            presenter.reset()
        }
        localMap.setOnClickListener {
            presenter.localmap = localMap.isChecked
        }

        btn_maps_update.setOnClickListener {
            AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo(gitUser, repo)
                .withListener(object : AppUpdaterUtils.UpdateListener {
                    override fun onSuccess(update: Update, isUpdateAvailable: Boolean) {
                        if (isUpdateAvailable) {
                            val url =
                                "https://github.com/$gitUser" + "/" + repo + "/releases/download/" + update.latestVersion + "/" + appName
                            update(url, appName)
                        }else{
                            Toast.makeText(this@DBSettingsActivity, "Обновления не найдены!", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailed(error: AppUpdaterError) {
                        Toast.makeText(this@DBSettingsActivity, error.toString(), Toast.LENGTH_LONG).show()
                        Log.e("UPDATE", "Failed")
                    }
                }).start()
        }

        localMap.isChecked = presenter.localmap

        val versionName =  this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;

        nameVersion.setText(" Версия \n ${versionName} ")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_INSTALL){

            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this,"Install succeeded!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"Install canceled!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Install Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    fun update(url: String?, fileName: String) {
        //get destination to update file and set Uri
        val destination =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() // + File.separator;
        val destUri = Uri.parse("file://" + destination + File.separator + fileName)
        //val destUri = Uri.parse(url)
        Toast.makeText(this@DBSettingsActivity, destUri.toString(), Toast.LENGTH_SHORT).show()

        Log.d("UPDATE", "destUri = $destUri")

        //Delete update file if exists
        val file = File(destination + File.separator + fileName)
        if (file.exists()) {
            val deleteSuccess = file.delete()
            Toast.makeText(this@DBSettingsActivity, "Файл существует! Удаление = $deleteSuccess", Toast.LENGTH_SHORT).show()

            Log.d("UPDATE", "file exists! Delete = $deleteSuccess")
        }

        //set downloadmanager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setDescription("Обновление Artillery")
        request.setTitle("Artillery")

        //set destination
        request.setDestinationUri(destUri)

        // get download service and enqueue file
        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        file.setReadable(true, false)

        //set BroadcastReceiver to install app when .apk is downloaded
        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent_: Intent) {
                var intent = intent_
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val apkUri = FileProvider.getUriForFile(
                        applicationContext, APP_ID + ".provider", file
                    )
                    intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)

                } else {
                    val apkUri = Uri.fromFile(file)
                    intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivityForResult(intent, REQUEST_INSTALL)
                //startActivity(intent)
                unregisterReceiver(this)
                //finish()
            }
        }

        //register receiver for when .apk download is compete
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        manager.enqueue(request)

    }

}
