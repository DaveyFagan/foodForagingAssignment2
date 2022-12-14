package ie.wit.foraging.ui.foraging

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import ie.wit.foraging.R
import ie.wit.foraging.databinding.FragmentForagingBinding
import ie.wit.foraging.main.ForagingApp
import ie.wit.foraging.models.ForagingModel

class ForagingFragment : Fragment() {

    lateinit var app: ForagingApp
    private var _fragBinding: FragmentForagingBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    //lateinit var navController: NavController
    private lateinit var foragingViewModel: ForagingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

//        app = activity?.application as ForagingApp
//        setHasOptionsMenu(true)
//        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
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
//        val textView: TextView = root.findViewById(R.id.text_home)
//        foragingViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
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
        foragingLayout.foragingButton.setOnClickListener() {
            val commonPlantName = foragingLayout.commonPlantName.text.toString()
            val scientificPlantName = foragingLayout.scientificPlantName.text.toString()
            val datePlantPicked = foragingLayout.datePlantPicked.text.toString()

            if (commonPlantName.isEmpty() or scientificPlantName.isEmpty() or datePlantPicked.isEmpty()) {
                Snackbar
                    .make(it, R.string.enter_all_fields, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                foragingViewModel.addForaging(ForagingModel(
                    commonPlantName = commonPlantName,
                    scientificPlantName = scientificPlantName,
                    datePlantPicked = datePlantPicked
                ))
            }
        }
    }

//     override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_foraging_list, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item,
//            requireView().findNavController()) || super.onOptionsItemSelected(item)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }

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