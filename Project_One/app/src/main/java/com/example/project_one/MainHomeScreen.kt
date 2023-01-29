package com.example.project_one

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_one.databinding.ActivityHomeScreenBinding
import com.example.project_one.databinding.ActivityMainHomeScreenBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MainHomeScreen : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainHomeScreenBinding
    private lateinit var mAuth: FirebaseAuth
    lateinit var builder: AlertDialog.Builder

    lateinit var myDialog: Dialog

    var capacitySelected = 0

    lateinit var progressBar: MyProgressBar
    private lateinit var database: FirebaseDatabase

    private var capacity = arrayOf(
        1,2,5,10,15
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        progressBar = MyProgressBar("Wait...", this)

        binding.selectCapacity.onItemSelectedListener = this
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            capacity
        )
        ad.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )
        binding.selectCapacity.adapter = ad

        myDialog = Dialog(this)
        myDialog.setContentView(R.layout.view_cart_layout)
        myDialog.setCancelable(false)
        myDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        binding.viewCurrentCart.setOnClickListener(View.OnClickListener {
            myDialog.show()

            val obj = Constantinfo()

            val p = myDialog.findViewById<TextView>(R.id.totalProductsTV)
            val pr = myDialog.findViewById<TextView>(R.id.totalPriceTV)
            val iC = myDialog.findViewById<TextView>(R.id.totalItemsTV)

            p.text = "All products - " + obj.allProducts
            pr.text = "Total price - " + obj.totalprice.toString() + " ₹"
            iC.text = "selected capacity - " + obj.selectCapacity.toString()

            val doneBtn = myDialog.findViewById<FloatingActionButton>(R.id.doneBtn)

            doneBtn.setOnClickListener(View.OnClickListener {
                if(obj.totalprice == ""){
                    Toast.makeText(this, "Please add items", Toast.LENGTH_SHORT).show()
                    myDialog.dismiss()
                }
                else{
                    progressBar.show()
                    myDialog.dismiss()
                    val items = AddCartItem(
                        obj.allProducts, obj.selectCapacity.toString(), obj.totalprice.toString(), "Unpaid"
                    )

                    obj.allProducts = ""
                    obj.totalprice = ""
                    obj.selectCapacity = 0

                    var childTime = Calendar.getInstance().time.toString()
                    childTime = childTime.replace("\\s".toRegex(), "")
                    childTime = childTime.replace(":", "")
                    childTime = childTime.replace("+", "")

                    val databaseRef = database.reference.child("users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).child("cartItems").child(childTime)

                    databaseRef.setValue(items).addOnCompleteListener {it1->
                        if (it1.isSuccessful) {
                            progressBar.dismiss()
                            startActivity(
                                Intent(this, MyCartActivity::class.java)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                        } else {
                            progressBar.dismiss()
                            displayToast("Something went wrong ,try again")
                        }
                    }
                }
            })
        })

        val cancelBtn = myDialog.findViewById<FloatingActionButton>(R.id.discardBtn)
        cancelBtn.setOnClickListener(View.OnClickListener {
            myDialog.dismiss()
        })

        mAuth = FirebaseAuth.getInstance()
        builder = AlertDialog.Builder(this)

        val myListData = arrayOf(
            Model1(
                "Hoodie",
                "Rs. 2000",
                R.drawable.image1
            ),
            Model1(
                "Speaker",
                "Rs. 3000",
                R.drawable.image2
            ),
            Model1(
                "Vegetable Basket",
                "Rs. 200",
                R.drawable.image3
            ),
            Model1(
                "Fruit Basket",
                "Rs. 500",
                R.drawable.image4
            ),
            Model1(
                "Shoes",
                "Rs. 1000",
                R.drawable.image5
            ),
            Model1(
                "Laptop",
                "Rs. 20000",
                R.drawable.image6
            )
        )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView3)
        val adapter = Adapter1(myListData, this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        val showMapBtn = findViewById<FloatingActionButton>(R.id.showMapBtn)
        showMapBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, HomeScreenActivity::class.java))
        })

        binding.logoutBtn.setOnClickListener(View.OnClickListener {
            builder.setMessage("Do you want to logout?").setTitle("GoFleet")
                .setCancelable(true)
                .setPositiveButton("Yes") { dialog, id ->

                    mAuth.signOut()
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(
                        applicationContext, "Logout successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id ->
                    dialog.cancel()
                    Toast.makeText(
                        applicationContext, "OK",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            val alert: AlertDialog = builder.create()
            alert.show()
        })

        binding.viewCart.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val obj = Constantinfo()
        capacitySelected = capacity[position]
        obj.selectCapacity = capacitySelected
        Log.d("TAG", "onItemSelected: ${obj.selectCapacity}")

        if (capacitySelected != 0) {
            Toast.makeText(
                this,
                "$capacitySelected is selected",
                Toast.LENGTH_SHORT
            )
                .show();
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun displayToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }
}