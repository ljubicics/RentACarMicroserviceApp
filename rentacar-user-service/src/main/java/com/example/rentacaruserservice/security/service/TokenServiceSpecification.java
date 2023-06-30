package com.example.rentacaruserservice.security.service;

import io.jsonwebtoken.Claims;

public interface TokenServiceSpecification {
    String generate(Claims claims);
    Claims parseToken(String jwt);
}
