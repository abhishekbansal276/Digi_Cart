package com.example.project_one

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import papaya.`in`.sendmail.SendMail
import java.util.*

class AllCartAdapter(context: Context) : RecyclerView.Adapter<AllCartAdapter.MyViewHolder>() {
    private val mList: ArrayList<AllCartModel> = ArrayList()
    private val context: Context

    lateinit var builder: AlertDialog.Builder
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.allProducts.text = mList[position].getAllProducts()
        holder.totalWeight.text = mList[position].getTotalWeight()
        holder.totalPrice.text = mList[position].getTotalPrice()
        holder.payOrNot.text = mList[position].getPayOrNot()

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        if (mList[position].getPayOrNot() == "Unpaid") {
            holder.payOrNot.visibility = View.GONE
            holder.paymentBtn.visibility = View.VISIBLE
        } else {
            holder.payOrNot.visibility = View.VISIBLE
            holder.paymentBtn.visibility = View.GONE
        }

        holder.paymentBtn.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context, PaymentActivity::class.java))
        })

        builder = AlertDialog.Builder(context)

        val mail = SendMail(
            "carttracker74@gmail.com",
            "djswkyucnejaqjow",
            mAuth!!.currentUser!!.email.toString(),
            "Billing Info",
            " Product list \n ${mList[position].getAllProducts()}\n\n Capacity selected \n ${mList[position].getTotalWeight()}\n\n Total price \n ${mList[position].getTotalPrice()}\n\n\n\n Paid!"
        )

        Log.d("TAG", "onBindViewHolder: ${mAuth!!.currentUser!!.email.toString()}")

        holder.paymentBtn.setOnLongClickListener(OnLongClickListener {
            builder.setMessage("Is payment done?").setTitle("Payment").setCancelable(true)
                .setPositiveButton("Yes") { dialog, id ->
                    mail.execute();
                    Toast.makeText(
                        context, "Mail sent", Toast.LENGTH_SHORT
                    ).show()

                    updatingInfo(position)
                }.setNegativeButton(
                    "No"
                ) { dialog, id ->
                    dialog.cancel()
                    Toast.makeText(
                        context, "OK", Toast.LENGTH_SHORT
                    ).show()
                }
            val alert: AlertDialog = builder.create()
            alert.show()
            true
        })
    }

    private fun updatingInfo(position: Int) {
        val ref = database.reference.child("users").child(mAuth.uid.toString()).child("cartItems")
            .child(mList[position].getId().toString())
        Log.d("REF", "updatingInfo: $ref")
        ref.child("paidOrNot").setValue("Paid")
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun add(allCartModel: AllCartModel) {
        mList.add(allCartModel)
        notifyDataSetChanged()
    }

    fun clear() {
        mList.clear()
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var allProducts: TextView
        var totalWeight: TextView
        var totalPrice: TextView
        var payOrNot: TextView
        var paymentBtn: FloatingActionButton

        init {
            allProducts = itemView.findViewById(R.id.allProductsTV)
            totalWeight = itemView.findViewById(R.id.totalWeightTV)
            totalPrice = itemView.findViewById(R.id.totalPriceTV)
            payOrNot = itemView.findViewById(R.id.payOrNotTV)
            paymentBtn = itemView.findViewById(R.id.paymentBtn)
        }
    }
}