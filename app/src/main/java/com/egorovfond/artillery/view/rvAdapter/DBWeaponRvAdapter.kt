package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.database.room.entity.WeaponEntity
import com.egorovfond.artillery.databinding.CardWeaponListBinding
import com.egorovfond.artillery.presenter.Presenter

class DBWeaponRvAdapter: RecyclerView.Adapter<DBWeaponRvAdapter.ViewHolder>() {
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(itemView: CardWeaponListBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val binding: CardWeaponListBinding = itemView
        private val presenter by lazy { Presenter.getPresenter() }

        fun bind(weapon: WeaponEntity, binding: CardWeaponListBinding) = with(itemView) {
            binding.textViewOrudieAbout.setText("${weapon.name}  mil: ${weapon.mil}")

            binding.buttonOrudieEdit.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.card_weapon_list, parent, false) as View
            CardWeaponListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(presenter.weaponlist[position], holder.binding)
    }

    override fun getItemCount(): Int {
        return presenter.weaponlist.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        notifyDataSetChanged()
    }
}