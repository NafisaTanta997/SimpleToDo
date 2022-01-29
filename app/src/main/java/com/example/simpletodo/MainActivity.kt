package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // Let's detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            //Code is executed when the user clicks a button
//            Log.i("Nafisa","User clicked on button")
//        }

        val onLongClickListener = object : TaskItemAdapter.onLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. Remove item from list
                listOfTasks.removeAt(position)
                // 2. Notify adapter that our data set has changed
                adapter.notifyDataSetChanged()
                // Updates list of tasks in file
                saveItems()
            }

        }

//        listOfTasks.add("Do laundry")
//        listOfTasks.add("Buy shoes")

        loadItems()

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field, so that the user can enter a
        // task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // Get a reference to the button
        // Then, set an onClickListener to it
        findViewById<Button>(R.id.button).setOnClickListener{

            // 1. Grab the user input text in: @+id/addTaskField
            val userInputText = inputTextField.text.toString()

            // 2. Add string to list of tasks: listOfTasks
            listOfTasks.add(userInputText)
            // Notify the adapter that our data has updated
            adapter.notifyItemInserted(listOfTasks.size - 1)
            // Updates list of tasks in file
            saveItems()
            // 3. Reset text field
            inputTextField.setText("")
        }
    }

    // Save the data the user has inputted - by writing and reading from a file

    // Get the file we need
    fun getDataFile(): File {

        // Every line represents a task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the file by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Saving items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}