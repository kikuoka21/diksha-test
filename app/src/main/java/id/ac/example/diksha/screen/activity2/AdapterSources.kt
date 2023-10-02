package id.ac.example.diksha.screen.activity2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ac.example.diksha.databinding.RowSourcesBinding
import id.ac.example.diksha.modal.model.ModelSources

class AdapterSources(
    private val jembatannya: BtnSourcesListener,
    private val sources: ArrayList<ModelSources>
) :
    RecyclerView.Adapter<SourcesViewHolder>() {

    private lateinit var binding: RowSourcesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourcesViewHolder {
        binding = RowSourcesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SourcesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SourcesViewHolder, position: Int) {
        val sourcesnya = sources[position]
        holder.bind(sourcesnya, jembatannya)
    }

    override fun getItemCount(): Int {
        return sources.size
    }

    interface BtnSourcesListener {
        fun pindahActivity(modelBerita: ModelSources)
    }
}

class SourcesViewHolder(private val binding: RowSourcesBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(berita: ModelSources, itemClick: AdapterSources.BtnSourcesListener) {

        binding.modelnya = berita
        binding.itemRow.setOnClickListener {
            itemClick.pindahActivity(berita)
        }

    }
}