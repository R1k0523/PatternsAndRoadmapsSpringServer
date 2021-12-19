package boring.owl.parapp.service

import boring.owl.parapp.entities.user.User
import boring.owl.parapp.service.StringUtils.getRandomString
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*


@Service
class GitHubService {
    @Value("\${github.api}")
    private val api: String? = null
    @Value("\${github.loginapi}")
    private val loginapi: String? = null
    @Value("\${github.id}")
    private val clientId: String? = null
    @Value("\${github.secret}")
    private val clientSecret: String? = null
    @Value("\${github.redirect}")
    private val redirectUri: String? = null

    private val template = RestTemplate()
    
    fun getToken(code: String) : String {
        val uri = "$loginapi/access_token?" +
                "code=$code&" +
                "client_id=$clientId&" +
                "client_secret=$clientSecret&" +
                "redirect_uri=$redirectUri"
        println(uri)
        val (request, response, result) = uri
            .httpPost()
            .responseString()
        println(response.responseMessage)
        val token: AccessToken = Gson().fromJson(result.get(), AccessToken::class.java)
        return token.accessToken
    }

    fun getUserInfo(accessToken: String) : User {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers["Authorization"] = "Bearer $accessToken"

        val (request, response, result) = "$api/user"
            .httpGet().header(mapOf("Authorization" to "Bearer $accessToken"))
            .responseString()
        println(request)
        println(response.responseMessage)
        val info: UserResponse = Gson().fromJson(result.get(), UserResponse::class.java)
        return info.toUser(accessToken)
    }
}

class AccessToken {
    @SerializedName("access_token")
    val accessToken: String = ""
    @SerializedName("token_type")
    val tokenType: String = ""
}

data class UserResponse(
    val bio: String = "",
    val login: String = "",
    val company: String = "",
    val email: String = "",
    val url: String = "",
    val avatarUrl: String = "",
    val name: String = "",
    val location: String = "",
    val blog: String = ""
) {
    fun toUser(accessToken: String): User {
         val user = User()
         user.let {
            it.userId = UUID.randomUUID()
            it.bio = bio
            it.login = login
            it.password = getRandomString(20)
            it.role = User.Roles.USER
            it.company = company
            it.email = email
            it.url = url
            it.avatarUrl = avatarUrl
            it.name = name
            it.location =  location
            it.blog = blog
            it.token = accessToken
        }
        return user
    }
}