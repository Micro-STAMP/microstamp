package microstamp.authorization.controller;

import microstamp.authorization.dto.UserReadDto;
import microstamp.authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserReadDto> getMe(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.getMe(jwt), HttpStatus.OK);
    }
}
