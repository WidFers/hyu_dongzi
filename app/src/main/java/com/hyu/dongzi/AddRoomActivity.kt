package com.hyu.dongzi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.hyu.dongzi.databinding.ActivityAddRoomBinding
import kotlinx.android.synthetic.main.activity_add_room.*
import kotlinx.android.synthetic.main.fragment_user.*

class AddRoomActivity : AppCompatActivity() {
    private var uid:String?= ""
    val binding by lazy { ActivityAddRoomBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) { //onCreate = 앱이 최초 실행되었을 때 수행한다.
        super.onCreate(savedInstanceState)
        setContentView(binding.root) //xml 화면 뷰를 연결한다.

        if(intent.hasExtra("uid")){
            uid = intent.getStringExtra("uid")
        }

        btn_addRoom.setOnClickListener {

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference()

            val dataInput = Rooms(
                btn_add_image.imageAlpha.toInt(),
                et_deposit.text.toString(),
                et_monthly.text.toString(),
                toggleButton3.text.toString()
            )
            myRef.child(uid!!).push().setValue(dataInput)
        }
    }
    fun loadImage(view: View) {
        startActivityForResult(
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI),
            0
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null) imageView.setImageURI(data?.data)
    }

}