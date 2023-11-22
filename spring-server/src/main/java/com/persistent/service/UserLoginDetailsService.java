package com.persistent.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

import com.persistent.exception.UserNotFoundException;
import com.persistent.model.User;
import com.persistent.service.UserRepository;

@Service
@Log4j2
public class UserLoginDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserLoginDetailsService() {
    }

    /**
     * This method is used to load the user details from the database.
     * 
     * @param username The username of the user to be loaded from the database.
     * @return UserDetails The UserDetails object containing the user details.
     * @throws UserNotFoundException If the username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<User> user = userRepository.findByUsername(username);
        User u = null;
        try {
            u = user.get();
        } catch (NoSuchElementException e) {
            log.error("User with username - " + username +
                    " not found");
            throw new UserNotFoundException("Access Denied");
        }
        return new org.springframework.security.core.userdetails.User(u.getUsername(),
                u.getPassword(),
                new ArrayList<>());
    }

}