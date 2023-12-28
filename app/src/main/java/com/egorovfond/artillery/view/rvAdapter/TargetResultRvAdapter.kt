package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.data.Result
import com.egorovfond.artillery.databinding.CardTargetListResultBinding
import com.egorovfond.artillery.presenter.Presenter

class TargetResultRvAdapter: RecyclerView.Adapter<TargetResultRvAdapter.ViewHolder>() {
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(itemView: CardTargetListResultBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val binding: CardTargetListResultBinding = itemView
        private val TAG = "TargetResultRvAdapter"
        private val presenter by lazy { Presenter.getPresenter() }


        fun bind(position: Int, binding: CardTargetListResultBinding) = with(itemView) {
            //binding.checkBoxNotBussol.isChecked= true

            updateView(presenter.getResultAbout()[position], binding)

            binding.checkBoxMortire.setOnClickListener {
                presenter.changeMortirResultAbout(position, binding.checkBoxMortire.isChecked)
                if(presenter.autoupdate) updateResult(binding, position)
            }

            binding.checkBoxNotBussol.setOnClickListener {
                updateView(presenter.getResultAbout()[position], binding)
            }
            binding.btnChangeTHBussol.setOnClickListener {
                updateResult(binding, position)
            }
            binding.btnChangeTHDot.setOnClickListener {
                try {
                    presenter.updateDot(binding.btnChangeTHDot.isChecked, presenter.getResultAbout()[position])
                    //presenter.updateWeaponTH(checkBox_not_bussol.isChecked, presenter.getResultAbout()[position])
                }catch (e: Throwable){
                    Log.d(TAG, "bind: $e")
                }

                if(presenter.autoupdate) updateResult(binding, position)
            }
            binding.btnChangeTHDotPrilet.setOnClickListener {
                try {
                    presenter.updateDotPrilet(presenter.getResultAbout()[position])
                    //presenter.updateWeaponTH(checkBox_not_bussol.isChecked, presenter.getResultAbout()[position])
                }catch (e: Throwable){
                    Log.d(TAG, "bind: $e")
                }

                if(presenter.autoupdate) updateResult(binding, position)
            }

            binding.edBussol.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
                override fun afterTextChanged(p0: Editable?) {
//                    synchronized(Presenter::class) {
//                        if (p0.toString().isEmpty()) presenter.getResultAbout()[position].ugol = 0
//                        else presenter.getResultAbout()[position].ugol = p0.toString().toInt()
//                    }
                    if(presenter.autoupdate) updateResult(binding, position)
                }
            })
            binding.edEnemy.addTextChangedListener(object : TextWatcher {
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

                    if(presenter.autoupdate) updateResult(binding, position)
                }
            })
            binding.edRange.addTextChangedListener(object : TextWatcher {
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

                    if(presenter.autoupdate) updateResult(binding, position)
                }
            })
        }

        private fun updateResult(
            binding: CardTargetListResultBinding,
            position: Int
        ) {
            try {
                if (!binding.checkBoxNotBussol.isChecked) {
                    presenter.getResultAbout()[position].ugol =
                        binding.edBussol.text.toString().toInt()
                }
                presenter.updateWeaponTH(
                    binding.checkBoxNotBussol.isChecked,
                    presenter.getResultAbout()[position]
                )
            } catch (e: Throwable) {
                Log.d(TAG, "bind: $e")
            }
        }

        @SuppressLint("SetTextI18n")
        private fun updateView(resultAbout: Result, binding: CardTargetListResultBinding) = with(itemView){
            binding.textListItem.setText("${resultAbout.orudie.nameOrudie} Снаряд: ${resultAbout.bullet}")
            if (resultAbout.resultPricel == -1){
                binding.textListItemResult.setText("Снаряд не подходит")
            }else  binding.textListItemResult.setText("Прицел: ${resultAbout.resultPricel} +/- ${resultAbout.deltaPricel} Угол: ${resultAbout.resultUgol} +/- ${resultAbout.deltaUgol} (${resultAbout.time}сек.)")
            if (binding.edBussol.text.toString() != resultAbout.ugol.toString()) {
                binding.edBussol.setText(resultAbout.ugol.toString())
            }
            if (binding.edEnemy.text.toString() != resultAbout.azimut_target.toString()) {
                binding.edEnemy.setText(resultAbout.azimut_target.toString())
            }
            if (binding.edRange.text.toString() != resultAbout.distace.toString()) {
                binding.edRange.setText(resultAbout.distace.toString())
            }

            if (binding.checkBoxMortire.isChecked != resultAbout.orudie.mortir) {
                binding.checkBoxMortire.isChecked = resultAbout.orudie.mortir
            }

            if (binding.checkBoxNotBussol.isChecked) {
                binding.edBussolTil.isEnabled = false
            }else{
                binding.edBussolTil.isEnabled = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetResultRvAdapter.ViewHolder {
        return ViewHolder(CardTargetListResultBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TargetResultRvAdapter.ViewHolder, position: Int) {
        holder.bind(position, holder.binding)
    }

    override fun getItemCount(): Int {
        return presenter.getResultAbout().size
    }
}