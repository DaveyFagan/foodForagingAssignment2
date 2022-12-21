package ie.wit.foraging.adapters

import android.net.Uri
import android.net.Uri.decode
import android.util.Base64.decode
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.Base64Utils.decode
import com.squareup.picasso.Picasso
import ie.wit.foraging.R
import ie.wit.foraging.databinding.CardForagingBinding
import ie.wit.foraging.models.ForagingModel
import ie.wit.foraging.utils.customTransformation
import java.lang.Byte.decode
import java.lang.Integer.decode
import java.util.*
import kotlin.collections.ArrayList

interface ForagingClickListener {
    fun onForagingClick(foraging: ForagingModel)
}

class ForagingAdapter constructor(private var foragingList: ArrayList<ForagingModel>,
                                  private val listener: ForagingClickListener,
                                  private val readOnly: Boolean,)
    : RecyclerView.Adapter<ForagingAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardForagingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val foraging = foragingList[holder.adapterPosition]
        holder.bind(foraging, listener)

    }

    override fun getItemCount(): Int = foragingList.size

    fun removeAt(position: Int) {
        foragingList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class MainHolder(val binding : CardForagingBinding, private val readOnly : Boolean) : RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(foraging: ForagingModel, listener: ForagingClickListener) {
            binding.foragingPlantNameTitle.text = foraging.commonPlantName
            binding.foragingPlantScientificName.text = foraging.scientificPlantName
            binding.datePlantPicked.text = foraging.datePlantPicked
            Picasso.get().load(foraging.image.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.root.tag = foraging
            binding.root.setOnClickListener {
                listener.onForagingClick(foraging)

            }
        }
    }
}