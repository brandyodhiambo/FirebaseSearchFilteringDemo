package com.odhiambodevelopers.firebasesearchfilteringdemo

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.odhiambodevelopers.firebasesearchfilteringdemo.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot

import com.google.android.gms.tasks.Task

import androidx.annotation.NonNull
import androidx.core.content.ContentProviderCompat.requireContext

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.DatabaseReference


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var databaseReference: DatabaseReference

    private val studentAdapter: StudentsAdapter by lazy {
        StudentsAdapter()
    }
    val studentsList = ArrayList<Students>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initializing databaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("students")



        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                filter(editable.toString())
            }

        })

        binding.btnSearch.setOnClickListener {
           getStudents()
        }
        binding.btnSearchSingle.setOnClickListener {
            searchByName(binding.etsearchValue.text.toString())
        }
        binding.btngender.setOnClickListener {
            filterGender()
        }

    }

    // search by specified value
    private fun searchByName(name: String) {
        // adding a value listener to database reference to perform search
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Checking if the value exists
                if (snapshot.exists()){
                    studentsList.clear()
                    // looping through to values
                    for (i in snapshot.children){
                        val student = i.getValue(Students::class.java)
                        // checking if the name searched is available and adding to the array list
                        if (student!!.name == name){
                            studentsList.add(student)
                        }
                    }
                    //setting data to recyclerview
                    studentAdapter.submitList(studentsList)
                    binding.recyclerStudents.adapter = studentAdapter
                } else{
                    Toast.makeText(applicationContext, "Data does not exist", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

// filter by specified value
  private  fun filter(e: String) {
        //Declaring the array list that holds the filtered values
        val filteredItem = ArrayList<Students>()
        // looping through the array list to obtain the required value
        for (item in studentsList) {
            if (item.name!!.toLowerCase().contains(e.toLowerCase())) {
                filteredItem.add(item)
            }
        }
        // adding the filted value to adapter
        studentAdapter.submitList(filteredItem)
    }

    // filter in general
    private fun filterGender(){
        //specifying path and filter category and adding a listener
        databaseReference.orderByChild("gender").equalTo("female").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    studentsList.clear()
                    for (i in snapshot.children){
                        val female = i.getValue(Students::class.java)
                        studentsList.add(female!!)
                    }
                    studentAdapter.submitList(studentsList)
                    binding.recyclerStudents.adapter = studentAdapter
                } else{
                    Toast.makeText(applicationContext, "Data is not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    // fetching all values from firebase
    private fun getStudents() {
        databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val student= i.getValue(Students::class.java)
                            if (student != null) {
                                studentsList.add(student)
                            }
                        }
                        studentAdapter.submitList(studentsList)
                        binding.recyclerStudents.adapter = studentAdapter
                    } else {
                        Toast.makeText(applicationContext, "Data Does not Exist", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

}