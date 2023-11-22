/**
 * This class is used to authenticate the user and generate the token
 *
 */
package com.persistent.api;

import com.persistent.model.AuthenticationRequestBody;
import com.persistent.model.AuthenticationResponseBody;
import com.persistent.service.UserLoginDetailsService;
import com.persistent.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/v3/auth")
@Log4j2
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserLoginDetailsService userDetailsService;

    /**
     * This method is used to authenticate the user and generate the token
     * 
     * @param AuthenticationRequestBody The request body object to be used to authenticate the user
     * @return ResponseEntity<?> The response object for the request with the token
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestBody authReqBody) {
        System.out.println("Auth Details: " + authReqBody);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authReqBody.getUsername(),
                authReqBody.getPassword());

        Authentication authResult = authManager.authenticate(token);

        System.out.println();
        if (authResult.isAuthenticated())
            log.info("User is Authenticated");

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authReqBody.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponseBody(jwt));
    }

}