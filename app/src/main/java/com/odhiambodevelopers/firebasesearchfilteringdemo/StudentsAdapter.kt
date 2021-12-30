package com.odhiambodevelopers.firebasesearchfilteringdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.odhiambodevelopers.firebasesearchfilteringdemo.databinding.StudentsRowBinding

class StudentsAdapter:ListAdapter<Students,StudentsAdapter.MyHolder>(COMPARATOR) {

    private object COMPARATOR:DiffUtil.ItemCallback<Students>(){
        override fun areItemsTheSame(oldItem: Students, newItem: Students): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Students, newItem: Students): Boolean {
            return oldItem.id == newItem.id
        }

    }
// An innerclass that maps data with the available views
    inner class MyHolder(private val binding: StudentsRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Students?) {
            binding.tvName.text = student?.name
            binding.tvRegNumber.text = student?.regno
            binding.tvAmount.text = student?.amount.toString()
            binding.tvAge.text = student?.age.toString()
            binding.tvGender.text = student?.gender.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(StudentsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val student = getItem(position)
        holder.bind(student)
    }

}