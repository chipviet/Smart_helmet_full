package com.example.may1.smarthelmet.viewstrackingdata


import com.example.may1.smarthelmet.Class.User
import com.example.may1.smarthelmet.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.update_from_row.view.*
import kotlinx.android.synthetic.main.update_to_row.view.*


class UpdateFromItem(val text: String, val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text


//        val uri = user.profileImageUrl
//        val targetImageView = viewHolder.itemView.imageview_chat_from_row
//        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.update_from_row
    }
}

class UpdateToItem(val text: String, val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text

        // load our user image into the star
//        val uri = user.profileImageUrl
//        val targetImageView = viewHolder.itemView.imageview_chat_to_row
//        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.update_to_row
    }
}