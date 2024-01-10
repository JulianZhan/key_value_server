package com.keyvalueserver.project.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.keyvalueserver.project.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /*
    this filter first checks if the request has a valid JWT token,
    which means the request header has Authorization header starting with Bearer
    and then the username can be extracted from the token payload

    once we get the username, we load the user details from the database
    then we verify the token and signature, and set the authentication
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // get authorization part of the header
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        // JWT starts with "Bearer ", remove it and only keep the token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            // extract the token
            jwtToken = requestTokenHeader.substring(7);
            try {
                // get the username from the token, which will be used to get the user details
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        /*
        once we checked the token exists in the header, we use the token to get the user details
        then we validate the token in this step
        SecurityContextHolder.getContext().getAuthentication() returns null if the token is invalid
         */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // load user details from the database, which will be used to create the authentication object
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // if token is valid configure Spring Security to manually set authentication
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // after setting the authentication in the context, specify that the current user is authenticated
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // continue the filter chain
        chain.doFilter(request, response);
    }

}
