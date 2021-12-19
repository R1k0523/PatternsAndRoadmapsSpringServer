package boring.owl.parapp.security

import boring.owl.parapp.service.UserService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtRequestFilter(val userService: UserService) : OncePerRequestFilter() {

    @Autowired
    private val jwtTokenUtil: JwtTokenUtil? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val requestTokenHeader = request.getHeader("Authorization")
        var username: String? = null
        var jwtToken: String? = null
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7)
            try {
                username = jwtTokenUtil!!.getUsernameFromToken(jwtToken)
            } catch (e: IllegalArgumentException) {
                logger.info("Unable to get JWT Token")
            } catch (e: ExpiredJwtException) {
                logger.info("JWT Token has expired")
            } catch (e: MalformedJwtException) {
                logger.info("JWT Token is not valid")
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String")
        }
        if (username != null &&
            SecurityContextHolder.getContext().authentication == null &&
            userService.hasUser(username)) {
            val userDetails: UserDetails = userService.loadUserByLogin(username)
            if (jwtTokenUtil!!.validateToken(jwtToken, userDetails)!!) {
                val token = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                token.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = token
            }
        }
        chain.doFilter(request, response)
    }
}
