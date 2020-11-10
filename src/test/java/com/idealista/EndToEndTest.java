package com.idealista;

import com.idealista.infrastructure.api.ApiResponse;
import com.idealista.infrastructure.api.PublicAd;
import com.idealista.infrastructure.api.QualityAd;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.PortableServer.POA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTest {

    @Autowired
    private TestRestTemplate template;

    @LocalServerPort
    private int port;

    @Test
    public void getPublicAdsSuccess() {
        ResponseEntity<ApiResponse> response = template.exchange("http://localhost:" + port + "/api/ads/public", HttpMethod.GET, null, new ParameterizedTypeReference<ApiResponse>() {});
        assertThat(response.getBody().getMessage().equals("No se han calculado los score de los anuncios."));
    }

    @Test
    public void getQualityAdsSuccess() {
        ResponseEntity<ApiResponse> response = template.withBasicAuth("qualityAdmin","1234").exchange("http://localhost:" + port + "/api/ads/quality", HttpMethod.GET, null, new ParameterizedTypeReference<ApiResponse>() {});
        assertThat(response.getBody().getMessage().equals("No se han calculado los score de los anuncios."));
    }

    @Test
    public void calculateScoresSuccess() {
        ResponseEntity<ApiResponse> response = template.withBasicAuth("qualityAdmin","1234").exchange("http://localhost:" + port + "/api/ads/calculateScores", HttpMethod.PUT, null, new ParameterizedTypeReference<ApiResponse>() {});
        List<Map<String,Object>> qualityAd = ((List<Map<String,Object>>) response.getBody().getResponse());
        qualityAd.stream().forEach(
            qAd -> {
                qAd.entrySet().stream().forEach(
                    entry -> {
                        if(entry.getKey().equals("score")) {
                            assertThat(entry.getValue()!=null);
                        }
                    }
                );
            }
        );
    }
}