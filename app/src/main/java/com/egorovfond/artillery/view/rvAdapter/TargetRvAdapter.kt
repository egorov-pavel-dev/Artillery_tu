package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.data.Enemy
import com.egorovfond.artillery.databinding.CardTargetListBinding
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.EnemyActivity

class TargetRvAdapter: RecyclerView.Adapter<TargetRvAdapter.ViewHolder>(){
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(itemView: CardTargetListBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val binding: CardTargetListBinding = itemView
        private val presenter by lazy { Presenter.getPresenter() }

        fun bind(target: Enemy, binding: CardTargetListBinding) = with(itemView) {
            binding.targetListTextViewName.setText(target.nameTarget)
            binding.targetListButtonEdit.setOnClickListener {
                presenter.currentEnemy = target

                val intent = Intent(this.context, EnemyActivity::class.java)
                this.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetRvAdapter.ViewHolder {
//        return TargetRvAdapter.ViewHolder(
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.card_target_list, parent, false) as View
//        )
        return ViewHolder(CardTargetListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TargetRvAdapter.ViewHolder, position: Int) {
        holder.bind(presenter.getTargetList()[position], holder.binding)
    }

    override fun getItemCount(): Int {
        return presenter.getTargetList().size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        notifyDataSetChanged()
    }
}