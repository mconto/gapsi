package com.gapsi.suppliers.service;

import com.gapsi.suppliers.model.Proveedor;
import com.gapsi.suppliers.model.ResponseCompose;
import com.gapsi.suppliers.model.Usuario;
import com.gapsi.suppliers.model.pojo.PojoProveedor;
import com.gapsi.suppliers.model.pojo.PojoUsuario;
import com.gapsi.suppliers.repository.ProveedorRepo;
import com.gapsi.suppliers.repository.UsuariosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProveedorService implements IProveedorService {

    @Autowired
    ProveedorRepo proveedorRepo;

    @Autowired
    UsuariosRepo usuariosRepo;


    @Override
    public Page<PojoProveedor> findAll(int idRol, Pageable pageable) {
        Page<Proveedor> proveedorPage = null;
        List<Proveedor> proveedor = proveedorRepo.findAll();

        if (idRol == 0) {
            proveedorPage = proveedorRepo.findAll(PageRequest.of(0, proveedor.size()));
            return proveedorPage.map(this::convertirAPojoProveedor);
        }

        if (idRol == 1) {
            proveedorPage = proveedorRepo.findAll(pageable);
            return proveedorPage.map(this::convertirAPojoProveedor);
        }
        return null;
    }

    @Override
    public ResponseCompose save(Proveedor proveedor) {
        ResponseCompose responseCompose = new ResponseCompose();
        responseCompose.setStatus(true);

        if (validarExistencia(proveedor.getNombre())) {
            responseCompose.setStatus(false);
            responseCompose.setMsg("El proveedor ya existe");
            return responseCompose;
        }

        Proveedor proveedorSaved = proveedorRepo.save(proveedor);
        PojoProveedor pojoProveedor = PojoProveedor.builder()
                .nombre(proveedorSaved.getNombre())
                .id(proveedorSaved.getId())
                .direccion(proveedorSaved.getDireccion())
                .razonSocial(proveedorSaved.getRazonSocial())
                .build();
        responseCompose.setObjeto(pojoProveedor);
        responseCompose.setStatus(true);
        return responseCompose;
    }


    @Override
    public ResponseCompose updateProveedor(Proveedor proveedor) {

        ResponseCompose responseCompose = new ResponseCompose();
        responseCompose.setStatus(true);
        Proveedor proveedorToUpdate = proveedorRepo.findById(proveedor.getId()).map(
                proveedorNuevo -> {
                    proveedorNuevo.setNombre(proveedor.getNombre());
                    proveedorNuevo.setDireccion(proveedor.getDireccion());
                    proveedorNuevo.setRazonSocial(proveedor.getRazonSocial());
                    return proveedorRepo.save(proveedorNuevo);
                }
        ).get();
        responseCompose.setObjeto(proveedorToUpdate);
        return responseCompose;
    }

    @Override
    public PojoProveedor findByNombre(String nombre) {
        Proveedor proveedor = proveedorRepo.findByNombre(nombre);
        return PojoProveedor.builder()
                .nombre(proveedor.getNombre())
                .direccion(proveedor.getDireccion())
                .razonSocial(proveedor.getRazonSocial())
                .build();

    }


    private boolean validarExistencia(String nombre) {
        Proveedor proveedorDb = proveedorRepo.findByNombre(nombre);
        if (Objects.nonNull(proveedorDb)) {
            return true;
        }
        return false;
    }

    private PojoProveedor convertirAPojoProveedor(Proveedor proveedor) {
        PojoProveedor pojo = new PojoProveedor();
        pojo.setId(proveedor.getId());
        pojo.setNombre(proveedor.getNombre());
        pojo.setRazonSocial(proveedor.getRazonSocial());
        pojo.setDireccion(proveedor.getDireccion());
        return pojo;
    }

}
