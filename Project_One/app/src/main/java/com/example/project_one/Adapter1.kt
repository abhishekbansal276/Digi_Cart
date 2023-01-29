package com.example.project_one

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Adapter1(private val listData: Array<Model1>, context: Context) :
    RecyclerView.Adapter<Adapter1.ViewHolder>() {
    var context: Context

    var allProducts = ""
    var totalPrice = 0.0
    var count = 0

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.item_list, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.imageView.setImageResource(listData[position].getImgId())
        holder.itemName.text = listData[position].getName()
        holder.itemPrice.text = listData[position].getPrice()

        val obj = Constantinfo()

        holder.addBtn.setOnClickListener(View.OnClickListener {
            if(count == obj.selectCapacity){
                Log.d("TAG", "onBindViewHolder: {$count}, {${obj.selectCapacity}}")
                Toast.makeText(context,"Capacity is full", Toast.LENGTH_SHORT).show()
            }
            else{
                allProducts += listData[position].getName() + ", "
                var str = listData[position].getPrice()
                totalPrice += str.substring(4).toDouble()
                count++

                obj.allProducts = allProducts
                obj.totalprice = totalPrice.toString()

                Log.d("TAG", "onBindViewHolder: ${obj.allProducts}, ${obj.totalprice}, ${obj.selectCapacity}")

                Toast.makeText(context,"Product Added", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView
        var addBtn: Button
        var imageView: ImageView
        var itemPrice: TextView

        init {
            itemName = itemView.findViewById(R.id.itemName) as TextView
            imageView = itemView.findViewById(R.id.imageView) as ImageView
            itemPrice = itemView.findViewById(R.id.itemPrice) as TextView
            addBtn = itemView.findViewById(R.id.addCartBtn)
        }
    }
}