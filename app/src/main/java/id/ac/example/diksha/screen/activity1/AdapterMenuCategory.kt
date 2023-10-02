package id.ac.example.diksha.screen.activity1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ac.example.diksha.databinding.RowMenuCategoryBinding
import id.ac.example.diksha.modal.model.ModelMenu

class AdapterMenuCategory (private val jembatannya: OnBtnClickListener,
                           private val menu: ArrayList<ModelMenu>):
    RecyclerView.Adapter<AdapterMenuCategory.MenuViewHolder>() {

    private lateinit var binding: RowMenuCategoryBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        binding = RowMenuCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menu[position], jembatannya)
    }

    override fun getItemCount(): Int {
        return menu.size
    }


    class MenuViewHolder(private val binding: RowMenuCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: ModelMenu, jembatannya: OnBtnClickListener) {
            binding.modelnya = menu

            binding.itemRow.setOnClickListener{
                jembatannya.pindahActivity(menu)
            }

        }
    }

    interface OnBtnClickListener {
        fun pindahActivity(menu: ModelMenu)
    }


}