package com.vedatakcan.sharephotos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class UserActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun kayitOl(view : View){

        var email = emailText.text.toString()
        var password = passwordText.text.toString()
        if (email !="" && password !=""){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                // asenkron
                if(task.isSuccessful){

                    Toast.makeText(this,"Kayıt işlemi başarılı",Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener{ exception ->
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG ).show()
            }

        }else{
            Toast.makeText(this,"Lütfen tüm alanları doldurun.",Toast.LENGTH_LONG).show()
        }

    }

    fun girisYap(view : View){

        var email = emailText.text.toString()
        var password = passwordText.text.toString()
        if (email !="" && password !=""){

        }else{
            Toast.makeText(this, "Lütfen tüm alanaları doldurun", Toast.LENGTH_LONG).show()
        }

        auth.signInWithEmailAndPassword(emailText.text.toString(),passwordText.text.toString()).addOnCompleteListener { task->
            if (task.isSuccessful){
                val currentUser =auth.currentUser?.email.toString()
                Toast.makeText(this,"Hoşgeldin: ${currentUser}",Toast.LENGTH_LONG).show()
                val intent = Intent (this, NewsActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.localizedMessage,Toast.LENGTH_LONG).show()
        }



    }

}