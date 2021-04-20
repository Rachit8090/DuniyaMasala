package com.example.duniyamasala

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_startnews.*

class startnews : AppCompatActivity(),NewsItemClicked {
    private lateinit var mAdapter:NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startnews)
        RecyclerView.layoutManager= LinearLayoutManager(this)
        fetchData()
        mAdapter=NewsListAdapter(this)
        RecyclerView.adapter = mAdapter
    }
    private fun fetchData() {
        val url ="https://newsapi.org/v2/top-headlines?country=in&apiKey=dec25346fa96415a8869f26f5505d69e"
        val jsonObject = object : JsonObjectRequest(
            Request.Method.GET, url,
            null,
            Response.Listener
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener
            {

            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        MySingelton.getInstance(this).addToRequestQueue(jsonObject)
    }

    override fun onItemClicked(item: News){
        val builder  = CustomTabsIntent.Builder();
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));


    }
}
