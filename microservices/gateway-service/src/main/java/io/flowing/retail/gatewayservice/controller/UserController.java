package io.flowing.retail.gatewayservice.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    /**
     * Method to test security
     * @param principal user principal
     * @return principal string representation
     */
    @GetMapping("/user")
    public String index(Principal principal) {
        return principal.toString();
    }
}