package com.example.mymessenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymessenger.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity(){
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backBtn = binding.backBtn
        val regiBtn = binding.RegistrationBtn

        /* 회원가입 기능 개발 후 삭제 */
        backBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        /* ToDo:
           1. ID, PW, 이름, 사진 등록 화면 구현
           2. ID, PW, 이름, 사진 Data 등록 기능 구현*/
        regiBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}