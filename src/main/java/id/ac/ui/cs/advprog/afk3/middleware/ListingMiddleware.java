package id.ac.ui.cs.advprog.afk3.middleware;

import id.ac.ui.cs.advprog.afk3.model.Listing;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class ListingMiddleware {
    public static final String sellUrl = "http://35.198.243.155/";
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

    public static List<Listing> getAllListings(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        try {
            URI uri = UriComponentsBuilder.fromUriString(sellUrl + "/listing/get-all")
                    .build()
                    .toUri();

            ResponseEntity<List<Listing>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Listing>>() {}
            );
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Request failed: " + e.getMessage());
            return null;
        }
    }
}
