package boring.owl.parapp.entities.response

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty


@ApiModel("Модель данных об успешной загрузке файла")
class UploadFileResponse(
    @ApiModelProperty(notes = "Имя файла")
    var fileName: String,
    @ApiModelProperty(notes = "Ссылка для скачивания")
    var fileDownloadUri: String,
    @ApiModelProperty(notes = "Тип файла")
    var fileType: String,
    @ApiModelProperty(notes = "Размер файла")
    var size: Long
)