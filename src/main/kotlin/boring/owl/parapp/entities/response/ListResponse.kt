package boring.owl.parapp.entities.response

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Модель данных для возвращения списка объектов")
class ListResponse<T> (@ApiModelProperty(notes = "Список объектов") val items: List<T>)