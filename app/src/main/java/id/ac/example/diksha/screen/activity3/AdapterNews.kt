package id.ac.example.diksha.screen.activity3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ac.example.diksha.databinding.RowNewsBinding
import id.ac.example.diksha.modal.model.ModelNews

class AdapterNews(
    private val jembatannya: BtnSourcesListener,
    private val listArticle: ArrayList<ModelNews>
) :
    RecyclerView.Adapter<NewsViewHolder>() {

    private lateinit var binding: RowNewsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        binding = RowNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = listArticle[position]
        holder.bind(article, jembatannya)
    }

    override fun getItemCount(): Int {
        return listArticle.size
    }

    interface BtnSourcesListener {
        fun clickNews(modelBerita: ModelNews)
    }
}

class NewsViewHolder(private val binding: RowNewsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(berita: ModelNews, itemClick: AdapterNews.BtnSourcesListener) {
        binding.modelnya = berita
        binding.itemRow.setOnClickListener {
            itemClick.clickNews(berita)
        }

    }
}