package com.marwan.UserService.controller;
import com.marwan.UserService.dto.UserDTO;
import com.marwan.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //@PreAuthorize("hasAuthority(T(com.marwan.UserService.repository.Role).MANAGER)")
    @PostMapping("/add-admin")
    public ResponseEntity<String> addAdmin(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        userService.addAdmin(firstName, lastName, email, password);

        return ResponseEntity.ok("Admin was added successfully");
    }

    @DeleteMapping("/delete-admin")
    public ResponseEntity<String> deleteAdmin(
            @RequestParam("email") String email) {

        try{
            userService.deleteAdmin(email);
        }
        catch (Exception e){
            throw new RuntimeException("Admin was not found, make sure you are providing the right email");
        }

        return ResponseEntity.ok("Admin was deleted successfully");
    }

    @GetMapping("/admins")
    public ResponseEntity<List<UserDTO>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmins());
    }


    @GetMapping("/get-user-email-by-id")
    public ResponseEntity<String> getUserEmailById(@RequestParam("id") Integer id) {
        String userEmail = userService.getUserEmailById(id);

        if (userEmail != null) {
            return ResponseEntity.ok(userEmail);
        } else {
            return new ResponseEntity<String>("No user with this ID was found.", HttpStatus.NOT_FOUND);

        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
