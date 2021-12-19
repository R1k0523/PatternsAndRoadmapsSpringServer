package boring.owl.parapp.entities.posts.notes

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name="sections")
@ApiModel("Модель секции записи")
class NoteSection {
    @Id
    @GeneratedValue
    @Column(name = "section_id", nullable = false)
    @ApiModelProperty(notes = "Идентификационный номер секции")
    var sectionId: UUID? = null
    @Column
    @ApiModelProperty(notes = "Описание секции")
    var description: String = ""
    @ElementCollection
    @ApiModelProperty(notes = "Список элементов секции")
    var linkItems: MutableList<String> = mutableListOf()
}