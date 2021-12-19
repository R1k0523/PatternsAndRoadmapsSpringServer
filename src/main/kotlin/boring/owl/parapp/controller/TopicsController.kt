package boring.owl.parapp.controller

import boring.owl.parapp.entities.posts.Topic
import boring.owl.parapp.entities.response.GenericResponse
import boring.owl.parapp.entities.response.ListResponse
import boring.owl.parapp.entities.response.Message
import boring.owl.parapp.service.TopicsService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * Контроллер для работы с записками
 * Методы расположены по пути /api/
 */
@RestController
@RequestMapping("api/topic")
@Api(description = "Конечные сетевые точки обращения для получени, обновления, добавления, удаления топиков")
class TopicsController(val topicsService: TopicsService) {

    @PostMapping
    @ApiOperation("Точка создания объекта топика по переданному объекту")
    fun addTopic(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String, @ApiParam("Объект топика") @RequestBody topic: Topic): Topic {
        return topicsService.add(topic)
    }

    @RequestMapping( method = [RequestMethod.PATCH, RequestMethod.PUT])
    @ApiOperation("Точка обновления объекта топика по переданному объекту")
    fun updateTopic(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String, @ApiParam("Объект топика") @RequestBody topic: Topic): Topic {
        return  topicsService.update(topic)
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Точка удаления объекта топика по переданному идентификационному номеру")
    fun deleteTopic(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String, @ApiParam("Идентификационный номер топика") @PathVariable id: UUID): GenericResponse {
        topicsService.delete(id)
         return GenericResponse(Message.DELETED)
    }

    @GetMapping("")
    @ApiOperation("Точка получения списка всех топиков из базы данных")
    fun getAllById(): ListResponse<Topic> {
        return ListResponse(topicsService.getAll())
    }

    @GetMapping("/{id}")
    @ApiOperation("Точка получения объекта паттерна по переданному идентификационному номеру")
    fun getOne(@ApiParam("Идентификационный номер топика") @PathVariable id: UUID): Topic {
        return  topicsService.getOne(id)
    }

}
