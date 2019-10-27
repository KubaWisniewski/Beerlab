package com.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@SpringBootTest
@Suite.SuiteClasses({
        BeerRestControllerIntegrationTest.class,
        UserRestControllerIntegrationTest.class,
        GroupRestControllerIntegrationTest.class,
        OrderRestControllerIntegrationTest.class
})
public class BeerlabApplicationTests {

    @Test
    public void contextLoads() {
    }
}
