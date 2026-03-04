package com.gapsi.suppliers.repository;

import com.gapsi.suppliers.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepo extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findAll();

    @Query(value = "SELECT id, " +
            "id_unico, " +
            "nombre, " +
            "razon_social, " +
            "direccion, " +
            "FROM " +
            "dbo.proveedor " +
            "WHERE nombre = :nombre", nativeQuery = true)
    Proveedor findByNombre(@Param("nombre") String nombre);

}
