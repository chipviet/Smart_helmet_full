package com.example.may1.smarthelmet.viewstrackingdata

import com.example.may1.smarthelmet.Class.UpdateData
import com.example.may1.smarthelmet.Class.User
import com.example.may1.smarthelmet.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*
import java.text.SimpleDateFormat

class LatestDataRow (val updateData: UpdateData): Item<ViewHolder>() {
    var updatePartnerUser: User? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.content_textview_latest_update.text = updateData.text

        //************************************time*******************************************
        val sdf_time = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss a")
        val timeString = sdf_time.format((updateData.timestamp))
        viewHolder.itemView.latest_time_tracking.text = timeString

        //**********************************************************************************
        val chatPartnerId: String
        if (updateData.fromId == FirebaseAuth.getInstance().uid) {
            chatPartnerId = updateData.toId
        } else {
            chatPartnerId = updateData.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                updatePartnerUser = p0.getValue(User::class.java)
                viewHolder.itemView.username_textview_latest_update.text = updatePartnerUser?.username

               // val targetImageView = viewHolder.itemView.imageview_latest_message
               // Picasso.get().load(updatePartnerUser?.profileImageUrl).into(targetImageView)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}