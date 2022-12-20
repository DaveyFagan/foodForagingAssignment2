package ie.wit.foraging.ui.foraging

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.foraging.R
import ie.wit.foraging.databinding.FragmentForagingBinding
import ie.wit.foraging.firebase.FirebaseImageManager.uploadImageToFirebase
import ie.wit.foraging.models.ForagingModel
import ie.wit.foraging.ui.auth.LoggedInViewModel
import ie.wit.foraging.ui.detail.PlantDetailViewModel
import ie.wit.foraging.utils.customTransformation
import ie.wit.foraging.utils.readImageUri
import ie.wit.foraging.utils.showImagePicker
import timber.log.Timber
import java.util.*
import com.squareup.picasso.Target
import ie.wit.foraging.models.LocationModel

class ForagingFragment : Fragment() {
    private var _fragBinding: FragmentForagingBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var foragingViewModel: ForagingViewModel
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>
    val imageid = getRandomString(20)
    var imageUriString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _fragBinding = FragmentForagingBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_foraging)
        setupMenu()

        foragingViewModel =
                ViewModelProvider(this).get(ForagingViewModel::class.java)
        foragingViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })
        setDateListener(fragBinding)
        setButtonListener(fragBinding)
        setPhotoListener(fragBinding)
        setLocationListener(fragBinding, LocationModel())
        registerImagePickerCallback()
        return root;
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_foraging_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.foragingError),Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ForagingFragment().apply {
                    arguments = Bundle().apply {}
                }
    }

    fun setButtonListener(foragingLayout: FragmentForagingBinding) {
        foragingLayout.foragingButton.setOnClickListener {
            val commonPlantName = foragingLayout.commonPlantName.text.toString()
            val scientificPlantName = foragingLayout.scientificPlantName.text.toString()
            val datePlantPicked = foragingLayout.datePlantPicked.text.toString()
            val image = foragingLayout.foragingImage


            if (commonPlantName.isEmpty() or scientificPlantName.isEmpty() or datePlantPicked.isEmpty()) {
                Snackbar
                    .make(it, R.string.enter_all_fields, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                Timber.i("Adding Foraged Food")
                foragingViewModel.addForaging(
                        loggedInViewModel.liveFirebaseUser, ForagingModel(
                            commonPlantName = commonPlantName,
                            scientificPlantName = scientificPlantName,
                            datePlantPicked = datePlantPicked,
                            image = imageUriString,
                            email = loggedInViewModel.liveFirebaseUser.value?.email!!
                        )
                    )
                fragBinding.commonPlantName.text = null
                fragBinding.scientificPlantName.text = null
                fragBinding.datePlantPicked.text = null
            }
        }
    }

    fun setDateListener(foragingLayout: FragmentForagingBinding) {
        foragingLayout.datePlantPicked.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(

                requireContext(), R.style.DialogTheme,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    foragingLayout.datePlantPicked.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    fun setLocationListener(foragingLayout: FragmentForagingBinding, location: LocationModel) {
        foragingLayout.foragingLocation.setOnClickListener {
            Timber.i("Set Location Pressed")
//            val originalLocation =
//                LocationModel(53.45728574193019, -6.238869520651969, 15f)
//
//            if (location.zoom != 0f) {
//                location.lat = location.lat
//                location.lng = location.lng
//                location.zoom = location.zoom
//            }
//            val launcherIntent = Intent(context, Map::class.java)
//                .putExtra("location", originalLocation)
//            mapIntentLauncher.launch(launcherIntent)

        }
    }

    fun setPhotoListener(foragingLayout: FragmentForagingBinding) {
        foragingLayout.chooseImage.setOnClickListener {
            Timber.i("Select image")
            showImagePicker(intentLauncher)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        fragBinding.commonPlantName.text = fragBinding.commonPlantName.text
        fragBinding.scientificPlantName.text = fragBinding.scientificPlantName.text
        fragBinding.datePlantPicked.text = fragBinding.datePlantPicked.text
    }

    private fun registerImagePickerCallback(): String {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("FX registerPickerCallback() ${readImageUri(result.resultCode, result.data).toString()}")
//                            fragBinding.foragingImage.setImageURI(readImageUri(result.resultCode, result.data))
                            imageUriString = result.data!!.data!!.toString()
                            Picasso.get().load(readImageUri(result.resultCode, result.data))
                                .resize(200, 200)
                                .transform(customTransformation())
//                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .centerCrop()
                                .into(object : Target {
                                    override fun onBitmapLoaded(bitmap: Bitmap?,
                                                                from: Picasso.LoadedFrom?
                                    ) {
                                        Timber.i("FX onBitmapLoaded $bitmap")
                                        uploadImageToFirebase(imageid, bitmap!!,false)
                                        fragBinding.foragingImage.setImageURI(result.data!!.data!!)
                                    }

                                    override fun onBitmapFailed(e: java.lang.Exception?,
                                                                errorDrawable: Drawable?) {
                                        Timber.i("FX onBitmapFailed $e")
                                    }

                                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                                })
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }

            }
        return imageUriString
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}