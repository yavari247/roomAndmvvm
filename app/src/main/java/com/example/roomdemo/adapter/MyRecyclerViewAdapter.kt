package com.example.roomdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.R
import com.example.roomdemo.databinding.ListItemBinding
import com.example.roomdemo.db.local.Subscriber
/*onclick is a highered order function its input is an object of subscriber data class*/
class MyRecyclerViewAdapter(private val onClick: (Subscriber) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    private val subscriberList= ArrayList<Subscriber>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //pass higher ordered function on click
        holder.bind(subscriberList[position],onClick)
    }
    //everytime clear older list and add updated list to it from mainactivity
    //it make recyclerview run faster
    fun setList(subscribers:List<Subscriber>){
        subscriberList.clear()
        subscriberList.addAll(subscribers)
    }

    override fun getItemCount(): Int {
        return subscriberList.size

    }
}

class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(subscriber: Subscriber, onClick: (Subscriber) -> Unit) {
        binding.nameTextView.text = subscriber.name
        binding.emailTextView.text = subscriber.email

        binding.apply {
            listItemLayout.setOnClickListener {
                /*call higher order function to run in l=onclicklistener*/
                onClick(subscriber)
            }
        }

    }

}