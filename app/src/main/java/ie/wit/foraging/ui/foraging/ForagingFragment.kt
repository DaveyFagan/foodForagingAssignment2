package ie.wit.foraging.ui.foraging

import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import ie.wit.foraging.R
import ie.wit.foraging.databinding.FragmentForagingBinding
import ie.wit.foraging.main.ForagingApp
import ie.wit.foraging.models.ForagingModel
import ie.wit.foraging.ui.auth.LoggedInViewModel
import timber.log.Timber

class ForagingFragment : Fragment() {
    private var _fragBinding: FragmentForagingBinding? = null
    private val fragBinding get() = _fragBinding!!

//    lateinit var app: ForagingApp
    private lateinit var foragingViewModel: ForagingViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

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

        setButtonListener(fragBinding)
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
                            email = loggedInViewModel.liveFirebaseUser.value?.email!!
                        )
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        fragBinding.commonPlantName.text = null
        fragBinding.scientificPlantName.text = null
        fragBinding.datePlantPicked.text = null
    }
}