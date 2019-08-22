package i.kuster.ui.episodes.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import i.kuster.BuildConfig
import i.kuster.R
import i.kuster.data.model.EpisodePost
import i.kuster.ui.episodes.EpisodesActivity.Companion.SHOW_ID
import i.kuster.ui.login.LoginActivity
import i.kuster.ui.shared.Validations
import i.kuster.ui.shared.onTextChanged
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.number_picker.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

const val PERMISSION_CAMERA_AND_WRITE_EXT = 213
const val PERMISSION_READ_EXT_STORAGE = 4444
const val REQUEST_IMAGE_CAMERA = 44
const val REQUEST_IMAGE_EXT_STORAGE = 45
const val SEASON_EPISODE_WITH_ZERO = 10
const val SEASON_PICK_MAX = 20
const val EPISODE_PICK_MAX = 99
const val SEASON_EPISODE_PICK_MIN = 1
const val PHOTO_URI = "IMAGE_SAVED_ON_ROTATE"
const val IMAGE="IMAGE_UPLOAD"

class AddEpisodeActivity : AppCompatActivity() {
    private lateinit var viewModelAddEp: AddEpisodeViewModel
    private lateinit var viewModelUploadEpImg: MediaViewModel

    companion object {
        fun newInstance(context: Context, showId: String): Intent {
            val intent = Intent(context, AddEpisodeActivity::class.java)
            intent.putExtra(SHOW_ID, showId)
            return intent
        }
    }

    private fun changeView() {
        addPhoto.visibility = View.INVISIBLE
        viewPhoto.visibility = View.VISIBLE
    }

    var seasonNumber: String = "1"
    var episodeNumber: String = "1"
    fun createDialog() {
        val numPickDialogView = LayoutInflater.from(this).inflate(R.layout.number_picker, null)
        val builder = AlertDialog.Builder(this).setView(numPickDialogView).show()
        with(numPickDialogView) {
            seasonPicker.maxValue = SEASON_PICK_MAX
            seasonPicker.minValue = SEASON_EPISODE_PICK_MIN
            episodePicker.minValue = SEASON_EPISODE_PICK_MIN
            episodePicker.maxValue = EPISODE_PICK_MAX
        }
        numPickDialogView.seasonPicker.value = seasonNumber.toInt()
        numPickDialogView.episodePicker.value = episodeNumber.toInt()
        numPickDialogView.saveNumberPicker.setOnClickListener {
            builder.dismiss()
            seasonNumber = numPickDialogView.seasonPicker.value.toString()
            episodeNumber = numPickDialogView.episodePicker.value.toString()
            seasonEpisodePicker.text = getString(R.string.season_episode, seasonNumber, episodeNumber)
        }
    }

    private fun showAddPictureChoiceDialog() {
        val choice = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setItems(choice) { _, i ->
            when (i) {
                0 -> givePermissionCameraAndWriteExt()
                1 -> givePermissionExtStorageRead()
            }
        }.show()


    }

