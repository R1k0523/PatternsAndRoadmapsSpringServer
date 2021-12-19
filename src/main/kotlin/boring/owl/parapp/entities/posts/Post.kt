package boring.owl.parapp.entities.posts

import boring.owl.parapp.entities.user.User
import java.time.LocalDateTime
import java.util.*

open class Post {
    open val postId: UUID? = null
    open val title: String = ""
    open val image: String? = null
    open val publicationDateTime: LocalDateTime = LocalDateTime.now()
    open val postCategories: List<String> = listOf()
    open val postDescription: String = ""
    open val topic: Topic? = null
    open var creator: User? = null
    open var isNote: Boolean = true
    fun categories():String {
    return postCategories.joinToString(
        prefix = "",
        separator = " ",
        postfix = ",",
        transform = { it.uppercase() }
    )
}}
