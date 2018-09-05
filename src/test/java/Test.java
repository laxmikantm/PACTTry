import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Test {

    @Rule
    public PactProviderRuleMk2 mockProvider = new
        PactProviderRuleMk2("test_provider", "localhost", 8080, this);

    @Pact(consumer = "test_consumer")
    public RequestResponsePact createPactContract(PactDslWithProvider builder){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json");

        return builder
                .given("test GET")
                    .uponReceiving("GET REQUEST")
                    .path("/pact")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(headers)
                    .body("{\"condition\": true, \"name\": \"tom\"}")
//                .given("test POST")
//                    .uponReceiving("POST REQUEST")
//                    .method("POST")
//                    .headers(headers)
//                    .body("{\"name\": \"Michael\"}")
//                    .path("/pact")
//                    .willRespondWith()
//                    .status(201)

//
                .toPact();

    }


    @org.junit.Test
    @PactVerification()
    public void givenGet_whenSendRequest_shouldReturn200WithProperHeaderAndBody() throws IOException {
// when
        ResponseEntity<String> response = new RestTemplate()
                .getForEntity(mockProvider.getUrl() + "/pact", String.class);
        System.out.println("OUTPUT ==== "+response.getStatusCode().value());
        System.out.println("\n BODY --- "+response.getBody().toString());
        System.out.println("\n HEADERS --- "+response.getHeaders().get("Content-Type").toString());

//then
        assertEquals(response.getStatusCode().value(),200);

//        assertThat(response.getHeaders().get("Content-Type").contains("application/json")).isTrue();
//        assertThat(response.getBody()).contains("condition", "true", "name", "tom");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String jsonBody = "{\"name\": \"Michael\"}";

// when
        ResponseEntity<String> postResponse = new RestTemplate()
                .exchange(
                        mockProvider.getUrl() + "/create",
                        HttpMethod.POST,
                        new HttpEntity<>(jsonBody, httpHeaders),
                        String.class
                );
    }




}
