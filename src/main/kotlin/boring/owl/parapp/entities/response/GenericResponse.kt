package boring.owl.parapp.entities.response

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Модель данных, показывающая результат выполнения запроса")
open class GenericResponse(
    @ApiModelProperty(notes = "Сообщение о результате выполнения")
    val message: String = Message.OK
)

