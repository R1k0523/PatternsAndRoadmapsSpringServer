package boring.owl.parapp.entities.request

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotBlank

@ApiModel("Модель данных, отправляемая для обновления токена")
class TokenRefreshRequest {
    @ApiModelProperty(notes = "Токен обновления токена доступа, действительный в течение 30 суток")
    var refreshToken: @NotBlank String? = null
}