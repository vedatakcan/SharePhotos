package com.vedatakcan.sharephotos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.coroutines.handleCoroutineException

class NewsActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var recyclerViewAdapter: NewsRecyclerAdapter

    var postListesi = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        verileriAl()

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        recyclerViewAdapter = NewsRecyclerAdapter(postListesi)
        recyclerView.adapter = recyclerViewAdapter
    }

    fun verileriAl(){
        database.collection("Post").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener{snapshot, exception ->
            if (exception != null){
                Toast.makeText(this, exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(snapshot!!.isEmpty==false){

                   val documents = snapshot.documents

                    postListesi.clear()

                    for(document in documents){
                        val kullaniciEmail = document.get("kullaniciemail") as String
                        val kullaniciYorumu = document.get("kullaniciyorum") as String
                        val gorselUrl = document.get("gorselurl") as String

                        var indirilenPost = Post(kullaniciEmail,kullaniciYorumu,gorselUrl)
                        postListesi.add(indirilenPost)

                    }
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.share_photos){


            val intent = Intent(this, SharePhotosActivity::class.java)
            startActivity(intent)


        }else if(item.itemId==R.id.log_out){
            auth.signOut()
            val intent= Intent(this,UserActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}