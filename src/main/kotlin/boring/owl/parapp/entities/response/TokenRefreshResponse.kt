package boring.owl.parapp.entities.response

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Модель данных, получаемая при обновлении токена")
class TokenRefreshResponse(@ApiModelProperty(notes = "Токен доступа, который необходим для каждого POST, PATCH, PUT, DELETE запроса. Действителен 5 суток")
                           var accessToken: String,
                           @ApiModelProperty(notes = "Токен обновления токена доступа, действительный в течение 30 суток")
                           var refreshToken: String) {
    @ApiModelProperty(notes = "Тип токена, который необходимо указывать перед самим токеном в заголовке")
    var tokenType = "Bearer"

}