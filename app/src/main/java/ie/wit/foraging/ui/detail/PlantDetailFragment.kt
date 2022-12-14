package ie.wit.foraging.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import ie.wit.foraging.R

class PlantDetailFragment : Fragment() {

    companion object {
        fun newInstance() = PlantDetailFragment()
    }

    private lateinit var viewModel: PlantDetailViewModel
    private val args by navArgs<PlantDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plant_detail, container, false)

        Toast.makeText(context,"Plant ID Selected : ${args.foragingid}",Toast.LENGTH_LONG).show()

        return view
//        return inflater.inflate(R.layout.fragment_plant_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlantDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}