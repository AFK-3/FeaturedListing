package id.ac.ui.cs.advprog.afk3.middleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.afk3.model.Listing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class ListingMiddlewareTest {
    @InjectMocks
    private ListingMiddleware listingMiddleware;

    @Mock
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;
    private final String token = String.format("Bearer %s", fetchToken("rafi2", "rafizia1"));

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        objectMapper = new ObjectMapper();
    }

    public static String fetchToken(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://35.198.243.155/api/auth/login?username=" + username + "&password=" + password;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String token = response.getBody();
            return token;
        } catch (Exception e) {
            return "SALAH";
        }
    }

    @Test
    public void testGetListingSuccess() throws JsonProcessingException {
        String id = "92a1ccda-fd9e-46ec-89a8-b9ef619b23d0";
        String expectedUrl = "http://34.126.165.220/listing/get-by-id/" + id;
        Listing expectedListing = new Listing();
        expectedListing.setId(id);
        expectedListing.setName("produk10");

        String expectedListingJson = objectMapper.writeValueAsString(expectedListing);

        mockServer.expect(requestTo(expectedUrl))
                .andRespond(withStatus(HttpStatus.OK).body(expectedListingJson));

        Listing listing = listingMiddleware.getListing(id, token);

        assertEquals(expectedListing.getId(), listing.getId());
        assertEquals(expectedListing.getName(), listing.getName());
    }

    @Test
    public void testGetListingError() {
        String id = "invalid_id";
        String expectedUrl = "http://34.126.165.220/listing/get-by-id/" + id;

        mockServer.expect(requestTo(expectedUrl))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        Listing listing = listingMiddleware.getListing(id, token);

        assertNull(listing);
    }
}
