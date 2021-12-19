package boring.owl.parapp.entities.posts.roadmaps

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
@Table(name="roadmaps")
@ApiModel("Модель дорожной карты")
open class Roadmap : Post() {
    @Id @GeneratedValue @Column(nullable = false)
    @ApiModelProperty(notes = "Идентификационный номер дорожной карты")
    override var postId: UUID? = null
    @Column(nullable = false) @ApiModelProperty(notes = "Название дорожной карты")
    override var title: String = ""
    @Column @ApiModelProperty(notes = "Ссылка на фото, прикрепленное к дорожной карты")
    override var image: String? = null
    @Column @ApiModelProperty(notes = "Флаг, является ли пост записью")
    override var isNote: Boolean = false
    @Column @DateTimeFormat(pattern = "HH:mm dd.MM.yyyy", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "HH:mm dd.MM.yyyy") @ApiModelProperty(notes = "Дата публикации дорожной карты")
    override var publicationDateTime: LocalDateTime = LocalDateTime.now()
    @ElementCollection @ApiModelProperty(notes = "Список категорий дорожной карты")
    override var postCategories: MutableList<String> = mutableListOf()
    @Column(length=2000) @ApiModelProperty(notes = "Описание дорожной карты")
    override var postDescription: String = ""
    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.DETACH])
    @ApiModelProperty(notes = "Создатель дорожной карты")
    override var creator: User? = null
    @ManyToOne @ApiModelProperty(notes = "Топик, где расположена запись")
    override var topic: Topic? = null
    @OneToOne(cascade= [CascadeType.ALL])
    @JoinColumn(name = "address_id", referencedColumnName = "node_id")
    @ApiModelProperty(notes = "Корень дорожной карты")
    open var root: RoadmapNode? = null
}

