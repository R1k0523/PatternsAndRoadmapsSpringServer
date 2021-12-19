package boring.owl.parapp.entities.posts.roadmaps

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*
import javax.persistence.*


@Entity
@Table(name="roadmapnodes")
@ApiModel("Узел дерева")
class RoadmapNode() {
    @Id @GeneratedValue @Column(name = "node_id", nullable = false)
    @ApiModelProperty("Идентификационный номер дерева")
    var id: UUID? = null
    @Column(nullable = false) @ApiModelProperty(notes = "Название дерева")
    var title: String = ""
    @Column @ApiModelProperty(notes = "Описание дерева")
    var description: String = ""
    @ManyToMany(cascade = [CascadeType.MERGE]) @JoinColumn(name = "node_id")
    @ApiModelProperty(notes = "Список детей узла")
    var childrenNodes: MutableList<RoadmapNode> = mutableListOf()
    @Column @ApiModelProperty(notes = "Флаг, является ли узел дерева ключевым")
    var isMainWay: Boolean = false
    @OneToOne(mappedBy = "root")
    @ApiModelProperty(notes = "Дорожная карта, которой узел принадлежит")
    private val roadmap: Roadmap? = null

    constructor(title: String, description: String,
                childrenNodes: MutableList<RoadmapNode>, isMainWay: Boolean = false) : this() {
        this.title = title
        this.description = description
        this.childrenNodes = childrenNodes
        this.isMainWay = isMainWay
    }
}