package ie.wit.foraging.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.foraging.databinding.FragmentPlantDetailBinding
import ie.wit.foraging.ui.auth.LoggedInViewModel
import ie.wit.foraging.ui.foragingList.ForagingListViewModel
import timber.log.Timber

class PlantDetailFragment : Fragment() {

    private lateinit var plantDetailViewModel: PlantDetailViewModel
    private val args by navArgs<PlantDetailFragmentArgs>()
    private var _fragBinding: FragmentPlantDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val foragingListViewModel : ForagingListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentPlantDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        plantDetailViewModel = ViewModelProvider(this).get(PlantDetailViewModel::class.java)
        plantDetailViewModel.observablePlant.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editPlantButton.setOnClickListener {
            plantDetailViewModel.updatePlant(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.foragingid, fragBinding.foragingvm?.observablePlant!!.value!!)
            findNavController().navigateUp()
        }

//        val view = inflater.inflate(R.layout.fragment_plant_detail, container, false)

        Toast.makeText(context,"Plant ID Selected : ${args.foragingid}",Toast.LENGTH_LONG).show()

        return root
//        return inflater.inflate(R.layout.fragment_plant_detail, container, false)
    }

    private fun render() {
        fragBinding.editScientificName.setText("A Message")
        fragBinding.editCommonName.setText("")
        fragBinding.editScientificName.setText("")
        fragBinding.foragingvm = plantDetailViewModel
        Timber.i("Retrofit fragBinding.donationvm == $fragBinding.foragingvm")
    }

    override fun onResume() {
        super.onResume()
        plantDetailViewModel.getPlant(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.foragingid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}