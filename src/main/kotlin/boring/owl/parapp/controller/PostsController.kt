package boring.owl.parapp.controller

import boring.owl.parapp.entities.posts.Post
import boring.owl.parapp.entities.posts.notes.Note
import boring.owl.parapp.entities.posts.notes.NoteSection
import boring.owl.parapp.entities.posts.roadmaps.Roadmap
import boring.owl.parapp.entities.response.GenericResponse
import boring.owl.parapp.entities.response.ListResponse
import boring.owl.parapp.entities.response.Message
import boring.owl.parapp.service.PostsService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Valid

/**
 * Контроллер для работы с записками
 * Методы расположены по пути /api/
 */
@RestController
@RequestMapping("api")
@Api(description = "Конечные сетевые точки обращения для получения, обновления, добавления, удаления постов (Дорожных карт и Записей)")
class PostsController(val postsService: PostsService) {

    @PostMapping("/roadmap")
    @ApiOperation("Точка создания объекта дорожной карты по переданному объекту")
    fun addRoadmap(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String, @ApiParam("Объект дорожной карты") @RequestBody roadmap: Roadmap): Roadmap {
        return postsService.add(roadmap)
    }

    @RequestMapping(path= ["/roadmap"], method = [RequestMethod.PATCH, RequestMethod.PUT])
    @ApiOperation("Точка обновления объекта дорожной карты по переданному объекту")
    fun updateRoadmap(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String, @ApiParam("Объект дорожной карты") @RequestBody roadmap: Roadmap): Roadmap {
        return  postsService.update(roadmap)
    }

    @PostMapping("/note")
    @ApiOperation("Точка создания объекта записи по переданному объекту")
    fun addNote(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String, @ApiParam("Объект записи") @RequestBody note: Note): Note {
        return postsService.add(note)
    }

    @RequestMapping(path= ["/note"], method = [RequestMethod.PATCH, RequestMethod.PUT])
    @ApiOperation("Точка обновления объекта записи по переданному объекту")
    fun updateNote(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String, @ApiParam("Объект записи для обновления") @RequestBody note: Note): Note {
        return postsService.update(note)
    }

    @DeleteMapping("/post/{id}")
    @ApiOperation("Точка удаления объекта записи по переданному идентификационному номеру поста")
    fun deletePost(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String, @ApiParam("Идентификационный номер поста") @PathVariable id: UUID): GenericResponse {
        return try {
            postsService.delete(id)
            GenericResponse(Message.DELETED)
        } catch (e: Exception) {
            throw e
        }
    }


    @GetMapping("/bytopic/{id}")
    @ApiOperation("Точка получения списка всех постов, относящихся к топику," +
            " идентификационный номер которого передается")
    fun getAllById(
        @ApiParam("Идентификационный номер топика, откуда берутся посты")
        @PathVariable id: UUID
    ): ListResponse<Post> {
        return ListResponse(postsService.getAllByTopicId(id))
    }

    @GetMapping("/post/{id}")
    @ApiOperation("Точка получения объекта поста по переданному идентификационному номеру")
    fun <T : Post> getOne(@ApiParam("Идентификационный номер поста") @PathVariable id: UUID): T {
        return  postsService.getOne(id)
    }

}
