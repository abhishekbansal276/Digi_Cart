package com.example.project_one

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_one.databinding.ActivityMyCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.R

class MyCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCartBinding

    var allProducts:String = ""
    var totalPrice:String = ""
    var totalWeight:String = ""

    val networkChangeListener = NetworlChangeListener()

    private lateinit var list: ArrayList<AllCartModel>
    private lateinit var adapter: AllCartAdapter
    lateinit var mAuth: FirebaseAuth
    lateinit var dialog:MyProgressBar

    lateinit var builder: AlertDialog.Builder
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        dialog = MyProgressBar("Wait...", this)

        dialog.show()
        database = FirebaseDatabase.getInstance()

        val databaseRef = database.reference.child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("cartItems")

        binding.recyclerView1.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        list = ArrayList()

        adapter = AllCartAdapter(this)
        binding.recyclerView1.adapter = adapter

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                adapter.clear()
                dialog.dismiss()
                for (ds in dataSnapshot.child("").children) {
                    val allProducts = ds.child("allProducts").value.toString()
                    val totalPrice = ds.child("totalPrice").value.toString()
                    val totalWeight = ds.child("totalWeight").value.toString()
                    val payOrNot = ds.child("paidOrNot").value.toString()

                    Log.d("Key", ds.key.toString())
                    Log.d("allProducts", allProducts)
                    Log.d("totalPrice", totalPrice)
                    Log.d("totalWeight", totalWeight)
                    Log.d("paidOrNot", payOrNot)
                    Log.d("id", ds.key.toString())

                    adapter.add(AllCartModel(allProducts, totalPrice, totalWeight, payOrNot, ds.key.toString()))
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", "Read failed")
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainHomeScreen::class.java))
    }

    override fun onStart() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, filter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }
}