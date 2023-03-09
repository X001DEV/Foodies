package dev.x001.foodies.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.permissionx.guolindev.PermissionX
import dev.x001.foodies.R
import dev.x001.foodies.databinding.ActivityAddUpdateDishBinding
import dev.x001.foodies.databinding.DialogImageSelectionBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        private const val IMAGE_DIRECTORY = "FoodiesImages"
    }

    private lateinit var cameraImageResultLauncher: ActivityResultLauncher<Intent>
    private var saveImageToInternalStorage : Uri? = null

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

        registerOnActivityForCameraResult()
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
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraImageResultLauncher.launch(intent)
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

    private fun registerOnActivityForCameraResult(){
        cameraImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                    result ->
                if (result.resultCode == Activity.RESULT_OK){
                    val data: Intent? = result.data

                    if (data != null){
                        try {
                            val thumbNail: Bitmap =
                                result!!.data!!.extras?.get("data") as Bitmap
                            binding.imageView.setImageBitmap(thumbNail)

                            saveImageToInternalStorage = saveImageToInternalStorage(thumbNail)

                            Log.e("Saved image: ", "Path :: $saveImageToInternalStorage")
                        } catch (e: IOException){
                            e.printStackTrace()
                            Toast.makeText(this@AddUpdateDishActivity, "Failed to take photo from camera.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try{
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }
}