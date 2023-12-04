package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.data.Orudie
import com.egorovfond.artillery.databinding.CardWeaponListBinding
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.OrudieSettingsActivity

class OrudieRvAdapter() : RecyclerView.Adapter<OrudieRvAdapter.ViewHolder>(){
    private val presenter by lazy { Presenter.getPresenter() }
    private val data = mutableListOf<Orudie>()

    class ViewHolder(itemView: CardWeaponListBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val binding: CardWeaponListBinding = itemView
        private val presenter by lazy { Presenter.getPresenter() }

        fun bind(weapon: Orudie, binding: CardWeaponListBinding) = with(itemView) {
            binding.textViewOrudieAbout.setText(weapon.nameOrudie)

            binding.buttonOrudieEdit.setOnClickListener {
                presenter.setCurrentWeapon(weapon)

                /// Открываем форму настройки орудия
                val intent = Intent(context, OrudieSettingsActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.card_weapon_list, parent, false) as View
//        )
        return ViewHolder(CardWeaponListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], holder.binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        this.data.clear()
        this.data.addAll(presenter.getWeapon())

        notifyDataSetChanged()
    }
}