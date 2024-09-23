package microstamp.authorization.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import microstamp.authorization.dto.UserInsertDto;
import microstamp.authorization.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public RedirectView insert(@Valid UserInsertDto userInsertDto) {
        userService.insert(userInsertDto);
        return new RedirectView("login");
    }
}