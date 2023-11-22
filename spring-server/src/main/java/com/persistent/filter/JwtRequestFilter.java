/**
 * This class is used to filter the request and validate the JWT token.
 */
package com.persistent.filter;

import com.persistent.service.UserLoginDetailsService;
import com.persistent.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserLoginDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private final String HEADER_STRING = "Authorization";
    private final String TOKEN_PREFIX =  "Bearer ";

    /**
     * This method is used to filter the request and validate the JWT token.
     * 
     * @param request The request object to be filtered and validated for JWT token 
     * @param response The response object for the request
     * @param chain The filter chain object to be used to filter the request
     * 
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HEADER_STRING);

        String username = null;
        String jwt = null;
        try {
            // Check if the header is not null and starts with TOKEN_PREFIX
            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
                // Extract the JWT token from the header
                jwt = authorizationHeader.substring(TOKEN_PREFIX.length());
                try{
                    username = jwtUtil.extractUsername(jwt);
                } catch (IllegalArgumentException e) {
                    log.error("an error occured during getting username from token", e);
                } catch (ExpiredJwtException e) {
                    log.warn("the token is expired and not valid anymore", e);
                }
            }
        } catch (MalformedJwtException e) {
            String message = "No JWT token found in request headers";
            log.error(message);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtUtil.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}