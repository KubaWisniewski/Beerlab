package com.app;

import com.app.model.Beer;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.model.User;
import com.app.repository.BeerRepository;
import com.app.repository.RoleRepository;
import com.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        BeerlabApplication.class,
        Jsr310JpaConverters.class
})
@EnableJdbcHttpSession
public class BeerlabApplication {
    private RoleRepository roleRepository;
    private BeerRepository beerRepository;
    private UserRepository userRepository;

    public BeerlabApplication(RoleRepository roleRepository, BeerRepository beerRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.beerRepository = beerRepository;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BeerlabApplication.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public void run(String... args) throws Exception {
        if (roleRepository.count() != RoleName.values().length) {
            roleRepository.deleteAll();
            Arrays
                    .stream(RoleName.values())
                    .forEach(role -> roleRepository.save(Role.builder().roleName(role).build()));
        }
        if (beerRepository.count() == 0) {
            Beer beer = Beer.builder().brand("Aaaa").description("ADesc").price(10.0).build();
            Beer beer2 = Beer.builder().brand("Bbbb").description("BDesc").price(10.0).build();
            beerRepository.saveAll(Arrays.asList(beer, beer2));
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userRepository.save(User.builder().email("test@test.com").username("test").roles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER).get())).password(bCryptPasswordEncoder.encode("123")).build());
        userRepository.save(User.builder().email("admin@test.com").username("admin").roles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_ADMIN).get())).password(bCryptPasswordEncoder.encode("123")).build());

    }
}
