package com.nosto.currencyconvertor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Profile("test")
class MainApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void main() {
        MainApplication.main(new String[]{});
    }

}
