package com.example.project_one

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_one.databinding.ActivityAddCartBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList

class AddCartActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityAddCartBinding

    var capacitySelected: Double = 0.0
    var remainingCapacity: Double = 0.0
    var totalCapacitySelected: Double = 0.0

    private var capacity = arrayOf(
        0.0, 0.5, 1.0, 2.0, 2.5, 5.0, 10.0, 15.0
    )

    private lateinit var list: ArrayList<Model>
    private lateinit var adapter: MyAdapter

    lateinit var myDialog:Dialog

    lateinit var itemName: String
    lateinit var itemWeight: String
    lateinit var itemPrice: String

    var totalProducts: String = ""
    var totalPrice: Double = 0.0
    var totalWeight: Double = 0.0

    lateinit var builder: AlertDialog.Builder

    var firebaseAuth: FirebaseAuth? = null
    private lateinit var database: FirebaseDatabase

    lateinit var progressBar: MyProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectCapacity.onItemSelectedListener = this
        builder = AlertDialog.Builder(this)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        progressBar = MyProgressBar("Wait...", this)

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

        list = ArrayList()
        adapter = MyAdapter(this)

        binding.addItemBtn.setOnClickListener(View.OnClickListener {
            if(capacitySelected==0.0){
                Toast.makeText(this, "Please select capacity first", Toast.LENGTH_SHORT).show()
            }
            else{
                myDialog = Dialog(this)
                myDialog.setContentView(R.layout.take_info)
                myDialog.setCancelable(false)
                myDialog.window!!.setLayout( MATCH_PARENT, WRAP_CONTENT)

                binding.recyclerView.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                binding.recyclerView.adapter = adapter

                val addItem: FloatingActionButton = myDialog.findViewById(R.id.addNewItem)

                val iNET:EditText = myDialog.findViewById(R.id.itemNameET)
                val iWET:EditText = myDialog.findViewById(R.id.itemWeightET)
                val iPET:EditText = myDialog.findViewById(R.id.itemPriceET)

                val cancelBtn = myDialog.findViewById<FloatingActionButton>(R.id.cancelBtn)
                cancelBtn.setOnClickListener(View.OnClickListener {
                    myDialog.dismiss()
                })

                myDialog.show()

                addItem.setOnClickListener(View.OnClickListener {
                    itemName = iNET.text.toString()
                    itemWeight = iWET.text.toString()
                    itemPrice = iPET.text.toString()
                    if(itemName.isEmpty() || itemWeight.isEmpty() || itemPrice.isEmpty()){
                        if(itemName.isEmpty()){
                            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show()
                        }
                        if(itemWeight.isEmpty()){
                            Toast.makeText(this, "Please enter weight", Toast.LENGTH_SHORT).show()
                        }
                        if(itemPrice.isEmpty()){
                            Toast.makeText(this, "Please enter price", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        val curCap = itemWeight.toDouble()
                        if(curCap<=remainingCapacity){
                            totalCapacitySelected += curCap
                            remainingCapacity -= totalCapacitySelected
                            binding.gotoCart.visibility = View.VISIBLE

                            totalProducts += "$itemName, "
                            totalPrice += itemPrice.toDouble()
                            totalWeight += itemWeight.toDouble()

                            adapter.add(Model(itemName, "$itemWeight kg", "₹ $itemPrice"))
                        }
                        else{
                            Toast.makeText(this, "No capacity in your bag\nPlease increase capacity", Toast.LENGTH_SHORT).show()
                        }
                    }
                    myDialog.dismiss()
                })
            }
        })

        binding.gotoCart.setOnClickListener(View.OnClickListener {
            val i = Intent(this@AddCartActivity, MyCartActivity::class.java)

            val items = AddCartItem(
                totalProducts, totalWeight.toString(), totalPrice.toString(), "Unpaid"
            )

            var childTime = Calendar.getInstance().time.toString()
            childTime = childTime.replace("\\s".toRegex(), "")
            childTime = childTime.replace(":", "")
            childTime = childTime.replace("+", "")

            val databaseRef = database.getReferenceFromUrl("https://project-one-fe963-default-rtdb.firebaseio.com/").child("users")
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

            startActivity(i)
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        capacitySelected = capacity[position]
        remainingCapacity = capacitySelected-totalCapacitySelected
        if (capacitySelected != 0.0) {
            Toast.makeText(
                this@AddCartActivity,
                "$capacitySelected kg is selected",
                Toast.LENGTH_SHORT
            )
                .show();
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        builder.setMessage("Do you discard the cart?").setTitle("Warning!")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                val intent: Intent = Intent(this, HomeScreenActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    this, "Okay",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
                dialog.cancel()
                Toast.makeText(
                    this, "OK",
                    Toast.LENGTH_SHORT
                ).show()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun displayToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }
}