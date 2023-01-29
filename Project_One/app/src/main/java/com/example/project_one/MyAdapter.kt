package com.example.project_one

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter (context: Context) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val mList: ArrayList<Model> = ArrayList()
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.new_item_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemName.text = mList[position].getItemName()
        holder.itemWeight.text = mList[position].getItemWeight()
        holder.itemPrice.text = mList[position].getItemPrice()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun add(model: Model){
        mList.add(model)
        notifyDataSetChanged()
    }

    fun clear(){
        mList.clear()
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView
        var itemWeight: TextView
        var itemPrice: TextView
        init {
            itemName = itemView.findViewById(R.id.itemName)
            itemWeight = itemView.findViewById(R.id.itemWeight)
            itemPrice = itemView.findViewById(R.id.itemPrice)
        }
    }
}