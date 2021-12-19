package boring.owl.parapp.entities.patterns

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name="features")
@ApiModel("Модель особенности паттерна")
class PatternFeature {
    @Id
    @GeneratedValue
    @Column(name="feature_id", nullable = false)
    @ApiModelProperty(notes = "Идентификационный номер особенности паттерна")
    var featureId: UUID? = null

    @Column
    @ApiModelProperty(notes = "Название особенности паттерна")
    var title: String = ""

    @Column
    @ApiModelProperty(notes = "Флаг, является ли особенность плюсом")
    var isAdvantage: Boolean = true

}
