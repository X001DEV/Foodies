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
import android.os.Build
import android.os.Build.VERSION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.permissionx.guolindev.PermissionX
import dev.x001.foodies.R
import dev.x001.foodies.application.DishApplication
import dev.x001.foodies.databinding.ActivityAddUpdateDishBinding
import dev.x001.foodies.databinding.DialogImageSelectionBinding
import dev.x001.foodies.databinding.DialogListBinding
import dev.x001.foodies.model.entities.Dish
import dev.x001.foodies.utils.Constants
import dev.x001.foodies.view.adapter.ListItemAdapter
import dev.x001.foodies.viewmodel.DishViewModel
import dev.x001.foodies.viewmodel.DishViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var cameraImageResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryImageResultLauncher: ActivityResultLauncher<Intent>
    private var saveImageToInternalStorage: Uri? = null
    private var mImagePath = ""

    private lateinit var mListDialog: Dialog

    private var mDishDetails: Dish? = null

    private val mDishViewModel: DishViewModel by viewModels {
        DishViewModelFactory((application as  DishApplication).repository)
    }

    private lateinit var binding: ActivityAddUpdateDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_DISH_DETAILS)){
            mDishDetails = intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
        }

        toast("${mDishDetails?.dish}")

        mDishDetails?.let {
            binding.toolbar.title = "Update"

            if(it.id != 0){
                mImagePath = it.image
                Glide.with(this@AddUpdateDishActivity)
                    .load(mImagePath)
                    .into(binding.imageView)
                binding.dishNameEditText.setText(it.dish)
                binding.typeEditText.setText(it.type)
                binding.categoryEditText.setText(it.category)
                binding.ingredientsEditText.setText(it.ingredients)
                binding.cookingTimeEditText.setText(it.cookingTime)
                binding.directionsEditText.setText(it.directionToCook)

                binding.saveButton.text = "Update"
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addImageImageView.setOnClickListener(this)
        binding.typeEditText.setOnClickListener(this)
        binding.categoryEditText.setOnClickListener(this)
        binding.cookingTimeEditText.setOnClickListener(this)
        binding.saveButton.setOnClickListener(this)

        Animatoo.animateSlideLeft(this)

        registerOnActivityForCameraResult()
        registerOnActivityForGalleryResult()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.add_image_imageView -> {
                    dialogSelectImage()
                }
                R.id.type_editText -> {
                    dialogItems("Select Dish Type", Constants.dishTypes, Constants.DISH_TYPE)
                }
                R.id.category_editText -> {
                    dialogItems(
                        "Select Dish Category",
                        Constants.dishCategory,
                        Constants.DISH_CATEGORY
                    )
                }
                R.id.cooking_time_editText -> {
                    dialogItems(
                        "Select Cooking Time",
                        Constants.dishCookTime,
                        Constants.DISH_COOKING_TIME
                    )
                }
                R.id.save_button -> {
                    val title = binding.dishNameEditText.text.toString().trim { it <= ' ' }
                    val type = binding.typeEditText.text.toString().trim { it <= ' ' }
                    val category = binding.categoryEditText.text.toString().trim { it <= ' ' }
                    val ingredients = binding.ingredientsEditText.text.toString().trim { it <= ' ' }
                    val cookingTimeInMinutes = binding.cookingTimeEditText.text.toString().trim { it <= ' ' }
                    val cookingDirection = binding.directionsEditText.text.toString().trim { it <= ' ' }

                    when{
                        TextUtils.isEmpty(mImagePath) -> {
                            toast("Please select an image.")
                        }
                        TextUtils.isEmpty(title) -> {
                            toast("Please enter a title.")
                        }
                        TextUtils.isEmpty(type) -> {
                            toast("Please select a type.")
                        }
                        TextUtils.isEmpty(category) -> {
                            toast("Please select a category.")
                        }
                        TextUtils.isEmpty(ingredients) -> {
                            toast("Please write the ingredients.")
                        }
                        TextUtils.isEmpty(cookingTimeInMinutes) -> {
                            toast("Please select a cooking time.")
                        }
                        TextUtils.isEmpty(cookingDirection) -> {
                            toast("Please write a cooking direction.")
                        }
                        else -> {
                            var dishID = 0
                            var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL
                            var favoriteDish = false

                            mDishDetails?.let {
                                if (it.id != 0){
                                    dishID = it.id
                                    imageSource = it.imageSource
                                    favoriteDish = it.favoriteDish
                                }
                            }


                           val dishDetails: Dish = Dish(
                               mImagePath,
                               imageSource,
                               title,
                               type,
                               category,
                               ingredients,
                               cookingTimeInMinutes,
                               cookingDirection,
                               favoriteDish,
                               dishID
                           )

                            if (dishID == 0){
                                mDishViewModel.insert(dishDetails)
                                toast("Dish added!")
                                Log.d("Insertion", "Success")
                            }else{
                                //Update dish
                                mDishViewModel.update(dishDetails)
                                toast("Updated dish!")
                            }

                            //close activity
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun dialogSelectImage() {
        val dialog = Dialog(this)
        val dialogBinding: DialogImageSelectionBinding =
            DialogImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.cameraLinearLayout.setOnClickListener {
            PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList,
                        "App needs permission to complete action.",
                        "OK",
                        "Cancel"
                    )
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
                    scope.showRequestReasonDialog(
                        deniedList,
                        "This app needs storage permission to add photos.",
                        "OK",
                        "Cancel"
                    )
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        val galleryIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        galleryImageResultLauncher.launch(galleryIntent)
                    } else {
                        showRationalDialogForPermissions("Storage permission required. Go to settings and enable storage permission.")
                    }
                }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRationalDialogForPermissions(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Go to Settings") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL") { dialog, which ->
                dialog.dismiss()
            }.show()
    }

    private fun registerOnActivityForCameraResult() {
        cameraImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    if (data != null) {
                        try {
                            val thumbNail: Bitmap =
                                result!!.data!!.extras?.get("data") as Bitmap
                            binding.imageView.setImageBitmap(thumbNail)

                            saveImageToInternalStorage = saveImageToInternalStorage(thumbNail)
                            binding.addImageImageView.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this,
                                    R.drawable.ic_baseline_edit_24
                                )
                            )
                            mImagePath = saveImageToInternalStorage.toString()
                            Log.e("Saved image: ", "Path :: $saveImageToInternalStorage")
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                "Failed to take photo from camera.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
    }

    private fun registerOnActivityForGalleryResult() {
        galleryImageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data

                    if (data != null) {
                        val contentUri = data.data
                        try {
                            binding.imageView.setImageURI(contentUri)
                            val bitmap: Bitmap =
                                MediaStore.Images.Media.getBitmap(contentResolver, contentUri)

                            saveImageToInternalStorage = saveImageToInternalStorage(bitmap)

                            mImagePath = saveImageToInternalStorage.toString()
                            Log.e("Saved image: ", "Path :: $saveImageToInternalStorage")
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                "Failed to load image from gallery.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
    }

    private fun dialogItems(title: String, itemsList: List<String>, selection: String) {
        mListDialog = Dialog(this)
        val dialogBinding: DialogListBinding = DialogListBinding.inflate(layoutInflater)
        mListDialog.setContentView(dialogBinding.root)

        dialogBinding.titleTextView.text = title

        val adapter = ListItemAdapter(this, null, itemsList, selection)
        dialogBinding.listRecyclerView.adapter = adapter
        mListDialog.show()
    }

    fun selectedListItem(item: String, selection: String) {
        when (selection) {
            Constants.DISH_TYPE -> {
                mListDialog.dismiss()
                binding.typeEditText.setText(item)
            }
            Constants.DISH_CATEGORY -> {
                mListDialog.dismiss()
                binding.categoryEditText.setText(item)
            }
            else -> {
                mListDialog.dismiss()
                binding.cookingTimeEditText.setText(item)
            }
        }
    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    companion object {
        private const val IMAGE_DIRECTORY = "FoodiesImages"
    }
}