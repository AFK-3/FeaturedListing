package id.ac.ui.cs.advprog.afk3.middleware;

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

class AuthMiddlewareTest {
    @InjectMocks
    private AuthMiddleware authMiddleware;

    @Mock
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private final String token = String.format("Bearer %s", fetchToken("rafi2", "rafizia1"));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authMiddleware = new AuthMiddleware();
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    static String fetchToken(String username, String password) {
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
    void testGetUsernameFromTokenSuccess() {
        String expectedUsername = "rafi2";

        mockServer.expect(requestTo("http://35.198.243.155/user/get-username"))
                .andRespond(withStatus(HttpStatus.OK).body(expectedUsername));

        String username = authMiddleware.getUsernameFromToken(token);

        assertEquals(expectedUsername, username);
    }

    @Test
    void testGetUsernameFromTokenFailure() {
        String invalid_token = "Bearer invalid-token";

        mockServer.expect(requestTo("http://35.198.243.155/user/get-username"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        String username = authMiddleware.getUsernameFromToken(invalid_token);

        assertNull(username);
    }

    @Test
    void testGetRoleFromTokenSuccess() {
        String expectedRole = "STAFF";

        mockServer.expect(requestTo("http://35.198.243.155/user/get-username"))
                .andRespond(withStatus(HttpStatus.OK).body(expectedRole));

        String role = authMiddleware.getRoleFromToken(token);

        assertEquals(expectedRole, role);
    }

    @Test
    void testGetRoleFromTokenFailure() {
        String invalid_token = "Bearer invalid-token";

        mockServer.expect(requestTo("http://35.198.243.155/user/get-username"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        String role = authMiddleware.getRoleFromToken(invalid_token);

        assertNull(role);
    }
}
