package edu.eci.ieti.integrador.controller.auth;

import edu.eci.ieti.integrador.data.user.UserEntity;
import edu.eci.ieti.integrador.data.user.UserService;
import edu.eci.ieti.integrador.exception.InvalidCredentialsException;
import edu.eci.ieti.integrador.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserService userService;

    private final JwtUtil jwtUtil;


    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        Optional<UserEntity> optionalUser = userService.findByEmail(loginDto.getUsername());
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            if (BCrypt.checkpw(loginDto.getPassword(), user.getPasswordHash())){
                TokenDto tokenDto = jwtUtil.generateToken(user.getEmail(), user.getRoles());
                return ResponseEntity.ok(tokenDto);
            } else{
                throw new InvalidCredentialsException();
            }
        }else{
            throw new InvalidCredentialsException();
        }
    }


}
