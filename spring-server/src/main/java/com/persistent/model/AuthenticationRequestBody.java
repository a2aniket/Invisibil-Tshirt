package com.persistent.model;

/**
 * This class is used to represent the body of the authentication request.
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestBody {

    private String username;
    private String password;

    @Override
    public String toString() {
        return "AuthReqBody [username=" + username + "]";
    }

}