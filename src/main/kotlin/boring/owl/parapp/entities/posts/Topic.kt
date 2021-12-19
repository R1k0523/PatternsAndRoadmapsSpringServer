package boring.owl.parapp.entities.posts

import boring.owl.parapp.entities.user.User
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name="topics")
@ApiModel("Модель топика")
class Topic {
    @Id
    @GeneratedValue
    @Column(nullable = false)
    @ApiModelProperty(notes = "Идентификационный номер топика")
    var topicId: UUID? = null
    @Column(nullable = false)
    @ApiModelProperty(notes = "Название топика")
    var title: String = ""
    @ManyToOne(cascade = [CascadeType.MERGE, CascadeType.DETACH])
    @ApiModelProperty(notes = "Создатель топика")
    var creator: User? = null
    @ManyToMany(cascade = [CascadeType.DETACH])
    @ApiModelProperty(notes = "Список модераторов топика")
    var moderators: MutableList<User> = mutableListOf()
}