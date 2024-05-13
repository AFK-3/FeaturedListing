package id.ac.ui.cs.advprog.afk3.middleware;

import id.ac.ui.cs.advprog.afk3.model.Listing;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class ListingMiddleware {
    public static final String sellUrl = "http://34.126.165.220/";
    public static Listing getListing(String id, String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            URI uri = UriComponentsBuilder.fromUriString(sellUrl + "/listing/get-by-id/{id}")
                    .buildAndExpand(id)
                    .toUri();

            ResponseEntity<Listing> response = restTemplate.exchange(uri,
                    HttpMethod.GET,
                    entity,
                    Listing.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Request failed: " + e.getMessage());
            return null;
        }
    }
}
