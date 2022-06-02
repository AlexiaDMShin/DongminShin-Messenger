package com.example.mymessenger.ui.message

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mymessenger.databinding.FragmentMessageBinding
import com.example.mymessenger.databinding.ItemRecyclerMessageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.util.*

// ToDo: 읽지 않은 메시지 표기

class MessageFragment : Fragment() {
    private val fireDatabase = FirebaseDatabase.getInstance().reference

    private var _binding: FragmentMessageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // "새 메시지" button click 시, 새 메시지 화면 이동
        val newBtn = binding.newMsgButton
        newBtn.setOnClickListener{
            val intent = Intent(context, NewMessageActivity::class.java)
            context?.startActivity(intent)
        }

        val recyclerView = binding.rvMessage
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MessageAdapter()

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class MessageAdapter() : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

        private var uid : String? = null
        private val chatData = ArrayList<ChatData>()
        private val destinationUsers : ArrayList<String> = arrayListOf()

        init {
            uid = "alexia"
            fireDatabase.child("chatrooms").orderByChild("users/$uid").equalTo(true).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatData.clear()
                    for(data in snapshot.children){
                        chatData.add(data.getValue<ChatData>()!!)
                    }
                    notifyDataSetChanged()
                }
            })
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            val binding : ItemRecyclerMessageBinding = ItemRecyclerMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var destinationUid: String? = null

            for (user in chatData[position].users.keys) {
                if (!user.equals(uid)) {
                    destinationUid = user
                    destinationUsers.add(destinationUid)
                }
            }
            fireDatabase.child("users").child("$destinationUid").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messagedata = snapshot.getValue<MessageData>()
                    Glide.with(holder.itemView.context).load(messagedata?.photo)
                        .apply(RequestOptions().circleCrop())
                        .into(holder.imageView)
                    holder.textView_title.text = messagedata?.name
                }
            })

            //대화내용 내림차순 정렬 후, 마지막 대화내용의 키값을 가져옴
            val commentMap = TreeMap<String, ChatData.Chatting>(Collections.reverseOrder())
            commentMap.putAll(chatData[position].chattings)
            val lastMessageKey = commentMap.keys.toTypedArray()[0]
            holder.textView_lastMessage.text = chatData[position].chattings[lastMessageKey]?.message

            //대화방 Click 시, 대화창 이동
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("destinationUid", destinationUsers[position])
                context?.startActivity(intent)
            }
        }
        override fun getItemCount(): Int {
            return chatData.size
        }

        inner class ViewHolder(binding : ItemRecyclerMessageBinding) : RecyclerView.ViewHolder(binding.root) {
            val imageView = binding.itemPhoto
            val textView_title = binding.itemName
            val textView_lastMessage = binding.itemMsgText
        }
    }
}