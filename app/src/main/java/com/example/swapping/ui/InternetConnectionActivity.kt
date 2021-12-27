package com.example.swapping.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import com.example.swapping.DataBaseHelper
import com.example.swapping.Models.NetworkConnection
import com.example.swapping.R
import com.example.swapping.ui.userLogin.LoginActivity

class InternetConnectionActivity : AppCompatActivity() {
    private var connected = false
    private val handler = Handler()
    private lateinit var pb: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internet_connection)
        val networkConnection = NetworkConnection()
        var progressStatus = 0
        pb = findViewById(R.id.progressBar)
        Thread {
            while (!connected) {
                // Update the progress status
                progressStatus += 1

                // Try to sleep the thread for 20 milliseconds
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                // Update the progress bar
                handler.post {
                    pb.progress = progressStatus
                    // Show the progress on TextView
                    // If task execution completed
                    if (networkConnection.isNetworkAvailable(applicationContext)) {
                        val dbHelper = DataBaseHelper(applicationContext)
                        dbHelper.addCategories()
                        dbHelper.addVoivodeships()
                        dbHelper.addStatuses()
                        connected = true
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }

                }
            }
        }.start()
    }
}