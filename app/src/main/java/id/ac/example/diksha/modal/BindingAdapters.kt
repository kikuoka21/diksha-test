package id.ac.example.diksha.modal

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import id.ac.example.diksha.R
import java.util.*

@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, linkFoto: String?) {
    linkFoto?.let {

        val picassoBuilder = Picasso.Builder(view.context)
        val picasso = picassoBuilder.build()
        picasso
            .load(linkFoto)
            .error(R.drawable.ic_baseline_hide)
            .placeholder(R.drawable.ic_baseline)
            .into(view)
    }
}

@BindingAdapter("setImageDrawable")
fun fromDrawable(imageView: ImageView, resource: Int) {
    imageView.setBackgroundResource(resource)
}



