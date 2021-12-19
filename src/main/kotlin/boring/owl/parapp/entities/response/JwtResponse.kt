package boring.owl.parapp.entities.response

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

@ApiModel("Модель данных с токенами, которая возвращается при успешной аутентификации")
class JwtResponse(
    @ApiModelProperty(notes = "Токен доступа, который необходим для каждого POST, PATCH, PUT, DELETE запроса. Действителен 5 суток")
    var accessToken: String,
    @ApiModelProperty(notes = "Токен обновления токена доступа, действительный в течение 30 суток")
    var refreshToken: String,
    @ApiModelProperty(notes = "Идентификационный номер пользователя")
    var id: UUID,
    @ApiModelProperty(notes = "Имя пользователя")
    var username: String,
    val role: String
) : GenericResponse() { @ApiModelProperty(notes = "Тип токена, который необходимо указывать перед самим токеном в заголовке") val tokenType = "Bearer" }