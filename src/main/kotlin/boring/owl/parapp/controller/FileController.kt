package boring.owl.parapp.controller

import boring.owl.parapp.entities.response.UploadFileResponse
import boring.owl.parapp.service.FileStorageService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import javax.servlet.http.HttpServletRequest


@RestController("api")
@Api(description = "Конечные сетевые точки обращения для загрузки и получения файлов")
class FileController(private val fileStorageService: FileStorageService) {
    @PostMapping("/uploadFile")
    @ApiOperation("Точка для загрузки файла на сервер")
    fun uploadFile(@ApiParam("Токен доступа")
                   @RequestHeader("Authorization") token: String,
                   @ApiParam("Файл, который необходимо загрузить")
                   @RequestParam("file") file: MultipartFile): UploadFileResponse {
        val fileName: String = fileStorageService.storeFile(file)
        val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/downloadFile/")
            .path(fileName)
            .toUriString()
        return UploadFileResponse(
            fileName, fileDownloadUri,
            file.contentType!!, file.size
        )
    }

    @PostMapping("/uploadMultipleFiles")
    @ApiOperation("Точка для загрузки множества файлов на сервер")
    fun uploadMultipleFiles(@ApiParam("Токен доступа")
                            @RequestHeader("Authorization") token: String,
                            @ApiParam("Файлы, которые необходимо загрузить")
                            @RequestParam("files") files: Array<MultipartFile>): List<UploadFileResponse> {
        val response = arrayListOf<UploadFileResponse>()
        files.forEach {
            response.add(uploadFile(token, it))
        }
        return response
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    @ApiOperation("Точка для скачивания файла по названию с сервера")
    fun downloadFile(
        @ApiParam("Название файла для скачивания")
        @PathVariable fileName: String,
        request: HttpServletRequest
    ): ResponseEntity<Resource> {
        val resource: Resource = fileStorageService.loadFileAsResource(fileName)

        var contentType: String? = null
        try {
            contentType =
                request.servletContext.getMimeType(resource.getFile().getAbsolutePath())
        } catch (ex: IOException) {}

        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename().toString() + "\""
            )
            .body(resource)
    }

}