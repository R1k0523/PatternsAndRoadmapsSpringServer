package boring.owl.parapp.entities.posts.notes

import boring.owl.parapp.entities.posts.Post
import boring.owl.parapp.entities.posts.Topic
import boring.owl.parapp.entities.user.User
import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name="notes")
@ApiModel("Модель записи")
class Note : Post() {
    @Id @GeneratedValue @Column(nullable = false)
    @ApiModelProperty(notes = "Идентификационный номер записи")
    override var postId: UUID? = null
    @Column(nullable = false) @ApiModelProperty(notes = "Название записи")
    override var title: String = ""
    @Column @ApiModelProperty(notes = "Ссылка на фото, прикрепленное к записи")
    override var image: String? = null
    @Column @DateTimeFormat(pattern = "HH:mm dd.MM.yyyy", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "HH:mm dd.MM.yyyy") @ApiModelProperty(notes = "Дата публикации записи")
    override var publicationDateTime: LocalDateTime = LocalDateTime.now()
    @ElementCollection @ApiModelProperty(notes = "Список категорий записи")
    override var postCategories: MutableList<String> = mutableListOf()
    @Column(length=2000) @ApiModelProperty(notes = "Описание записи")
    override var postDescription: String = ""
    @ManyToOne(cascade = [CascadeType.MERGE]) @ApiModelProperty(notes = "Создатель записи")
    override var creator: User? = null
    @ManyToOne @ApiModelProperty(notes = "Топик, где расположена запись")
    override var topic: Topic? = null
    @OneToMany(cascade = [CascadeType.ALL]) @ApiModelProperty(notes = "Секции текста записи")
    var sections: MutableList<NoteSection> = mutableListOf()
    @ElementCollection @ApiModelProperty(notes = "Документы, прикрепленные к записи")
    var docs: MutableList<String>? = null
    @Column @ApiModelProperty(notes = "Флаг, является ли пост записью")
    override var isNote: Boolean = true
    fun getKeyWords(): String {
        val regex = Regex("[A-Z][A-z]+")
        val query = arrayListOf<String>()
        var text = categories() + " "
        sections?.forEach { it ->
            text += it.description
        }
        regex.findAll(text).iterator().forEach {
            if (it.value !in query)
                query.add(it.value)
        }
        return query.joinToString(" OR ")
    }
}
