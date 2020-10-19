package com.peter.memo3.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.peter.memo3.R
import com.peter.memo3.databinding.ActivityGalleryBinding
import com.peter.memo3.main.MainActivity

class GalleryActivity : AppCompatActivity(), GalleryAdapter.GalleryItemListener {

    private val requestCode: Int = 0

    private lateinit var binding: ActivityGalleryBinding
    private val viewModel by viewModels<GalleryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        if (hasReadExternalStoragePermission()) {
            create()
        } else {
            if (isMMore()) requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ),
                requestCode
            )
        }
    }

    private fun isMMore(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    private fun hasReadExternalStoragePermission(): Boolean {
        return if (isMMore()) {
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun create() {
        setListener()
        initView()
        initRecyclerView()
        setObserver()
    }




    private fun setListener() {
        binding.apply {
            toolbarBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun initView() {
        binding.apply {
            toolbarText.text = getString(R.string.app_name)
        }
    }

    private fun initRecyclerView() {
        binding.imageList.apply {
            layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            adapter = GalleryAdapter(this@GalleryActivity)
        }
    }

    private fun setObserver() {
        viewModel.apply {
            imageListLiveData.observe(
                this@GalleryActivity,
                Observer {
                    (binding.imageList.adapter as GalleryAdapter).submitList(it)
                }
            )
        }
    }

    override fun onClick(position: Int) {
        val imageUrl = viewModel.getImageUriByPosition(position)
        val intent = Intent().apply {
            putExtra("image_url", imageUrl)
        }
        setResult(MainActivity.REQUEST_CODE_GALLERY, intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == this.requestCode) {
            grantResults.forEach {
                if (it == PackageManager.PERMISSION_DENIED) {
                    AlertDialog.Builder(this)
                        .setTitle("권한이 필요합니다")
                        .setMessage(
                            "사진을 첨부하기 위해서 해당 권한이 필요합니다.\n" +
                                    "설정 > 앱 에서 권한을 허용해 주십시오."
                        )
                        .setPositiveButton(
                            "설정"
                        ) { _, _ ->
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            ).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            startActivity(intent)
                            finish()
                        }
                        .setNegativeButton("닫기") { _, _ ->
                            finish()
                        }
                        .show()
                    return@onRequestPermissionsResult
                }
            }

            create()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}