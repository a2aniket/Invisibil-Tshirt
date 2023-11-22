/**
 * This class is used to authenticate user.
 */
package com.persistent.security;

import com.persistent.exception.IncorrectCredentialsException;
import com.persistent.service.UserLoginDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private UserLoginDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * This method is called by Spring Security when a user tries to login. It
     * authenticates the user and returns an Authentication object if successful.
     * 
     * @param authentication The Authentication object that contains the user details.
     * @return Authentication The Authentication object if successful authentication is done.
     * @throws IncorrectCredentialsException If provided credentials re incorrect.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {


        // Get the User from UserDetailsService
        String providedUsername = authentication.getPrincipal().toString();
        UserDetails user = userDetailsService.loadUserByUsername(providedUsername);
        log.info("User Details from UserService based on username-" + providedUsername + " : " + user);

        String providedPassword = authentication.getCredentials().toString();
        log.info("Provided Password after encoding: " + providedPassword + "");
        String correctPassword = user.getPassword();

        // Authenticate
        // If Passwords do not match throw an exception
        if (!passwordEncoder.matches(providedPassword, correctPassword)) {
            String message = "Incorrect Credentials";
            log.error(message);
            throw new IncorrectCredentialsException(message);
        }

        log.info("Passwords Match....\n");

        Authentication authenticationResult = new UsernamePasswordAuthenticationToken(user,
                authentication.getCredentials(), user.getAuthorities());
        return authenticationResult;
    }

    /**
     * This method is called by Spring Security to check if this
     * AuthenticationProvider
     * supports the type of Authentication object passed to it.
     * 
     * @param authentication The Authentication object that contains the user details
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}