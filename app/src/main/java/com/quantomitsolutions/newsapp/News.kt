package com.quantomitsolutions.newsapp

 data class News (
     val pubDate : String,
     val source_id : String,
     val title: String,
     val description: String,
     val image_url: String,
     val link: String
)
