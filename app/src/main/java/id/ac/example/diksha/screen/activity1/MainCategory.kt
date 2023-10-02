package id.ac.example.diksha.screen.activity1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.example.diksha.R
import id.ac.example.diksha.modal.model.ModelMenu
import id.ac.example.diksha.screen.activity2.MainSources

class MainCategory : AppCompatActivity(), AdapterMenuCategory.OnBtnClickListener {
    private var listmenu = ArrayList<ModelMenu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_category)
        title = "List Category"

        val recyclerView = findViewById<RecyclerView>(R.id.recycleCategory)
        val menuAdapter = AdapterMenuCategory(this, listmenu)
        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = menuAdapter
        recyclerView.isNestedScrollingEnabled = false

//        business entertainment general health science sports technology
        additem("business", R.drawable.hands_shake)
        additem("entertainment", R.drawable.entertainment)
        additem("general", R.drawable.news_paper)
        additem("health", R.drawable.health)
        additem("science", R.drawable.genetic_algorithm)
        additem("sports", R.drawable.football)
        additem("technology", R.drawable.chip)
        menuAdapter.notifyDataSetChanged()
//

    }

    private fun additem(nama: String, drawable: Int) {
        listmenu.add(
            ModelMenu(
                nama,
                nama.substring(0, 1).uppercase() + nama.substring(1),
                drawable
            )
        )
    }

    override fun pindahActivity(menu: ModelMenu) {
        val pindahbawa = Intent(this, MainSources::class.java)
        pindahbawa.putExtra(MainSources.extraIdCategory, menu.idMenuCategory)
        pindahbawa.putExtra(MainSources.extraStrCategory, menu.namaMenuCategory)

        startActivity(pindahbawa)
    }

}