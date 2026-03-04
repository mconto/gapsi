package com.gapsi.suppliers.service;


import com.gapsi.suppliers.model.ResponseCompose;
import com.gapsi.suppliers.model.Usuario;
import com.gapsi.suppliers.model.login.Login;
import com.gapsi.suppliers.model.pojo.PojoUsuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<PojoUsuario> findAll();
    ResponseCompose save(Usuario usuario);
    Optional<PojoUsuario> findByNombre(String nombre);
    ResponseCompose actualizarPassword(Login nuevaClave);
    PojoUsuario findById(Long id, String authHeader);

}
