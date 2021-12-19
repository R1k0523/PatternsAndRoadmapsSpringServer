package boring.owl.parapp.entities.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

@ApiModel("Модель данных для выхода из системы")
class LogOutRequest {
    @ApiModelProperty(notes = "Идентификационный номер пользователя")
    val userId: UUID? = null
}