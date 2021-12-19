package boring.owl.parapp.controller

import boring.owl.parapp.security.JwtTokenUtil
import boring.owl.parapp.entities.RefreshToken
import boring.owl.parapp.entities.exceptions.TokenRefreshException
import boring.owl.parapp.entities.request.LogOutRequest
import boring.owl.parapp.entities.request.TokenRefreshRequest
import boring.owl.parapp.entities.response.*
import boring.owl.parapp.entities.request.AuthRequestEntity
import boring.owl.parapp.entities.user.User
import boring.owl.parapp.service.JwtUserDetailsService
import boring.owl.parapp.service.UserService
import boring.owl.parapp.service.RefreshTokenService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import javax.validation.Valid

@Controller
@Api(description = "Конечные сетевые точки обращения для аутентификации")
class AuthController(
    val jwtTokenUtil: JwtTokenUtil,
    val authenticationManager: AuthenticationManager,
    val userService: UserService,
    val userDetailsService: JwtUserDetailsService,
    var refreshTokenService: RefreshTokenService,
) {

    @PostMapping(value = ["/login"])
    @Throws(Exception::class)
    @ApiOperation("Точка аутентификации по логину и паролю")
    fun login(@ApiParam("Данные пользователя для аутентификации, хранящие логин и пароль")
              @RequestBody authenticationRequest: AuthRequestEntity): ResponseEntity<JwtResponse> {
        authenticate(authenticationRequest.username, authenticationRequest.password)
        return getJwt(authenticationRequest.username)
    }

    @PostMapping(value = ["/oauth"])
    @Throws(Exception::class)
    @ApiOperation("Точка регистрации по токену доступа провайдера GitHub")
    fun oauth(@ApiParam("Токен доступа провайдера GitHub")@RequestParam token: String): ResponseEntity<JwtResponse> {
        val user = userDetailsService.userByCode(token)
        return getJwt(user.login!!)
    }

    @PostMapping(value = ["/register"])
    @Throws(Exception::class)
    @ApiOperation("Точка регистрации по логину и паролю")
    fun register(@ApiParam("Данные пользователя для регистрации, хранящие логин и пароль") @RequestBody user: AuthRequestEntity): ResponseEntity<GenericResponse> {
        if (user.username.length < 6) {
            return ResponseEntity<GenericResponse>(
                GenericResponse(Message.USERNAME_SHORT), HttpStatus.UNAUTHORIZED
            )
        }
        if (user.password.length < 8) {
            return ResponseEntity<GenericResponse>(
                GenericResponse(Message.PASSWORD_SHORT), HttpStatus.UNAUTHORIZED
            )
        }
        if (userService.hasUser(user.username)) {
            return ResponseEntity<GenericResponse>(
                GenericResponse(Message.USER_EXISTS), HttpStatus.UNAUTHORIZED
            )
        }
        userDetailsService.register(user)
        return ResponseEntity.ok(GenericResponse(Message.USER_REGISTERED))
    }


    @PostMapping(value = ["/profile"])
    @Throws(Exception::class)
    @ApiOperation("Точка получения данных пользователя по токену")
    fun profile(@ApiParam("Токен доступа") @RequestHeader("Authorization") token: String): ResponseEntity<User> {
        val username = jwtTokenUtil.getUsernameFromToken(token.substring(7))
        return ResponseEntity.ok(userService.getOne(username))
    }

    @PostMapping("/refreshtoken")
    @ApiOperation("Точка обновления токена доступа с помощью токена обновления")
    fun refreshtoken(@ApiParam("Токен обновления токена доступа") @RequestBody request: @Valid TokenRefreshRequest): ResponseEntity<TokenRefreshResponse> {
        val requestRefreshToken: String = request.refreshToken!!
        val token = refreshTokenService.findByToken(requestRefreshToken)

        if (token != null) {
            refreshTokenService.verifyExpiration(token)
            val user = token.user ?: throw TokenRefreshException(requestRefreshToken, "Refresh token is not in database!")
            val newToken: String =
                jwtTokenUtil.generateToken(user.login!!)
            return ResponseEntity.ok(TokenRefreshResponse(newToken, requestRefreshToken))
        }

        throw TokenRefreshException(requestRefreshToken, "Refresh token is invalid!")
    }

    @PostMapping("/loggingout")
    @ApiOperation("Точка выхода пользователя для удаления токена доступа")
    fun logoutUser(@ApiParam("Идентификационный номер пользователя") @RequestBody logOutRequest: @Valid LogOutRequest): ResponseEntity<GenericResponse> {
        refreshTokenService.deleteByUserId(logOutRequest.userId!!)
        return ResponseEntity.ok(GenericResponse("Log out successful!"))
    }


    @Throws(Exception::class)
    private fun authenticate(username: String, password: String) {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, password)
            )
        } catch (e: BadCredentialsException) {
            throw Exception("Login Error: ${e.message}")
        }
    }

    private fun getJwt(username: String): ResponseEntity<JwtResponse> {
        try {
            val userDetails = userDetailsService.loadUserByUsername(username)
            val token = jwtTokenUtil.generateToken(userDetails.username)
            val refreshToken: RefreshToken =
                refreshTokenService.createRefreshToken(userDetails.username)
            return ResponseEntity.ok(JwtResponse(
                accessToken = token,
                refreshToken = refreshToken.token,
                id = userDetails.id,
                username = userDetails.username,
                role = userDetails.role.toString(),))
        } catch (e: Exception) {
            throw Exception("OAuth2 exception: ${e.message}")
        }
    }
}