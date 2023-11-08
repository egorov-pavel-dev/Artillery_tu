package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.R
import com.egorovfond.artillery.presenter.Presenter
import kotlinx.android.synthetic.main.card_dbtable.view.*

class DBTableRvAdapter: RecyclerView.Adapter<DBTableRvAdapter.ViewHolder>() {
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val presenter by lazy { Presenter.getPresenter() }

        fun bind(position: Int) = with(itemView) {
            update(position)

            dbtable_rv_namebullet.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].bulet = ""
                    else presenter.currentTable[position].bulet = p0.toString().toString()
                }
            })
            dbtable_rv_mortire.setOnClickListener {
                presenter.currentTable[position].mortir = dbtable_rv_mortire.isChecked
            }
            dbtable_rv_distance.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].D = 0
                    else presenter.currentTable[position].D = p0.toString().toInt()
                }
            })
            dbtable_rv_pricel.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].II = 0
                    else presenter.currentTable[position].II = p0.toString().toInt()
                }
            })
            dbtable_rv_priel_distance.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].II_delt = 0f
                    else presenter.currentTable[position].II_delt = p0.toString().toFloat()
                }
            })
            dbtable_rv_priel_heigth.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].IIh_delt = 0f
                    else presenter.currentTable[position].IIh_delt = p0.toString().toFloat()
                }
            })
            dbtable_rv_divergenshion.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].Z = 0f
                    else presenter.currentTable[position].Z = p0.toString().toFloat()
                }
            })
            dbtable_rv_ywind.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].Zw_delt = 0f
                    else presenter.currentTable[position].Zw_delt = p0.toString().toFloat()
                }
            })
            dbtable_rv_xwind.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].Xw_delt = 0f
                    else presenter.currentTable[position].Xw_delt = p0.toString().toFloat()
                }
            })
            dbtable_rv_xtis.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].X_tis = 0f
                    else presenter.currentTable[position].X_tis = p0.toString().toFloat()
                }
            })
            dbtable_rv_temp.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].Xt_delt = 0f
                    else presenter.currentTable[position].Xt_delt = p0.toString().toFloat()
                }
            })
            dbtable_rv_hum.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].Xh_delt = 0f
                    else presenter.currentTable[position].Xh_delt = p0.toString().toFloat()
                }
            })
        }

        private fun update(position: Int)= with(itemView) {
            dbtable_rv_namebullet.setText(presenter.currentTable[position].bulet)
            dbtable_rv_mortire.isChecked = presenter.currentTable[position].mortir
            dbtable_rv_distance.setText(presenter.currentTable[position].D.toString())
            dbtable_rv_pricel.setText(presenter.currentTable[position].II.toString())
            dbtable_rv_priel_distance.setText(presenter.currentTable[position].II_delt.toInt().toString())
            dbtable_rv_priel_heigth.setText(presenter.currentTable[position].IIh_delt.toInt().toString())
            dbtable_rv_divergenshion.setText(presenter.currentTable[position].Z.toInt().toString())
            dbtable_rv_ywind.setText(presenter.currentTable[position].Zw_delt.toInt().toString())
            dbtable_rv_xwind.setText(presenter.currentTable[position].Xw_delt.toInt().toString())
            dbtable_rv_xtis.setText(presenter.currentTable[position].X_tis.toInt().toString())
            dbtable_rv_temp.setText(presenter.currentTable[position].Xt_delt.toInt().toString())
            dbtable_rv_hum.setText(presenter.currentTable[position].Xh_delt.toInt().toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBTableRvAdapter.ViewHolder {
        return DBTableRvAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_dbtable, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: DBTableRvAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return presenter.currentTable.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        notifyDataSetChanged()
    }
}