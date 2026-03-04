package com.gapsi.suppliers.model.pojo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gapsi.suppliers.model.Usuario;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PojoProveedor {

    private Long id;
    private String idUnico;
    private String nombre;
    private String razonSocial;
    private String direccion;
    private List<Usuario> usuarios;
    private Long usuarioId;

}
