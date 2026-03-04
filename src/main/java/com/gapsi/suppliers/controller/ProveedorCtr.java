package com.gapsi.suppliers.controller;

import com.gapsi.suppliers.config.JwtService;
import com.gapsi.suppliers.model.Proveedor;
import com.gapsi.suppliers.model.ResponseCompose;
import com.gapsi.suppliers.model.ResponseHTTP;
import com.gapsi.suppliers.model.pojo.PojoProveedor;
import com.gapsi.suppliers.model.pojo.PojoUsuario;
import com.gapsi.suppliers.service.ProveedorService;
import com.gapsi.suppliers.service.UsuarioService;
import com.gapsi.suppliers.util.ValidationFactory;
import com.gapsi.suppliers.util.ValidationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "Authorization", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RequestMapping("/gapsi/proveedores")
public class ProveedorCtr {

    @Autowired
    ProveedorService proveedorService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    JwtService jwtService;

    @GetMapping("/version")
    public String version() {
        return "1.0";
    }

    @GetMapping("/admin/{idRol}")
    public ResponseEntity<ResponseHTTP> retornaProveedores(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                           Pageable pageable,
                                                           @PathVariable("idRol") int idRol) {
        ValidationToken validationToken = new ValidationToken(jwtService);

        if (!validationToken.validationToken(authHeader)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), "Credenciales incorrectas para visualizar todas los proveedores registrados."),
                    HttpStatus.FORBIDDEN);
        }

            Page<PojoProveedor> proveedor;
            try {
                proveedor = proveedorService.findAll(idRol, pageable);
                if (Objects.isNull(proveedor)) {
                    return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), "Credenciales incorrectas para visualizar todos proveedores registrados."),
                            HttpStatus.FORBIDDEN);
                }
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), proveedor),
                        HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), null),
                        HttpStatus.BAD_GATEWAY);
            }
        }

    @GetMapping("/{nombre}")
    public ResponseEntity<ResponseHTTP> retornaProveedor
            (@RequestHeader(value = "Authorization", required = false) String authHeader,
             @PathVariable("nombre") String nombre) {

        ValidationToken validationToken = new ValidationToken(jwtService);

        if (!validationToken.validationToken(authHeader)) {

            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), null),
                    HttpStatus.FORBIDDEN);
        }


        if (Objects.isNull(nombre)) {

            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campo id requerido"),
                    HttpStatus.BAD_GATEWAY);
        }

        PojoProveedor proveedor;

        try {
            proveedor = proveedorService.findByNombre(nombre);
            if (Objects.nonNull(proveedor.getId())) {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), proveedor),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.NOT_FOUND.value(), proveedor),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), null),
                    HttpStatus.BAD_GATEWAY);
        }
    }


    @PostMapping("/")
    public ResponseEntity<ResponseHTTP> guardarProveedor
            (@RequestHeader(value = "Authorization", required = false) String authHeader,
             @RequestBody Proveedor proveedor) {

        ValidationToken validationToken = new ValidationToken(jwtService);

        if (!validationToken.validationToken(authHeader)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), null),
                    HttpStatus.FORBIDDEN);
        }
        if (!ValidationFactory.validateProveedor(proveedor)) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }

        ResponseCompose responseCompose;
        try {
            responseCompose = proveedorService.save(proveedor);
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), responseCompose),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), null),
                    HttpStatus.BAD_GATEWAY);
        }

    }


    @PutMapping("/{nombre}")
    public ResponseEntity<ResponseHTTP> actualizarProveedor
            (@RequestHeader(value = "Authorization", required = false) String authHeader,
             @RequestBody Proveedor proveedor,
             @PathVariable("nombre") String nombre) {
        ValidationToken validationToken = new ValidationToken(jwtService);

        if (!validationToken.validationToken(authHeader)) {

            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.FORBIDDEN.value(), null),
                    HttpStatus.FORBIDDEN);
        }

        if (!ValidationFactory.validateProveedor(proveedor)) {

            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_REQUEST.value(), "campos obligatorios requeridos"),
                    HttpStatus.BAD_REQUEST);
        }

        ResponseCompose responseCompose = new ResponseCompose();
        try {
            PojoUsuario pojoUsuario = usuarioService.findByNombre(nombre).orElse(null);
            if (Objects.isNull(pojoUsuario)) {
                return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), null),
                        HttpStatus.BAD_GATEWAY);
            }
            responseCompose = proveedorService.updateProveedor(proveedor);
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.CREATED.value(), responseCompose),
                    HttpStatus.CREATED);


        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseHTTP(HttpStatus.BAD_GATEWAY.value(), null),
                    HttpStatus.BAD_GATEWAY);
        }
    }

}