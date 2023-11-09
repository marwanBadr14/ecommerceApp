package com.marwan.UserService.controller;
import com.marwan.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add-admin")
    public ResponseEntity<String> addAdmin(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        userService.addAdmin(firstName, lastName, email, password);

        return ResponseEntity.ok("Admin was added successfully");
    }

    @GetMapping("/get-user-email-by-id")
    public ResponseEntity<String> getUserEmailById(@RequestParam("id") Integer id) {
        String userEmail = userService.getUserEmailById(id);

        if (userEmail != null) {
            return ResponseEntity.ok(userEmail);
        } else {
            return new ResponseEntity<>("No user with this ID was found.", HttpStatus.NOT_FOUND);

        }
    }

}
