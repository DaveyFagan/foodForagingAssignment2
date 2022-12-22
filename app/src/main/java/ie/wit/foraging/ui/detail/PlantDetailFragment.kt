package ie.wit.foraging.ui.detail

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.foraging.R
import ie.wit.foraging.databinding.FragmentForagingBinding
import ie.wit.foraging.databinding.FragmentPlantDetailBinding
import ie.wit.foraging.ui.auth.LoggedInViewModel
import ie.wit.foraging.ui.foragingList.ForagingListViewModel
import timber.log.Timber
import java.util.*

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

        setDateListener()

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
        fragBinding.editDatePlantPicked.setText("")
        fragBinding.foragingvm = plantDetailViewModel
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

    fun setDateListener() {
        fragBinding.editDatePlantPicked.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(

                requireContext(), R.style.DialogTheme,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    fragBinding.editDatePlantPicked.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

}