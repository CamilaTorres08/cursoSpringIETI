package edu.eci.ieti.integrador.controller.user;


import edu.eci.ieti.integrador.data.user.RoleEnum;
import edu.eci.ieti.integrador.data.user.UserEntity;
import edu.eci.ieti.integrador.data.user.UserService;
import edu.eci.ieti.integrador.exception.UserNotFoundException;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static edu.eci.ieti.integrador.utils.Constants.ADMIN_ROLE;


@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        loadSampleUsers();
    }

    public void loadSampleUsers() {
        if (passwordEncoder != null) {
            UserEntity userEntity = new UserEntity("Ada Lovelace", "ada@mail.com", passwordEncoder.encode("passw0rd"));
            userService.save(userEntity);
            UserEntity adminUserEntity = new UserEntity("Ada Admin", "admin@mail.com", passwordEncoder.encode("passw0rd"));
            adminUserEntity.addRole(RoleEnum.ADMIN);
            userService.save(adminUserEntity);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String id) {
        Optional<UserEntity> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        throw new UserNotFoundException("User not found");
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserDto userDto) {
        String fullName = userDto.getName() + userDto.getLastName();
        UserEntity user = new UserEntity(fullName,userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));
        UserEntity savedUser = userService.save(user);
        return ResponseEntity.created(URI.create("")).body(savedUser);
    }

    @RolesAllowed(ADMIN_ROLE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String id) {
        Optional<UserEntity> user = userService.findById(id);
        if (user.isPresent()) {
            userService.delete(user.get());
            return ResponseEntity.ok().build();
        }
        throw new UserNotFoundException("User not found");
    }

}
