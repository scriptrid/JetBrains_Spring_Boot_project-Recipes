package recipes.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.model.dto.CreateUserDto;
import recipes.service.UserService;

import javax.validation.Valid;

@RestController
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/register")
    public void register(@RequestBody @Valid CreateUserDto dto) {
        userService.addUser(dto);
    }
}
