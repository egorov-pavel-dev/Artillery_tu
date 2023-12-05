package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.databinding.CardDbtableBinding
import com.egorovfond.artillery.presenter.Presenter

class DBTableRvAdapter: RecyclerView.Adapter<DBTableRvAdapter.ViewHolder>() {
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(itemView: CardDbtableBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val binding: CardDbtableBinding = itemView
        private val presenter by lazy { Presenter.getPresenter() }

        fun bind(position: Int, binding: CardDbtableBinding) = with(itemView) {
            update(position, binding)

            binding.dbtableRvNamebullet.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].bulet = ""
                    else presenter.currentTable[position].bulet = p0.toString().toString()
                }
            })
            binding.dbtableRvMortire.setOnClickListener {
                presenter.currentTable[position].mortir = binding.dbtableRvMortire.isChecked
            }
            binding.dbtableRvDistance.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].D = 0
                    else presenter.currentTable[position].D = p0.toString().toInt()
                }
            })
            binding.dbtableRvPricel.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].II = 0
                    else presenter.currentTable[position].II = p0.toString().toInt()
                }
            })
            binding.dbtableRvPrielDistance.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].II_delt = 0f
                    else presenter.currentTable[position].II_delt = p0.toString().toFloat()
                }
            })
            binding.dbtableRvPrielHeigth.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].IIh_delt = 0f
                    else presenter.currentTable[position].IIh_delt = p0.toString().toFloat()
                }
            })
            binding.dbtableRvDivergenshion.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].Z = 0f
                    else presenter.currentTable[position].Z = p0.toString().toFloat()
                }
            })
            binding.dbtableRvYwind.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].Zw_delt = 0f
                    else presenter.currentTable[position].Zw_delt = p0.toString().toFloat()
                }
            })
            binding.dbtableRvXwind.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].Xw_delt = 0f
                    else presenter.currentTable[position].Xw_delt = p0.toString().toFloat()
                }
            })
            binding.dbtableRvXtis.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].X_tis = 0f
                    else presenter.currentTable[position].X_tis = p0.toString().toFloat()
                }
            })
            binding.dbtableRvTemp.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    if(p0.toString().isEmpty()) presenter.currentTable[position].Xt_delt = 0f
                    else presenter.currentTable[position].Xt_delt = p0.toString().toFloat()
                }
            })
            binding.dbtableRvHum.addTextChangedListener(object : TextWatcher {
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

        private fun update(position: Int, binding: CardDbtableBinding)= with(itemView) {
            binding.dbtableRvNamebullet.setText(presenter.currentTable[position].bulet)
            binding.dbtableRvMortire.isChecked = presenter.currentTable[position].mortir
            binding.dbtableRvDistance.setText(presenter.currentTable[position].D.toString())
            binding.dbtableRvPricel.setText(presenter.currentTable[position].II.toString())
            binding.dbtableRvPrielDistance.setText(presenter.currentTable[position].II_delt.toInt().toString())
            binding.dbtableRvPrielHeigth.setText(presenter.currentTable[position].IIh_delt.toInt().toString())
            binding.dbtableRvDivergenshion.setText(presenter.currentTable[position].Z.toInt().toString())
            binding.dbtableRvYwind.setText(presenter.currentTable[position].Zw_delt.toInt().toString())
            binding.dbtableRvXwind.setText(presenter.currentTable[position].Xw_delt.toInt().toString())
            binding.dbtableRvXtis.setText(presenter.currentTable[position].X_tis.toInt().toString())
            binding.dbtableRvTemp.setText(presenter.currentTable[position].Xt_delt.toInt().toString())
            binding.dbtableRvHum.setText(presenter.currentTable[position].Xh_delt.toInt().toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBTableRvAdapter.ViewHolder {
        return DBTableRvAdapter.ViewHolder(
            CardDbtableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.card_dbtable, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: DBTableRvAdapter.ViewHolder, position: Int) {
        holder.bind(position, holder.binding)
    }

    override fun getItemCount(): Int {
        return presenter.currentTable.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        notifyDataSetChanged()
    }
}