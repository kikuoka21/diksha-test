package id.ac.example.diksha

import dagger.Component
import id.ac.example.diksha.modal.AppModule
import id.ac.example.diksha.modal.MyApplication
import id.ac.example.diksha.screen.activity2.MainSources
import id.ac.example.diksha.screen.activity2.SourcesViewModel
import id.ac.example.diksha.screen.activity3.MainNews
import id.ac.example.diksha.screen.activity3.NewsViewModel
import id.ac.example.diksha.screen.activity4.MainWebView

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: MyApplication)
//
    fun inject(activity: MainSources)
    fun inject(activity: SourcesViewModel)
//
    fun inject(activity: MainNews)
    fun inject(activity: NewsViewModel)
//
    fun inject(activity: MainWebView)
}