package com.example.may1.smarthelmet.trackingdata

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.may1.smarthelmet.Class.Data
import com.example.may1.smarthelmet.Class.UpdateData
import com.example.may1.smarthelmet.Class.User
import com.example.may1.smarthelmet.R
import com.example.may1.smarthelmet.viewstrackingdata.UpdateFromItem
import com.example.may1.smarthelmet.viewstrackingdata.UpdateToItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_latest_data_tracking.*
import kotlinx.android.synthetic.main.activity_tracking_activity.*

class TrackingActivity : AppCompatActivity() {

    companion object {
        var currentData: Data? = null
        val TAG = "ChatLog"
    }
    internal var text_data = String()


    val adapter = GroupAdapter<ViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking_activity)

        recyclerview_chat_log.adapter = adapter

        toUser = intent.getParcelableExtra<User>(NewUserTrackingActivity.USER_KEY)

        supportActionBar?.title = toUser?.username

//    setupDummyData()
        listenForMessages()
        fetchCurrentData()
            send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send message....")
            performSendMessage()
        }

        navigate_tracking_latest_btn.setOnClickListener {
            val intent = Intent(this,LatestDataTrackingActivity::class.java)
            startActivity(intent)
        }



    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(UpdateData::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = LatestDataTrackingActivity.currentUser ?: return
                        adapter.add(UpdateFromItem(chatMessage.text, currentUser))
                    } else {
                        adapter.add(UpdateToItem(chatMessage.text, toUser!!))
                    }
                }

                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }

    private fun performSendMessage( ) {
        // how do we actually send a message to firebase...
        //val text = edittext_chat_log.text.toString()

        val text = text_data.toString()
        //data_txt.setText(text)
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewUserTrackingActivity.USER_KEY)
        //user_key.setText("fuck:"+ NewUserTrackingActivity.USER_KEY.toString())
        val toId = user.uid

        if (fromId == null) return

//    val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = UpdateData(reference.key!!,text!!, fromId, toId, System.currentTimeMillis())

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
               // edittext_chat_log.text.clear()
                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }

    private fun fetchCurrentData() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/datas/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentData = p0.getValue(Data::class.java)
                text_data = currentData!!.text
                // Log.d("LatestMessages", "Current user ${currentUser?.profileImageUrl}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}
