package boring.owl.parapp.entities.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

class FileStorageException : RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}
@ResponseStatus(HttpStatus.NOT_FOUND)
class MyFileNotFoundException : RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}
@ResponseStatus(HttpStatus.FORBIDDEN)
class TokenRefreshException(token: String?, message: String?) :
    RuntimeException(String.format("Failed for [%s]: %s", token, message)) {
    companion object {
        private const val serialVersionUID = 1L
    }
}