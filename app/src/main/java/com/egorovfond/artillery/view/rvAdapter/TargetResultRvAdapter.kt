package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.R
import com.egorovfond.artillery.data.Result
import com.egorovfond.artillery.presenter.Presenter
import kotlinx.android.synthetic.main.card_target_list_result.view.*

class TargetResultRvAdapter: RecyclerView.Adapter<TargetResultRvAdapter.ViewHolder>() {
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val TAG = "TargetResultRvAdapter"
        private val presenter by lazy { Presenter.getPresenter() }


        fun bind(position: Int) = with(itemView) {
            updateView(presenter.getResultAbout()[position])

            checkBox_mortire.setOnClickListener {
                presenter.changeMortirResultAbout(position, checkBox_mortire.isChecked)
            }

            checkBox_not_bussol.setOnClickListener {
                updateView(presenter.getResultAbout()[position])
            }
            btn_change_TH_bussol.setOnClickListener {
                try {
                    presenter.updateWeaponTH(checkBox_not_bussol.isChecked, presenter.getResultAbout()[position])
                }catch (e: Throwable){
                    Log.d(TAG, "bind: $e")
                }

//                text_list_item.setText("${presenter.getResultAbout()[position].orudie.nameOrudie} Снаряд: ${presenter.getResultAbout()[position].bullet}")
//                text_list_item_result.setText("Прицел: ${presenter.getResultAbout()[position].resultPricel} Угол: ${presenter.getResultAbout()[position].resultUgol}")
//                ed_bussol.setText(presenter.getResultAbout()[position].ugol.toString())

            }
            btn_change_TH_dot.setOnClickListener {
                try {
                    presenter.updateDot(checkBox_not_bussol.isChecked, presenter.getResultAbout()[position])
                    presenter.updateWeaponTH(checkBox_not_bussol.isChecked, presenter.getResultAbout()[position])
                }catch (e: Throwable){
                    Log.d(TAG, "bind: $e")
                }

//                text_list_item.setText("${presenter.getResultAbout()[position].orudie.nameOrudie} Снаряд: ${presenter.getResultAbout()[position].bullet}")
//                text_list_item_result.setText("Прицел: ${presenter.getResultAbout()[position].resultPricel} Угол: ${presenter.getResultAbout()[position].resultUgol}")
//                ed_bussol.setText(presenter.getResultAbout()[position].ugol.toString())

            }
            btn_change_TH_dot_prilet.setOnClickListener {
                try {
                    presenter.updateDotPrilet(presenter.getResultAbout()[position])
                    presenter.updateWeaponTH(checkBox_not_bussol.isChecked, presenter.getResultAbout()[position])
                }catch (e: Throwable){
                    Log.d(TAG, "bind: $e")
                }

//                text_list_item.setText("${presenter.getResultAbout()[position].orudie.nameOrudie} Снаряд: ${presenter.getResultAbout()[position].bullet}")
//                text_list_item_result.setText("Прицел: ${presenter.getResultAbout()[position].resultPricel} Угол: ${presenter.getResultAbout()[position].resultUgol}")
//                ed_bussol.setText(presenter.getResultAbout()[position].ugol.toString())

            }

            ed_bussol.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    synchronized(Presenter::class) {
                        if (p0.toString().isEmpty()) presenter.getResultAbout()[position].ugol = 0
                        else presenter.getResultAbout()[position].ugol = p0.toString().toInt()
                    }
                }
            })
            ed_enemy.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    synchronized(Presenter::class) {
                        if (p0.toString()
                                .isEmpty()
                        ) presenter.getResultAbout()[position].azimut_target =
                            0
                        else presenter.getResultAbout()[position].azimut_target =
                            p0.toString().toInt()
                    }
                }
            })
            ed_range.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
                    synchronized(Presenter::class) {
                        if (p0.toString().isEmpty()) presenter.getResultAbout()[position].distace =
                            0f
                        else presenter.getResultAbout()[position].distace = p0.toString().toFloat()
                    }
                }
            })
        }

        @SuppressLint("SetTextI18n")
        private fun updateView(resultAbout: Result) = with(itemView){
            text_list_item.setText("${resultAbout.orudie.nameOrudie} Снаряд: ${resultAbout.bullet}")
            if (resultAbout.resultPricel == -1){
                text_list_item_result.setText("Снаряд не подходит")
            }else     text_list_item_result.setText("Прицел: ${resultAbout.resultPricel} +/- ${resultAbout.deltaPricel} Угол: ${resultAbout.resultUgol} +/- ${resultAbout.deltaUgol} (${resultAbout.time}сек.)")
            ed_bussol.setText(resultAbout.ugol.toString())
            ed_enemy.setText(resultAbout.azimut_target.toString())
            ed_range.setText(resultAbout.distace.toString())
            checkBox_mortire.isChecked = resultAbout.orudie.mortir

            if (checkBox_not_bussol.isChecked) {
                ed_bussol_til.isEnabled = false
            }else{
                ed_bussol_til.isEnabled = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetResultRvAdapter.ViewHolder {
        return TargetResultRvAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_target_list_result, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: TargetResultRvAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return presenter.getResultAbout().size
    }
}