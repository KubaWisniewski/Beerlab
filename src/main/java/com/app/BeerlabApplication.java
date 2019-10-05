package com.app;

import com.app.model.Beer;
import com.app.model.Role;
import com.app.model.RoleName;
import com.app.repository.BeerRepository;
import com.app.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        BeerlabApplication.class,
        Jsr310JpaConverters.class
})
public class BeerlabApplication implements CommandLineRunner {
    private RoleRepository roleRepository;
    private BeerRepository beerRepository;

    public BeerlabApplication(RoleRepository roleRepository, BeerRepository beerRepository) {
        this.roleRepository = roleRepository;
        this.beerRepository = beerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BeerlabApplication.class, args);
    }

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Override
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
            beerRepository.saveAll(Arrays.asList(beer,beer2));
        }
    }
}
