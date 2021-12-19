package boring.owl.parapp.controller

import boring.owl.parapp.entities.response.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.util.*

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(value = [Exception::class])
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleException(
        e: Exception,
        request: WebRequest
    ): ErrorMessage {
        return ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            Date(),
            e.message!!,
            request.getDescription(false)
        )
    }
}