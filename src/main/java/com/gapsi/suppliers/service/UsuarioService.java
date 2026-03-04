package com.gapsi.suppliers.service;

import com.gapsi.suppliers.model.Proveedor;
import com.gapsi.suppliers.model.ResponseCompose;
import com.gapsi.suppliers.model.Usuario;
import com.gapsi.suppliers.model.login.Login;
import com.gapsi.suppliers.model.pojo.PojoUsuario;
import com.gapsi.suppliers.repository.ProveedorRepo;
import com.gapsi.suppliers.repository.UsuariosRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    UsuariosRepo usuariosRepo;

    @Autowired
    ProveedorRepo proveedorRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseCompose save(Usuario usuario) {
        ResponseCompose responseCompose = new ResponseCompose();
        String passwordEncoded = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(passwordEncoded);
        responseCompose.setObjeto(usuariosRepo.save(usuario));
        responseCompose.setStatus(true);
        responseCompose.setMsg("Usuario almacenado correctamente");
        return responseCompose;

    }

    @Override
    public Optional<PojoUsuario> findByNombre(String correo) {
        PojoUsuario pojo = new PojoUsuario();
        Usuario usuario = usuariosRepo.findByNombre(correo).orElse(null);
        if (Objects.isNull(usuario)) {
            return Optional.empty();
        }
        LOGGER.info("El usuario logeado tiene correo " + usuario.getNombre());

        pojo.setId(usuario.getId());
        pojo.setNombre(usuario.getNombre());
        pojo.setRolApp(usuario.getRol());
        pojo.setVersionApp("v1.0");
        pojo.setPassword(usuario.getPassword());
        return Optional.of(pojo);
    }

    @Override
    public List<PojoUsuario> findAll() {
        List<Usuario> usuariosFound = usuariosRepo.findAll();
        return usuariosFound.stream()
                .map(usuario -> PojoUsuario.builder()
                        .id(usuario.getId())
                        .nombre(usuario.getNombre())
                        .rolApp(usuario.getRol())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public PojoUsuario findById(Long id, String authHeader) {
        Usuario usuarioFound = usuariosRepo.findById(id).orElse(null);
        return PojoUsuario
                .builder()
                .id(usuarioFound.getId())
                .nombre(usuarioFound.getNombre())
                .rolApp(usuarioFound.getRol())
                .token(authHeader)
                .build();
    }


    @Override
    public ResponseCompose actualizarPassword(Login nuevaClave) {
        ResponseCompose responseCompose = new ResponseCompose();

        Usuario usuario = usuariosRepo.findByNombre(nuevaClave.getNombre()).orElse(null);
        if (Objects.isNull(usuario)) {
            responseCompose.setStatus(false);
            responseCompose.setMsg("usuario no encontrado");
            return responseCompose;
        }
        String passwordEncoded = passwordEncoder.encode(nuevaClave.getPwd());
        usuario.setPassword(passwordEncoded);

        try {
            usuariosRepo.save(usuario);
            responseCompose.setStatus(true);
            responseCompose.setObjeto("Contraseña actualizada");
            responseCompose.setMsg("Procedimiento correcto");
            return responseCompose;
        } catch (Exception e) {
            responseCompose.setStatus(false);
            responseCompose.setMsg("Operacion incorrecta");
            return responseCompose;
        }
    }

    private PojoUsuario convertirAPojoUsuario(Usuario usuario) {
        PojoUsuario pojo = new PojoUsuario();
        pojo.setId(usuario.getId());
        pojo.setNombre(usuario.getNombre());
        pojo.setVersionApp("v1.0");
        return pojo;
    }
}
