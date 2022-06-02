package com.example.mymessenger.ui.message

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymessenger.databinding.ActivityMessageBinding

class NewMessageActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val closeBtn = binding.closeBtn
        closeBtn.setOnClickListener{
            finish()
        }
    }
    /* ToDo:
       1. 새 메시지 화면 구현
       2. 새 메시지 상대 검색 기능 구현
       3. 선택한 상대와의 대화방 구성 및 이동 구현
     */
}