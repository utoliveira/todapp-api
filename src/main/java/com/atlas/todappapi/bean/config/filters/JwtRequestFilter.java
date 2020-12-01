package com.atlas.todappapi.bean.config.filters;

import com.atlas.todappapi.bean.service.UserService;
import com.atlas.todappapi.bean.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        try{
            Long userId= getUserIdFromJwt(authorizationHeader);

            /*Só realiza a autenticação se o context já não tiver tido uma autenticação*/
            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.getUserDetailsByUserId(userId);

                /*This is what Spring use for manage the authentication in the context of username and password*/
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }catch (Exception e){
            logger.error("Something went wrong during the authentication JWT: {} {}", e.getClass().getName(), e.getMessage());
        }

        chain.doFilter(request, response);
    }

    private Long getUserIdFromJwt(String authorizationHeader){
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return Long.valueOf(
                jwtUtil.extractUserId(authorizationHeader.substring(7)));
    }

}
