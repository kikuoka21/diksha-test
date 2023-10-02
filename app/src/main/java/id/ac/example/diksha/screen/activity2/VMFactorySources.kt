package id.ac.example.diksha.screen.activity2

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VMFactorySources (private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SourcesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SourcesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}