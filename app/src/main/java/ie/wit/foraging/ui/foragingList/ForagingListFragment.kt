package ie.wit.foraging.ui.foragingList

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.foraging.R
import ie.wit.foraging.adapters.ForagingAdapter
import ie.wit.foraging.adapters.ForagingClickListener
import ie.wit.foraging.databinding.FragmentForagingListBinding
import ie.wit.foraging.main.ForagingApp
import ie.wit.foraging.models.ForagingManager
import ie.wit.foraging.models.ForagingModel

class ForagingListFragment : Fragment(), ForagingClickListener {

    lateinit var app: ForagingApp
    private var _fragBinding: FragmentForagingListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var foragingListViewModel: ForagingListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        app = activity?.application as ForagingApp
//        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentForagingListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_foraging_list)
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        foragingListViewModel =
                ViewModelProvider(this).get(ForagingListViewModel::class.java)
        foragingListViewModel.observableDonationsList.observe(viewLifecycleOwner, Observer {
                foragingList ->
            foragingList?.let { render(foragingList) }
        })

//        val textView: TextView = root.findViewById(R.id.text_gallery)
//        foragingListViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//
        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ForagingListFragmentDirections.actionForagingListFragmentToForagingFragment()
            findNavController().navigate(action)
        }
        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_foraging, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(foragingList: List<ForagingModel>) {
        fragBinding.recyclerView.adapter = ForagingAdapter(ForagingManager.foragingList,this)
        if (foragingList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.foodNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.foodNotFound.visibility = View.GONE
        }
    }

    override fun onForagingClick(foraging: ForagingModel) {
        val action = ForagingListFragmentDirections.actionForagingListFragmentToPlantDetailFragment(foraging.id)
        findNavController().navigate(action)
    }

//   override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_foraging, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item,
//            requireView().findNavController()) || super.onOptionsItemSelected(item)
//    }

    companion object {
        @JvmStatic
        fun newInstance() =
                ForagingListFragment().apply {
                    arguments = Bundle().apply { }
                }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}