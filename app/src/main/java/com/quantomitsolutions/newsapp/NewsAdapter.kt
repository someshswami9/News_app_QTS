package com.quantomitsolutions.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter (private val listner: NewsItemClicked): RecyclerView.Adapter<NewsViewHolder>() {

    private val items: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        val viewHolder = NewsViewHolder(view)
        view.setOnClickListener{
           listner.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.timeView.text = currentItem.pubDate
        holder.newsSoruce.text = currentItem.source_id
        holder.titleView.text = currentItem.title
        holder.description.text = currentItem.description
        Glide.with(holder.itemView.context).load(currentItem.image_url).into(holder.newsImage)

    }

    fun updatedNews(updatedNews : ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)
        notifyDataSetChanged()
    }

}
class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val titleView: TextView = itemView.findViewById(R.id.title_tv)
    val timeView: TextView = itemView.findViewById(R.id.published_at_tv)
    val description: TextView = itemView.findViewById(R.id.description_news_tv)
    val newsImage: ImageView = itemView.findViewById(R.id.news_image_iv)
    val newsSoruce: TextView = itemView.findViewById(R.id.news_source_id)
}

interface NewsItemClicked {
    fun onItemClicked(item: News)
}