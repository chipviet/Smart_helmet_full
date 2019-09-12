package com.example.may1.smarthelmet.trackingdata

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.may1.smarthelmet.Class.User
import com.example.may1.smarthelmet.connectbluetooth.DashboardActivity
import com.example.may1.smarthelmet.MusicDashboardActivity
import com.example.may1.smarthelmet.R
import com.example.may1.smarthelmet.SettingsDashboardActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_tracking.view.*
import kotlinx.android.synthetic.main.new_user_tracking_activity.*

class NewUserTrackingActivity : AppCompatActivity() {


    companion object {
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_user_tracking_activity)


        //**************************************************************************************************************

        feed_music_activity.setOnClickListener {
            val intent = Intent(this, MusicDashboardActivity::class.java)
            startActivity(intent)
        }

        feed_home_activity.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        feed_settings_activity.setOnClickListener {
            val intent = Intent(this, SettingsDashboardActivity::class.java)
            startActivity(intent)
        }

        feed_map_activity.setOnClickListener {
                val launchIntent = packageManager.getLaunchIntentForPackage("com.google.android.apps.maps")
                if (launchIntent != null) {
                    startActivity(launchIntent)//null pointer check in case package name was not found
                }
                Log.d("AAA","running")
            }

        //**************************************************************************************************************
        fetchUsers()

    }
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem

                    val intent = Intent(view.context, TrackingActivity::class.java)

                    //Something wrong in here
                    intent.putExtra(USER_KEY,  userItem.user)
//                    intent.putExtra(USER_KEY, userItem.user)
//                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)

                    finish()

                }

                recyclerview_user_tracking.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}
class UserItem(val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_tracking.text = user.username
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_tracking
    }
}

// this is super tedious

