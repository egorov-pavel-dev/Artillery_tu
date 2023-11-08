package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.R
import com.egorovfond.artillery.data.Enemy
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.EnemyActivity
import kotlinx.android.synthetic.main.card_target_list.view.*

class TargetRvAdapter: RecyclerView.Adapter<TargetRvAdapter.ViewHolder>(){
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val presenter by lazy { Presenter.getPresenter() }

        fun bind(target: Enemy) = with(itemView) {
            target_list_textView_name.setText(target.nameTarget)
            target_list_button_edit.setOnClickListener {
                presenter.currentEnemy = target

                val intent = Intent(this.context, EnemyActivity::class.java)
                this.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetRvAdapter.ViewHolder {
        return TargetRvAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_target_list, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: TargetRvAdapter.ViewHolder, position: Int) {
        holder.bind(presenter.getTargetList()[position])
    }

    override fun getItemCount(): Int {
        return presenter.getTargetList().size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        notifyDataSetChanged()
    }
}