package boring.owl.parapp.entities.response

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

@ApiModel("Модель данных, возвращаемая при ошибке")
class ErrorMessage(
    @ApiModelProperty(notes = "Код статуса")
    val statusCode: Int,
    @ApiModelProperty(notes = "Время ошибки")
    val timestamp: Date,
    @ApiModelProperty(notes = "Сообщение ошибки")
    val message: String,
    @ApiModelProperty(notes = "Ссылка, по которой произошла ошибка")
    val description: String
)