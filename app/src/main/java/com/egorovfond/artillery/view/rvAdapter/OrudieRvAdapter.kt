package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.R
import com.egorovfond.artillery.data.Orudie
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.OrudieSettingsActivity
import kotlinx.android.synthetic.main.card_weapon_list.view.*

class OrudieRvAdapter() : RecyclerView.Adapter<OrudieRvAdapter.ViewHolder>(){
    private val presenter by lazy { Presenter.getPresenter() }
    private val data = mutableListOf<Orudie>()

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val presenter by lazy { Presenter.getPresenter() }

        fun bind(weapon: Orudie) = with(itemView) {
            textView_orudie_about.setText(weapon.nameOrudie)

            button_orudie_edit.setOnClickListener {
                presenter.setCurrentWeapon(weapon)

                /// Открываем форму настройки орудия
                val intent = Intent(context, OrudieSettingsActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_weapon_list, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
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