package boring.owl.parapp

import boring.owl.parapp.entities.posts.notes.Note
import com.github.kittinunf.fuel.httpGet
import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime


fun main() {
    val (_, _, r) = "http://localhost:8080/api/post/29d4e984-6428-4887-86f3-ec0a69e096c6".httpGet().responseString()
//    val collectionType: Type = object : TypeToken<ListResponse<Pattern>>() {}.type
    val gson = GsonBuilder().registerTypeAdapter(
        LocalDateTime::class.java,
        object : JsonDeserializer<LocalDateTime?> {
            @Throws(JsonParseException::class)
            override fun deserialize(
                json: JsonElement,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): LocalDateTime? {
                return LocalDateTime.parse(json.asString)
            }
        }).create()
    val info: Note = gson.fromJson(r.get(), Note::class.java)
    println(info)

}