    fun showDialogOnBackPress() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert").setMessage("Do you really want to leave this screen?")
            .setNegativeButton("No") { _, _ -> }
            .setPositiveButton("Yes") { _, _ ->
                viewModelUploadEpImg.restartData()
                viewModelAddEp.restartTokenCondition()
                finish()
            }.show()
    }

    fun showDialogOnError(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert").setMessage(message)
            .setNegativeButton("Cancel") { _, _ ->
            }
            .setPositiveButton("Yes") { _, _ ->
                finish()
                viewModelAddEp.logOut()
                startActivity(LoginActivity.newInstance(this))
            }.show()
    }

    var pictureFile: File = File("")
    private fun createFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var name = timeStamp
        pictureFile = File(storageDir?.toString(), name + ".jpg")
        return pictureFile

    }

    private var photoUri: Uri? = null
    private fun takeCameraImageIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val pictureFile = createFile()
        pictureFile.also {
            photoUri = FileProvider.getUriForFile(this, "com." + BuildConfig.APPLICATION_ID + ".fileprovider", it)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, REQUEST_IMAGE_CAMERA)
        }
    }

    private fun takeGalleryImageIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUEST_IMAGE_EXT_STORAGE)
    }

    private fun givePermissionCameraAndWriteExt() {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission")
                    .setMessage("We need your permission to use camera, then you can take picture and add it to episode")
                    .setPositiveButton("OK, I GOT IT") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this, permissions,
                            PERMISSION_CAMERA_AND_WRITE_EXT
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(
                    this, permissions,
                    PERMISSION_CAMERA_AND_WRITE_EXT
                )
            }
        } else {
            takeCameraImageIntent()
        }
    }

    private fun givePermissionExtStorageRead() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Permission")
                    .setMessage("We need your permission to open gallery, then you can choose picture and add it to episode")
                    .setPositiveButton("OK,I GOT IT") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            PERMISSION_READ_EXT_STORAGE
                        )
                    }.show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_READ_EXT_STORAGE
                )
            }
        } else {
            takeGalleryImageIntent()
        }
    }

    fun setStateOfSaveEpBtn(): Pair<String, String> {
        val (allowedTitle, messageTitle) = Validations.isEpisodeTitleInputValid(episodeTitleInput.text.toString())
        val (allowedDescription, messageDescription) = Validations.isEpisodeDescriptionInputValid(
            episodeDescriptionInput.text.toString()
        )
        btnSaveEpisode.isEnabled = allowedTitle && allowedDescription
        return Pair(messageTitle, messageDescription)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)
        val toolbar: Toolbar = findViewById(R.id.toolbarAddEpisode)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = "Add episode"
        if (savedInstanceState != null) {
            photoUri = savedInstanceState.getParcelable(PHOTO_URI)
            var photoRealPath=savedInstanceState.getString(IMAGE)
            if (photoUri != null) {
                changeView()
                selectedPhoto.setImageURI(photoUri)
                pictureFile=File(photoRealPath)
            }
        }
        viewModelAddEp = ViewModelProviders.of(this).get(AddEpisodeViewModel::class.java)
        viewModelUploadEpImg = ViewModelProviders.of(this).get(MediaViewModel::class.java)
        val showId = intent.getStringExtra(SHOW_ID)
        viewModelAddEp.liveData.observe(this, Observer { token ->
            if (token != null) {
                if (token.isSuccessful == false) {
                    showDialogOnError(token.message.toString())
                    showLayoutWithoutPhoto()
                } else if (token.isSuccessful == null) {
                    Toast.makeText(this, token.message, Toast.LENGTH_LONG).show()
                    showLayoutWithoutPhoto()
                } else {
                    finish()
                    viewModelAddEp.restartTokenCondition()
                }
            }
        })
        viewModelUploadEpImg.liveData.observe(this, Observer { media ->
            if (media != null) {
                if (media.isSuccessful == true) {
                    val episode = EpisodePost(
                        showId,
                        media.media?.mediaId,
                        episodeTitleInput.text.toString(),
                        episodeDescriptionInput.text.toString(),
                        episodeNumber,
                        seasonNumber
                    )
                    viewModelAddEp.addEpisode(episode)
                    viewModelUploadEpImg.restartData()
                } else if (media.isSuccessful == null) {
                    showLayoutWithPhoto()
                    Toast.makeText(this, media.message, Toast.LENGTH_LONG).show()
                } else if (media.isSuccessful == false) {
                    showLayoutWithPhoto()
                    showDialogOnError(media.message.toString())
                }
            }
        })
        btnSaveEpisode.setOnClickListener {
            if(addPhoto.visibility==View.INVISIBLE){
                viewModelUploadEpImg.uploadPhoto(pictureFile)
                addPhotoInputs.visibility=View.GONE
                viewPhoto.visibility=View.INVISIBLE
                btnSaveEpisode.visibility=View.GONE
                loadingScreen.visibility = View.VISIBLE
            }
            else{
                val episode = EpisodePost(
                    showId,
                    "",
                    episodeTitleInput.text.toString(),
                    episodeDescriptionInput.text.toString(),
                    episodeNumber,
                    seasonNumber
                )
                viewModelAddEp.addEpisode(episode)
                addPhotoInputs.visibility=View.GONE
                addPhoto.visibility=View.GONE
                btnSaveEpisode.visibility=View.GONE
                loadingScreen.visibility = View.VISIBLE

            }
        }
        episodeTitleInput.onTextChanged {
            val messageTitle = setStateOfSaveEpBtn().first
            if(episodeTitleInput.hasFocus()){
                episodeTitleLayout.error = messageTitle
            }
        }
        episodeDescriptionInput.onTextChanged {
            val messageDescription = setStateOfSaveEpBtn().second
            if(episodeDescriptionInput.hasFocus()){
                episodeDescriptionLayout.error = messageDescription
            }
        }
        addPhoto.setOnClickListener {
            showAddPictureChoiceDialog()
        }
        viewPhoto.setOnClickListener {
            showAddPictureChoiceDialog()
        }
        seasonEpisodePicker.text = getString(R.string.season_episode, "01", "01")
        seasonEpisodePicker.setOnClickListener {
            createDialog()
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun showLayoutWithPhoto() {
        loadingScreen.visibility = View.GONE
        addPhotoInputs.visibility = View.VISIBLE
        viewPhoto.visibility = View.VISIBLE
        btnSaveEpisode.visibility=View.VISIBLE
    }

    private fun showLayoutWithoutPhoto() {
        loadingScreen.visibility = View.GONE
        addPhotoInputs.visibility = View.VISIBLE
        addPhoto.visibility = View.VISIBLE
        btnSaveEpisode.visibility=View.VISIBLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMERA_AND_WRITE_EXT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takeCameraImageIntent()
                }

            }
            PERMISSION_READ_EXT_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    takeGalleryImageIntent()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAMERA) {
                changeView()
                selectedPhoto.setImageURI(photoUri)

            } else if (requestCode == REQUEST_IMAGE_EXT_STORAGE) {
                changeView()
                if (data?.data != null) {
                    photoUri = data.data
                    pictureFile = File(getRealPath(photoUri))
                    selectedPhoto.setImageURI(photoUri)
                }
            }
        }
    }

    private fun getRealPath(uri: Uri?): String? {
        val filePath = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, filePath, null, null, null)
        assert(cursor != null)
        cursor?.moveToFirst()
        val index: Int? = cursor?.getColumnIndex(filePath[0])
        var path = ""
        if (index != null) {
            path = cursor.getString(index)
        }
        return path
    }

    override fun onBackPressed() {
        if (!episodeTitleInput.text.isNullOrBlank() || !episodeDescriptionInput.text.isNullOrBlank()) {
            showDialogOnBackPress()
        } else {
            viewModelUploadEpImg.restartData()
            viewModelAddEp.restartTokenCondition()
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (photoUri != null) {
            outState?.putParcelable(PHOTO_URI, photoUri)
            outState?.putString(IMAGE,pictureFile.path)
        }
    }
}