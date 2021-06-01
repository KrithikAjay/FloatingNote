package com.krithik.floatingnote


import android.app.Notification
import android.app.NotificationManager
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.krithik.floatingnote.database.Note
import com.krithik.floatingnote.database.NoteDatabase
import com.krithik.floatingnote.database.NoteRepository
import com.krithik.floatingnote.databinding.ActivityMainBinding
import com.krithik.floatingnote.service.*

import com.krithik.floatingnote.viewModel.NoteViewModel
import com.krithik.floatingnote.viewModel.NoteViewModelFactory
import com.krithik.floatingnote.viewModel.RecyclerViewAdapter

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: RecyclerViewAdapter


    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startFloatingService()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = NoteDatabase.getInstance(application).noteDao
        val repository = NoteRepository(dao)
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        binding.noteViewModel = noteViewModel
        binding.lifecycleOwner = this

        noteViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
        initRecyclerView()
        noteViewModel.noteList.observe(this, Observer {
            adapter.submitList(it)
        })
        receiveReplyInput()

    }


    private fun initRecyclerView() {
        binding.noteRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(this)
        binding.noteRecyclerView.adapter = adapter

    }

    override fun onDeleteNote(note: Note) {
        noteViewModel.deleteNote(note)
    }

    private fun Context.startFloatingService(command: String = "") {
        val intent = Intent(this, FloatingService::class.java)

        if (command.isNotBlank()) intent.putExtra(INTENT_COMMAND, command)
        Log.i("Command", INTENT_COMMAND + command)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(intent)
        } else {
            this.startService(intent)
        }


    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    private fun receiveReplyInput() {

        val replyKey = REPLY_KEY
        val channelID = NOTIFICATION_CHANNEL_GENERAL

        val intent = this.intent
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

        val replyInput = RemoteInput.getResultsFromIntent(intent)



        if (replyInput != null) {
            val inputReplyString = replyInput.getCharSequence(replyKey).toString()

            noteViewModel.addfromReply(inputReplyString)
//            val notificationId = NOTIFICATION_ID
//
//            val updateCurrentNotification =
//                    NotificationCompat.Builder(this@MainActivity, channelID)
//                            .setLargeIcon(
//                                    BitmapFactory.decodeResource(
//                                            this.resources,
//                                            android.R.drawable.ic_dialog_info
//                                    )
//                            )
//                            .setSmallIcon(android.R.drawable.ic_dialog_info)
//                            .setContentTitle("Message sent success")
//                            .setContentText("Updated notification")
//                            .build()
//
//            val notificationManager =
//                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            notificationManager.notify(notificationId, updateCurrentNotification)

        }

    }
}