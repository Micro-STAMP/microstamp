package microstamp.authorization.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import microstamp.authorization.dto.UserInsertDto;
import microstamp.authorization.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(@RequestParam("client_id") String clientId,
                               @RequestParam("redirect_uri") String redirectUri,
                               @RequestParam("response_type") String responseType,
                               @RequestParam("scope") String scope,
                               Model model) {

        model.addAttribute("client_id", clientId);
        model.addAttribute("redirect_uri", redirectUri);
        model.addAttribute("response_type", responseType);
        model.addAttribute("scope", scope);
        return "registration";
    }

    @PostMapping("/registration")
    public String insert(@RequestParam("client_id") String clientId,
                               @RequestParam("redirect_uri") String redirectUri,
                               @RequestParam("response_type") String responseType,
                               @RequestParam("scope") String scope,
                               @Valid UserInsertDto userInsertDto,
                               Model model) {

        String urlParameters = "client_id=" +
                clientId +
                "&redirect_uri=" +
                redirectUri +
                "&response_type=" +
                responseType +
                "&scope=" +
                scope;

        try {
            userService.insert(userInsertDto);
        } catch(Exception ex){
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("client_id", clientId);
            model.addAttribute("redirect_uri", redirectUri);
            model.addAttribute("response_type", responseType);
            model.addAttribute("scope", scope);
            return "registration";
        }

        return "redirect:oauth2/authorize?" + urlParameters;
    }
}