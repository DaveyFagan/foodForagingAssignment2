package ie.wit.foraging.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.foraging.R
import ie.wit.foraging.databinding.CardForagingBinding
import ie.wit.foraging.models.ForagingModel

interface ForagingClickListener {
    fun onForagingClick(foraging: ForagingModel)
}


class ForagingAdapter constructor(private var foragingList: List<ForagingModel>,
                                  private val listener: ForagingClickListener)
    : RecyclerView.Adapter<ForagingAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardForagingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val foraging = foragingList[holder.adapterPosition]
        holder.bind(foraging, listener)
    }

    override fun getItemCount(): Int = foragingList.size

    inner class MainHolder(val binding : CardForagingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(foraging: ForagingModel, listener: ForagingClickListener) {
            binding.foragingPlantNameTitle.text = foraging.commonPlantName
            binding.foragingPlantScientificName.text = foraging.scientificPlantName
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            binding.root.setOnClickListener { listener.onForagingClick(foraging) }
        }
    }
}