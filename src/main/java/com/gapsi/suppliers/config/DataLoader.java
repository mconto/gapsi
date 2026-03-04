package com.gapsi.suppliers.config;

import com.gapsi.suppliers.model.Proveedor;
import com.gapsi.suppliers.model.Usuario;
import com.gapsi.suppliers.repository.ProveedorRepo;
import com.gapsi.suppliers.repository.UsuariosRepo;
import com.gapsi.suppliers.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.stream.IntStream;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(ProveedorRepo proveedorRepo, UsuarioService usuarioService) {

        Random random = new Random();

        return args -> {

            if (proveedorRepo.count() == 0) {

                IntStream.rangeClosed(1, 30).forEach(i -> {

                    Proveedor proveedor = Proveedor.builder()
                            .nombre("Proveedor " + i)
                            .razonSocial("Empresa " + (char) (random.nextInt(26) + 'A'))
                            .direccion("Calle " + random.nextInt(200))
                            .build();

                    proveedorRepo.save(proveedor);
                });
            }

                Usuario admin = new Usuario();
                admin.setNombre("admin");
                admin.setPassword("admin");
                admin.setRol("0");

                usuarioService.save(admin);

                Usuario user = new Usuario();
                user.setNombre("user");
                user.setPassword("user");
                user.setRol("1");

                usuarioService.save(user);


        };
    }
}