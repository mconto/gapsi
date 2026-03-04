package com.gapsi.suppliers.service;

import com.gapsi.suppliers.model.Proveedor;
import com.gapsi.suppliers.model.ResponseCompose;
import com.gapsi.suppliers.model.pojo.PojoProveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProveedorService {

    Page<PojoProveedor> findAll(int idRol, Pageable pageable);
    ResponseCompose save(Proveedor proveedor);
    ResponseCompose updateProveedor(Proveedor proveedor);
    PojoProveedor findByNombre(String nombre);


}
