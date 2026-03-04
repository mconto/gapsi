package com.gapsi.suppliers.model.login;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Login {
    private String nombre;
    private String pwd;
}
