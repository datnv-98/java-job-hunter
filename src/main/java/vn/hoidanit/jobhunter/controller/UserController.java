package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<User> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.userService.getAllUsers(spec, pageable));
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User createdUser = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        User user = this.userService.getDetailUser(id);
        return user;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        this.userService.deleteUser(id);
        return "Deleted user with id: " + id;
    }

    @PutMapping("/user/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User userUpdate) {
        // TODO: process PUT request
        User usUpdate = this.userService.updateUser(id, userUpdate);
        return usUpdate;
    }
}
