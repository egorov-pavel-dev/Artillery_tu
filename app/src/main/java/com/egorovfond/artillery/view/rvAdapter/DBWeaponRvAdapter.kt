package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.R
import com.egorovfond.artillery.database.room.entity.WeaponEntity
import com.egorovfond.artillery.presenter.Presenter
import kotlinx.android.synthetic.main.card_weapon_list.view.*

class DBWeaponRvAdapter: RecyclerView.Adapter<DBWeaponRvAdapter.ViewHolder>() {
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val presenter by lazy { Presenter.getPresenter() }

        fun bind(weapon: WeaponEntity) = with(itemView) {
            textView_orudie_about.setText("${weapon.name}  mil: ${weapon.mil}")

            button_orudie_edit.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_weapon_list, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(presenter.weaponlist[position])
    }

    override fun getItemCount(): Int {
        return presenter.weaponlist.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        notifyDataSetChanged()
    }
}