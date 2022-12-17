package ie.wit.foraging.ui.foragingList

import android.os.Bundle
import android.view.*
import android.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.foraging.R
import ie.wit.foraging.adapters.ForagingAdapter
import ie.wit.foraging.adapters.ForagingClickListener
import ie.wit.foraging.databinding.FragmentForagingListBinding
import ie.wit.foraging.models.ForagingModel
import ie.wit.foraging.ui.auth.LoggedInViewModel
import ie.wit.foraging.utils.*

class ForagingListFragment : Fragment(), ForagingClickListener {

//    lateinit var app: ForagingApp
    private var _fragBinding: FragmentForagingListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader: AlertDialog
    private val foragingListViewModel: ForagingListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentForagingListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
//        activity?.title = getString(R.string.action_foraging_list)
        setupMenu()
        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        fragBinding.fab.setOnClickListener {
            val action =
                ForagingListFragmentDirections.actionForagingListFragmentToForagingFragment()
            findNavController().navigate(action)
        }
        showLoader(loader, "Downloading Foraged Food")

//        foragingListViewModel =
//            ViewModelProvider(this).get(ForagingListViewModel::class.java)
        foragingListViewModel.observableForagingList.observe(
            viewLifecycleOwner,
            Observer { foragingList ->
                foragingList?.let {
                    render(foragingList as ArrayList<ForagingModel>)
                    hideLoader(loader)
                    checkSwipeRefresh()
                }
            })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Foraged Food")
                val adapter = fragBinding.recyclerView.adapter as ForagingAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                foragingListViewModel.delete(foragingListViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as ForagingModel).uid!!)

                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onForagingClick(viewHolder.itemView.tag as ForagingModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

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
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(foragingList: ArrayList<ForagingModel>) {
        fragBinding.recyclerView.adapter = ForagingAdapter(foragingList, this)
        if (foragingList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.foodNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.foodNotFound.visibility = View.GONE
        }
    }

    override fun onForagingClick(foraging: ForagingModel) {
        val action =
            ForagingListFragmentDirections.actionForagingListFragmentToPlantDetailFragment(foraging.uid!!)
        findNavController().navigate(action)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ForagingListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Foraged Food")
            foragingListViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Foraged Food")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                foragingListViewModel.liveFirebaseUser.value = firebaseUser
                foragingListViewModel.load()
            }
        })
        hideLoader(loader)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}