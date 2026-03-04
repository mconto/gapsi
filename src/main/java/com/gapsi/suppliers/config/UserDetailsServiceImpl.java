package com.gapsi.suppliers.config;

import com.gapsi.suppliers.model.pojo.PojoUsuario;
import com.gapsi.suppliers.repository.UsuariosRepo;
import com.gapsi.suppliers.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
    public class UserDetailsServiceImpl implements UserDetailsService {

        private final UsuariosRepo usuariosRepo;

        @Autowired
        UsuarioService usuarioService;

        public UserDetailsServiceImpl(UsuariosRepo usuariosRepo) {
            this.usuariosRepo = usuariosRepo;
        }


        @Override
        public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
            PojoUsuario pojoUsuario = usuarioService.findByNombre(nombre)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            return new CustomUserDetails(
                    pojoUsuario.getId(),
                    pojoUsuario.getNombre(),
                    pojoUsuario.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(pojoUsuario.getRolApp()))
            );
        }
    }