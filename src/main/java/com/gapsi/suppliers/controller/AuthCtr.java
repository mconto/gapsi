package com.gapsi.suppliers.controller;

import com.gapsi.suppliers.config.JwtService;
import com.gapsi.suppliers.model.ResponseCompose;
import com.gapsi.suppliers.model.ResponseHTTP;
import com.gapsi.suppliers.model.Usuario;
import com.gapsi.suppliers.model.login.Login;
import com.gapsi.suppliers.model.pojo.PojoUsuario;
import com.gapsi.suppliers.service.MensajeriaService;
import com.gapsi.suppliers.service.UsuarioService;
import com.gapsi.suppliers.util.ValidationFactory;
import com.gapsi.suppliers.util.ValidationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/gapsi/auth")
public class AuthCtr {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    MensajeriaService mensajeriaService;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthCtr(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/version")
    public String version() {
        return "version 2.0 en http";
    }

    @PostMapping("/")
    public ResponseEntity<ResponseHTTP> guardarUsuario(@RequestBody Usuario usuarioInput) {
        if (!ValidationFactory.validateUsuarios(usuarioInput)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }
            String pwd = usuarioInput.getPassword();
            try {
                ResponseCompose responseCompose = new ResponseCompose();
                PojoUsuario pojoUsuario = usuarioService.findByNombre(usuarioInput.getNombre()).orElse(null);

                if (Objects.nonNull(pojoUsuario)) {
                    responseCompose.setMsg("Usuario existente.");
                    return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), responseCompose.getMsg()),
                            HttpStatus.FORBIDDEN);
                }

                usuarioService.save(usuarioInput);
                usuarioInput.setPassword(pwd);

                String token = jwtService.generateToken(usuarioInput.getNombre());

                System.out.println("Token generado ---------------------");

                PojoUsuario usuario = usuarioService.findByNombre(usuarioInput.getNombre()).orElse(null);
                usuario.setToken(token);
                usuario.setPassword("");

                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), usuario),
                        HttpStatus.CREATED);

            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), null),
                        HttpStatus.BAD_GATEWAY);
            }
        }


    @PostMapping("/login")
    public ResponseEntity<ResponseHTTP> login(@RequestBody Login login) {
        if (!ValidationFactory.validateLogin(login)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getNombre(), login.getPwd())
            );

            String token = jwtService.generateToken(authentication.getName());
            PojoUsuario usuario = usuarioService.findByNombre(login.getNombre()).orElse(null);

            if (Objects.isNull(usuario)) {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), null),
                        HttpStatus.FORBIDDEN);
            }

            usuario.setToken(token);
            usuario.setPassword("");

            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), usuario),
                    HttpStatus.CREATED);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), null),
                    HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/token/{correo}")
    public ResponseEntity<ResponseHTTP> generarToken(@PathVariable("correo") String correo) {

        if (Objects.isNull(correo)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }

        String token = jwtService.generateToken(correo);

        try {
            mensajeriaService.enviarCorreo(correo, token);
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), "token enviado"),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/cambiar")
    public ResponseEntity<ResponseHTTP> cambiarClave(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                     @RequestBody Login nuevaClave) {

        ValidationToken validationToken = new ValidationToken(jwtService);

        if (!validationToken.validationToken(authHeader)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), null),
                    HttpStatus.FORBIDDEN);
        }

        if (Objects.isNull(nuevaClave)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            usuarioService.actualizarPassword(nuevaClave);
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), "password actualizado"),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
