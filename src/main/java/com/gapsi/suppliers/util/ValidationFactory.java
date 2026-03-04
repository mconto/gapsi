package com.gapsi.suppliers.util;


import com.gapsi.suppliers.model.Proveedor;
import com.gapsi.suppliers.model.Usuario;
import com.gapsi.suppliers.model.login.Login;

import java.util.Objects;

public class ValidationFactory {


    public static Boolean validateUsuarios(Usuario usuario) {
        if (validField(usuario.getNombre()) &&
                validField(usuario.getPassword())) {
            return true;
        }
        return false;
    }

    public static Boolean validateLogin(Login login) {
        if (validField(login.getNombre()) &&
                validField(login.getPwd())) {
            return true;
        }
        return false;
    }

    public static Boolean validateProveedor(Proveedor proveedor) {
        if (validField(proveedor.getNombre()) &&
                validField(proveedor.getRazonSocial()) &&
                validField(proveedor.getDireccion())) {
            return true;
        }
        return false;
    }

    private static Boolean validField(Object field) {
        return Objects.nonNull(field) && !field.toString().isEmpty();
    }
}
