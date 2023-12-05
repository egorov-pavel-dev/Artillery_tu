package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.databinding.CardMapListBinding
import com.egorovfond.artillery.math.Map
import com.egorovfond.artillery.presenter.Presenter

class MapRvAdapter(): RecyclerView.Adapter<MapRvAdapter.ViewHolder>(){
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(binding_: CardMapListBinding) :
        RecyclerView.ViewHolder(binding_.root) {
        val binding: CardMapListBinding = binding_
        val globalSplitInstallManager = Presenter.getSplitInstallManager(binding_.root.context)

        fun bind(map: Map, binding: CardMapListBinding)= with(itemView) {
            map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)

            binding.mapRvIsloaded.setOnClickListener {
                loadMapGlobal(map)
            }

            update(map, binding)
        }

        private fun loadMapGlobal(map: Map) = with(itemView){
            val mapsInLoad = Presenter.getMapsLoad()

            if (mapsInLoad.find { it.equals(map.name) } != null){
                Toast.makeText(itemView.context, "Дождитесь окончания предыдущей установки", Toast.LENGTH_SHORT).show()
                return@with
            }

            Presenter.loadMap(map.name)

        }

        private fun update(map: Map, binding: CardMapListBinding) {
            binding.mapRvName.setText(map.name)
            binding.mapRvSize.setText("${Math.round(map.size/1024)} Kb")
            binding.mapRvIsloaded.isChecked = map.isLoaded
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapRvAdapter.ViewHolder {
        return ViewHolder(
            binding_ = CardMapListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MapRvAdapter.ViewHolder, position: Int) {
        holder.bind(presenter.maps[position], holder.binding)
    }

    override fun getItemCount(): Int {
        return presenter.maps.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        notifyDataSetChanged()
    }

}