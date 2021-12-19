package boring.owl.parapp.entities.patterns

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*
import javax.persistence.*

@Entity
@Table(name="patterns")
@ApiModel("Модель паттерна")
class Pattern {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    @ApiModelProperty(notes = "Идентификационный номер паттерна")
    var patternId: UUID? = null

    @Column
    @ApiModelProperty(notes = "Название паттерна")
    var title: String = ""
    @Column(length=3000)
    @ApiModelProperty(notes = "Описание паттерна")
    var description: String = ""
    @Column(length=3000)
    @ApiModelProperty(notes = "Проблема, решаемая паттерном")
    var problem: String = ""
    @Column(length=3000)
    @ApiModelProperty(notes = "Решение. предоставляемое паттерном")
    var solution: String = ""
    @Column(length=3000)
    @ApiModelProperty(notes = "Решение. предоставляемое паттерном в виде кода")
    var solutionInCode: String = ""
    @Column(length=3000)
    @ApiModelProperty(notes = "Вариант использования")
    var useCase: String = ""

    @OneToMany(cascade = [CascadeType.ALL])
    @ApiModelProperty(notes = "Список плюсов и минусов")
    var features: MutableList<PatternFeature>? = null

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(notes = "Тип паттерна")
    var type: PatternType = PatternType.UNKNOWN

    @Column
    @ApiModelProperty(notes = "Сложность реализации паттерна")
    var difficulty: Int = 0

    enum class PatternType(val title: String) {
        CREATIONAL("Порождающий"), STRUCTURAL("Структурный"), BEHAVIORAL("Поведенческий"), UNKNOWN("Неизвестный")

    }

    companion object {
        fun getType(title: String): PatternType {
            return when (title) {
                PatternType.STRUCTURAL.title -> PatternType.STRUCTURAL
                PatternType.CREATIONAL.title -> PatternType.CREATIONAL
                PatternType.BEHAVIORAL.title -> PatternType.BEHAVIORAL
                else -> PatternType.UNKNOWN
            }
        }
    }
}