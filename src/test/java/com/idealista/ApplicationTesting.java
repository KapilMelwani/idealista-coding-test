package com.idealista;

import static org.assertj.core.api.Assertions.assertThat;

import com.idealista.infrastructure.api.AdsController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTesting {

    @Autowired
    AdsController adsController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(adsController).isNotNull();
    }
}