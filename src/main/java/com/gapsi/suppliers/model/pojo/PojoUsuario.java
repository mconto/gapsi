package com.gapsi.suppliers.model.pojo;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PojoUsuario {

    private Long id;
    private String nombre;
    private String password;
    private String versionApp;
    private String rolApp;
    private String token;
}
