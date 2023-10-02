package id.ac.example.diksha.modal.model

data class ModelSources(
    val id:String,
    val name:String,
    val description :String,
    val url:String,
    val category:String,
    val language:String,
    val country:String
){
    override fun toString(): String {
        return "$id $name $description"
    }
}

