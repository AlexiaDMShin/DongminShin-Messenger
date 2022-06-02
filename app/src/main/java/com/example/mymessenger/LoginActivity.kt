package com.example.mymessenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mymessenger.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginBtn = binding.loginBtn
        val regiBtn = binding.RegistrationBtn

        /* ToDo:
           1. ID, PW 입력 화면 구현ㄴ
           2. 로그인 Button Click 시, ID, PW 확인,
           일치하면 MainActivity로 이동
           실패하면 재입력, 없을 경우 회원가입 요청 기능 구현*/
        loginBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        regiBtn.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}