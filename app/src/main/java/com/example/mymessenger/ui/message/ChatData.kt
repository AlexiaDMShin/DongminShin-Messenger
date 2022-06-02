package com.example.mymessenger.ui.message

class ChatData (val users: HashMap<String, Boolean> = HashMap(),
                val chattings : HashMap<String, Chatting> = HashMap())
{
    class Chatting(
        val uid: String? = null, // ID
        val message: String? = null, // 대화내용
        val time: String? = null // 대화시간
    )
}