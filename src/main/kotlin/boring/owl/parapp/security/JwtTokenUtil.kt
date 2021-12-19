package boring.owl.parapp.security


import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function

@Component
class JwtTokenUtil {
    @Value("\${jwt.validityseconds}")
    val validitySeconds: Long? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    private fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    fun <T> getClaimFromToken(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims: Claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }
    fun getUsernameFromToken(token: String?, hasBearer: Boolean = false) :  String {
        var tokenToCheck = token
        if (hasBearer)
            tokenToCheck = tokenToCheck!!.substring(7)
        val claims: Claims = getAllClaimsFromToken(tokenToCheck)
        return (claims::getSubject)()
    }
    fun getExpirationDateFromToken(token: String?): Date {
        val claims: Claims = getAllClaimsFromToken(token)
        return (claims::getExpiration)()
    }

    private fun isTokenExpired(token: String?): Boolean {
        val expiration: Date = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun generateToken(username: String): String {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, username)
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder().setClaims(claims).setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + validitySeconds!! * 1000))
            .signWith(SignatureAlgorithm.HS512, secret).compact()
    }
    
    fun validateToken(token: String?, userDetails: UserDetails): Boolean? {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

}