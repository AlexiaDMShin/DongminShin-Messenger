package com.example.mymessenger.ui.message

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mymessenger.R
import com.example.mymessenger.databinding.ActivityChatBinding
import com.example.mymessenger.databinding.ItemRecyclerChatBinding
import com.example.mymessenger.ui.message.ChatData.Chatting
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.*

/* ToDo:
   1. 대화방간 이동(메시지 알림)
   2. 대화에 URL이 있는 경우 clikable하게 출력
 */

class ChatActivity : AppCompatActivity() {

    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private var chattingUid: String? = null
    private var destinationUid: String? = null
    private var uid: String? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val closeBtn = binding.closeBtn
        val imageView = binding.chatSend
        val editText = binding.chatEdit

        //메시지 전송/수신 시간
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()

        destinationUid = intent.getStringExtra("destinationUid")
        uid = "alexia"
        recyclerView = binding.rvChat

        closeBtn.setOnClickListener{
            finish()
        }

        imageView.setOnClickListener {
            val chatData = ChatData()
            chatData.users.put(uid.toString(), true)
            chatData.users.put(destinationUid!!, true)

            val chatting = ChatData.Chatting(uid, editText.text.toString(), curTime)
            if (chattingUid == null) {
                imageView.isEnabled = false
                fireDatabase.child("chatrooms").push().setValue(chatData).addOnSuccessListener {
                    //대화창 생성
                    createChatRoom()
                    //대화내용 보내기
                    Handler().postDelayed({
                        println(chattingUid)
                        fireDatabase.child("chatrooms").child(chattingUid.toString()).child("chattings").push().setValue(chatting)
                        editText.text = null
                    }, 1000L)
                }
            } else {
                fireDatabase.child("chatrooms").child(chattingUid.toString()).child("chattings").push().setValue(chatting)
                editText.text = null
            }
        }
        createChatRoom()
    }

    private fun createChatRoom() {
        fireDatabase.child("chatrooms").orderByChild("users/$uid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
                        val chatData = item.getValue<ChatData>()
                        if(chatData?.users!!.containsKey(destinationUid)){
                            chattingUid = item.key
                            findViewById<ImageView>(R.id.chat_send).isEnabled = true
                            recyclerView?.layoutManager = LinearLayoutManager(this@ChatActivity)
                            recyclerView?.adapter = ChatAdapter()
                        }
                    }
                }
            })
    }

    inner class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

        private val chattings = ArrayList<Chatting>()
        private var messagedata : MessageData? = null

        init{
            fireDatabase.child("users").child(destinationUid.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    messagedata = snapshot.getValue<MessageData>()
                    findViewById<TextView>(R.id.chat_name).text = messagedata?.name
                    getMessageList()
                }
            })
        }

        fun getMessageList(){
            fireDatabase.child("chatrooms").child(chattingUid.toString()).child("chattings").addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    chattings.clear()
                    for(data in snapshot.children){
                        val item = data.getValue<Chatting>()
                        chattings.add(item!!)
                        println(chattings)
                    }
                    notifyDataSetChanged()
                    recyclerView?.scrollToPosition(chattings.size - 1)
                }
            })
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding : ItemRecyclerChatBinding = ItemRecyclerChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }
        //@SuppressLint("RtlHardcoded")
        override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
            holder.textView_message.textSize = 20F
            holder.textView_message.text = chattings[position].message
            holder.textView_time.text = chattings[position].time
            if(chattings[position].uid.equals(uid)){ // 본인 대화
                holder.textView_message.setBackgroundResource(R.drawable.chatting_background1)
                holder.textView_name.visibility = View.INVISIBLE
                holder.chat_sub.visibility = View.INVISIBLE
                holder.chat_main.gravity = Gravity.RIGHT
                }else{ // 상대방 대화
                Glide.with(holder.itemView.context)
                    .load(messagedata?.photo)
                    .apply(RequestOptions().circleCrop())
                    .into(holder.imageView_photo)
                holder.textView_name.text = messagedata?.name
                holder.chat_sub.visibility = View.VISIBLE
                holder.textView_name.visibility = View.VISIBLE
                holder.textView_message.setBackgroundResource(R.drawable.chatting_background2)
                holder.chat_main.gravity = Gravity.LEFT
            }
        }

        override fun getItemCount(): Int {
            return chattings.size
        }

        inner class ViewHolder(binding : ItemRecyclerChatBinding) : RecyclerView.ViewHolder(binding.root) {
            val textView_message = binding.itemChatMsg
            val textView_name = binding.itemChatName
            val imageView_photo = binding.itemChatPhoto
            val chat_sub = binding.itemChatSub
            val chat_main = binding.itemChatMain
            val textView_time = binding.itemChatTime
        }
    }
}