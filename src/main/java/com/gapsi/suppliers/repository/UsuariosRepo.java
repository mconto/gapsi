package com.gapsi.suppliers.repository;

import com.gapsi.suppliers.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepo extends JpaRepository<Usuario, Long> {

    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Usuario save(Usuario usuario);

    Optional<Usuario> findByNombre(@Param("nombre") String nombre);

    @Transactional
    @Modifying
    @Query(value = "UPDATE usuarios " +
            "       SET password = :password " +
            "       WHERE nombre = :nombre", nativeQuery = true)
    int updatePassword(@Param("password") String password,
                       @Param("correo") String correo);
}
