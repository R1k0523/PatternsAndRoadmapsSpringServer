package boring.owl.parapp.controller

import boring.owl.parapp.security.JwtTokenUtil
import boring.owl.parapp.entities.patterns.Pattern
import boring.owl.parapp.entities.response.GenericResponse
import boring.owl.parapp.entities.response.ListResponse
import boring.owl.parapp.entities.response.Message
import boring.owl.parapp.service.PatternService
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
@RequestMapping("api/pattern")
@Api(description = "Конечные сетевые точки обращения для получения, обновления, добавления, удаления паттернов")
class PatternsController(
    val jwtTokenUtil: JwtTokenUtil,
    val patternsService: PatternService
) {

    @PostMapping
    @ApiOperation("Точка создания объекта паттерна по переданному объекту")
    fun addPattern(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String,
                   @ApiParam("Объект паттерна") @RequestBody pattern: Pattern): Pattern {
        val username = jwtTokenUtil.getUsernameFromToken(token, true)
        return patternsService.add(username, pattern)
    }
    @RequestMapping( method = [RequestMethod.PATCH, RequestMethod.PUT])
    @ApiOperation("Точка обновления объекта паттерна по переданному объекту")
    fun updatePattern(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String,
                      @ApiParam("Объект паттерна") @RequestBody pattern: Pattern): Pattern {
        val username = jwtTokenUtil.getUsernameFromToken(token, true)
        return  patternsService.update(username, pattern)
    }
    @DeleteMapping("/{id}")
    @ApiOperation("Точка удаления объекта паттерна по переданному идентификационному номеру")
    fun deletePattern(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String,
                      @ApiParam("Идентификационный номер паттерна") @PathVariable id: UUID): GenericResponse {
        val username = jwtTokenUtil.getUsernameFromToken(token, true)
        patternsService.delete(username, id)
        return GenericResponse(Message.DELETED)
    }


    @GetMapping
    @ApiOperation("Точка получения списка всех паттернов из базы данных")
    fun getAll(): ListResponse<Pattern> {
        return ListResponse(patternsService.getAll())
    }

    @GetMapping("/{id}")
    @ApiOperation("Точка получения объекта паттерна по переданному идентификационному номеру")
    fun getOne(@ApiParam("Идентификационный номер паттерна") @PathVariable id: UUID): Pattern {
        return  patternsService.getOne(id)
    }

}
