package dev.x001.foodies.view.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.permissionx.guolindev.PermissionX
import dev.x001.foodies.R
import dev.x001.foodies.databinding.ActivityAddUpdateDishBinding
import dev.x001.foodies.databinding.DialogImageSelectionBinding

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addImageImageView.setOnClickListener(this)

        Animatoo.animateSlideLeft(this)
    }

    override fun onClick(view: View?) {
        if (view != null){
            when(view.id){
                R.id.add_image_imageView -> {
                    dialogSelectImage()
                }
            }
        }
    }

    private fun dialogSelectImage(){
        val dialog = Dialog(this)
        val dialogBinding : DialogImageSelectionBinding = DialogImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.cameraLinearLayout.setOnClickListener {
            PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(deniedList, "App needs permission to complete action.", "OK", "Cancel")
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                    } else {
                        showRationalDialogForPermissions("Camera and storage permission required. Go to settings and enable camera and storage permission.")
                    }
                }
            dialog.dismiss()
        }

        dialogBinding.galleryLinearLayout.setOnClickListener {
            PermissionX.init(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(deniedList, "This app needs storage permission to add photos.", "OK", "Cancel")
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                    } else {
                        showRationalDialogForPermissions("Storage permission required. Go to settings and enable storage permission.")
                    }
                }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRationalDialogForPermissions(message: String){
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Go to Settings"){
                    _ , _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL"){
                    dialog, which ->
                dialog.dismiss()
            }.show()
    }
}