package com.quantomitsolutions.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() ,  NewsItemClicked{
    private lateinit var mAdapter: NewsAdapter
    private lateinit var sign_out_btn: AppCompatButton
    private lateinit var gso :GoogleSignInOptions
    private lateinit var gsc : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.newsRecyclerView)
        gso  = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        gsc = GoogleSignIn.getClient(this,gso)
        sign_out_btn = findViewById(R.id.sign_out_btn)
        sign_out_btn.setOnClickListener {
            signOut()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        dataIteams()
        mAdapter = NewsAdapter(this)
        recyclerView.adapter = mAdapter
    }
    private fun dataIteams() {
        val url = "https://newsdata.io/api/1/news?&apikey=pub_78308805e5f10d3907293b0ea572da5b7cbd"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                val newsJsonArray = it.getJSONArray("results")
                val jsonArray = ArrayList<News>()
                for(i in 0 until  newsJsonArray.length()){
                    val jsonArrayObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        jsonArrayObject.getString("pubDate"),
                        jsonArrayObject.getString("source_id"),
                        jsonArrayObject.getString("title"),
                        jsonArrayObject.getString("description"),
                        jsonArrayObject.getString("image_url"),
                        jsonArrayObject.getString("link"),

                    )
                    jsonArray.add(news)
                }
                mAdapter.updatedNews(jsonArray)
            },
            { error ->
                // TODO: Handle error
            }
        )

            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.link))
    }
    private fun signOut(){
        gsc.signOut()
            .addOnCompleteListener(this, object : OnCompleteListener<Void?> {
                override fun onComplete(task: Task<Void?>) {
                    val i = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(i)
                }
            })
    }

}

