/*
 Name: Curt Terpstra
 Class: CIS-245-OA010 (Spring 2021)
 App: Week 12 To Do List
*/

package edu.rockvalleycollege.week12todolist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var txtTaskName = findViewById<EditText>(R.id.txtTaskName)
        var txtMessage = findViewById<EditText>(R.id.txtMessage)
        var btnMessage = findViewById<Button>(R.id.btnMessage)
        var txtNotes = findViewById<TextView>(R.id.txtNotes)
        var ref = FirebaseDatabase.getInstance("https://week-12-todo-list-default-rtdb.firebaseio.com/").getReference("Message")

        btnMessage.setOnClickListener{
            // Write a message to the database
            var messageid = ref.push().key
            var messageg = Message(messageid.toString(), txtTaskName.text.toString(), txtMessage.text.toString())
            hideKeyboard()
            txtTaskName.setText("")
            txtMessage.setText("")
            txtTaskName.requestFocus()
            ref.child(messageid.toString()).setValue(messageg).addOnCompleteListener {
                Toast.makeText(this, "Task Added!", 3).show()
            }
        }
//listen and show data changes
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                txtNotes.text = ""
                val children = dataSnapshot.children
                children.forEach {
                    println("data: " + it.toString())
                    if (txtNotes.text.toString() != "") {
                        txtNotes.text = txtNotes.text.toString() + "\n" + "Task: " + it.child("name").value.toString() + " " + "Desc: " + it.child("message").value.toString()
                    }else{
                        txtNotes.text = "My Tasks"
                        txtNotes.text = txtNotes.text.toString() + "\n" + "Task: " + it.child("name").value.toString() + " " + "Desc: " + it.child("message").value.toString()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Message", "Failed to read value.", error.toException())
            }
        })
        findViewById<View>(android.R.id.content).setOnTouchListener { _, event ->
            hideKeyboard()
            false
        }// End of hidekeyboard findviewbyid

    }// end of oncreate

    fun hideKeyboard() {
        try {
            val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            // TODO: handle exception
        }
    }// end of Hide Keyboard

}//end of main activity