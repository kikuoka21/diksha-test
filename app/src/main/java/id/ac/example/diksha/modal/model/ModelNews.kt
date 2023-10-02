package id.ac.example.diksha.modal.model

data class ModelNews(
    var author: String?,
    val title: String,
    val description: String,
    val url: String,
    var urlToImage: String?,
    val publishedAt: String,
    var content: String?)
