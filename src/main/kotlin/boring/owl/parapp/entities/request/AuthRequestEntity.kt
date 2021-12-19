package boring.owl.parapp.entities.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Модель данных для аутентификации и регистрации")
class AuthRequestEntity (
    @ApiModelProperty(notes = "Логин или имя пользователя")
    val username: String = "",
    @ApiModelProperty(notes = "Пароль")
    val password: String = ""
)