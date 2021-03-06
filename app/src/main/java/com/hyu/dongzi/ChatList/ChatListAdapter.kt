package com.hyu.dongzi.ChatList


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hyu.dongzi.R

class ChatListAdapter(val List: MutableList<ChatListItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return List.count()
    }

    override fun getItem(position: Int): Any {
        return List[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView

        val database = Firebase.database

        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_chatlist, parent, false)
        }

        val sellerName = view?.findViewById<TextView>(R.id.sellerId)


        // 사용자 이름 받아오기
        database.getReference("board").child(List[position].roomId).child("uid")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uid = snapshot.value.toString()

                    // 사용자 이름 받아오기
                    database.getReference("users").child(uid).child("name")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val value = snapshot.value.toString()
                                sellerName?.text = value

                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                }
                override fun onCancelled(error: DatabaseError) {
                }
            })


        val storageReference = Firebase.storage.reference.child(List[position].roomId + ".png")
        val image = view?.findViewById<ImageView>(R.id.imageView5)

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {

                Glide.with(view!!)
                    .load(task.result)
                    .into(image!!)

            }
        })

        return view!!

    }

}