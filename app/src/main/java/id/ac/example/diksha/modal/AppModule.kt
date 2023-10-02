package id.ac.example.diksha.modal

import dagger.Module
import dagger.Provides

@Module
class AppModule (private val application: MyApplication){
    @Provides
    fun provideGenerateTool(): GenerateTool {
        return GenerateTool(application)
    }
}