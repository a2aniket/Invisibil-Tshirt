package com.persistent.model;

/**
 * This class is used to return the JWT token to the client
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class AuthenticationResponseBody {

    private final String jwt;
}