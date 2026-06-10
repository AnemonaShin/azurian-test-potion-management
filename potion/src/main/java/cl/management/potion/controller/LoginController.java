package cl.management.potion.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.management.potion.dto.response.DefaultResponse;
import cl.management.potion.exception.ServiceException;
import cl.management.potion.service.LoginService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Rest controller designed for Login users to the system.
 * 
 * @author Christian Ramirez (cramireza1997@gmail.com)
 * @since 09-06-2026
 * @version 1.0.1
 */
@RestController
@RequestMapping(path = "/login")
public class LoginController {

    private LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    /**
     * Generates a new token to be used inside functionalities and works as a Login
     * itselfs.
     * 
     * @param token
     * @return
     * @throws ServiceException
     */
    @GetMapping("/")
    public ResponseEntity<DefaultResponse> getLoginToken(
            @RequestHeader(name = "Authorization", required = true) String token) throws ServiceException {
        return ResponseEntity.ok().body(service.getLoginToken(token));
    }

}
