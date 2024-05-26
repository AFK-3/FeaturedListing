package id.ac.ui.cs.advprog.afk3.controller;

import id.ac.ui.cs.advprog.afk3.middleware.AuthMiddleware;
import id.ac.ui.cs.advprog.afk3.middleware.ListingMiddleware;
import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import id.ac.ui.cs.advprog.afk3.service.FeaturedListingService;
import id.ac.ui.cs.advprog.afk3.model.Listing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@WebMvcTest(FeaturedListingController.class)
@ExtendWith(MockitoExtension.class)
public class FeaturedListingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    FeaturedListingController featuredListingController;

    @MockBean
    FeaturedListingService featuredListingService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(featuredListingController).build();
    }

    @Test
    void testCreateListingSuccess() throws Exception {
        String token = "valid_token_with_staff_role";
        String requestBody = "{\"id\": \"65cb5b49-fa43-4e87-9e56-8c9bfc7\", " +
                "\"featuredExpiryTime\": \"2024-05-20\"}";

        when(featuredListingService.create(any(FeaturedListing.class))).thenReturn(new FeaturedListing());
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("STAFF");

            ResultActions resultActions = mockMvc.perform(post("/featured-listing/createListing")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isOk());
        }
    }

    @Test
    void testCreateListingForbidden() throws Exception {
        String token = "valid_token_with_other_role";
        String requestBody = "{\"id\": \"65cb5b49-fa43-4e87-9e56-8c9bfc7\", " +
                "\"featuredExpiryTime\": \"2024-05-20\"}";

        when(featuredListingService.create(any(FeaturedListing.class))).thenReturn(new FeaturedListing());
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("BUYER");

            ResultActions resultActions = mockMvc.perform(post("/featured-listing/createListing")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isForbidden());
        }
    }

    @Test
    void testCreateListingUnauthorized() throws Exception {
        String token = "invalid_token";
        String requestBody = "{\"id\": \"65cb5b49-fa43-4e87-9e56-8c9bfc7\", " +
                "\"featuredExpiryTime\": \"2024-05-20\"}";

        when(featuredListingService.create(any(FeaturedListing.class))).thenReturn(new FeaturedListing());
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn(null);
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn(null);

            ResultActions resultActions = mockMvc.perform(post("/featured-listing/createListing")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isUnauthorized());
        }
    }

    @Test
    public void testGetFeaturedByIdSuccess() throws Exception {
        String token = "valid_token";
        String listingId = "1";
        Listing listing = new Listing();
        listing.setName("Sample Listing");
        listing.setSellerUsername("seller123");

        FeaturedListing featuredListing = new FeaturedListing();
        featuredListing.setId(listingId);
        featuredListing.setFeaturedExpiryTime(LocalDate.now().plusDays(5));

        when(featuredListingService.getFeaturedById(listingId)).thenReturn(featuredListing);
        try(MockedStatic<ListingMiddleware> mockListingMiddleware = Mockito.mockStatic(ListingMiddleware.class)) {
            mockListingMiddleware.when(() -> ListingMiddleware.getListing(listingId, token)).thenReturn(listing);

            ResultActions resultActions = mockMvc.perform(get("/featured-listing/" + listingId)
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON));

            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Sample Listing"))
                    .andExpect(jsonPath("$.sellerUsername").value("seller123"));
        }
    }

    @Test
    public void testGetFeaturedByIdNotFound() throws Exception {
        String token = "valid_token";
        String listingId = "1";

        when(featuredListingService.getFeaturedById(listingId)).thenThrow(new NoSuchElementException());
        try(MockedStatic<ListingMiddleware> mockListingMiddleware = Mockito.mockStatic(ListingMiddleware.class)) {
            mockListingMiddleware.when(() -> ListingMiddleware.getListing(listingId, token)).thenReturn(null);

            ResultActions resultActions = mockMvc.perform(get("/featured-listing/" + listingId)
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON));

            resultActions.andExpect(status().isNotFound());
        }
    }

    @Test
    public void testGetAllFeaturedSuccess() throws Exception {
        String token = "valid_token";
        FeaturedListing listing1 = new FeaturedListing();
        listing1.setId("1");
        listing1.setFeaturedExpiryTime(LocalDate.now().plusDays(5));

        FeaturedListing listing2 = new FeaturedListing();
        listing2.setId("2");
        listing2.setFeaturedExpiryTime(LocalDate.now().plusDays(10));

        List<FeaturedListing> featuredListings = Arrays.asList(listing1, listing2);

        Listing listing3 = new Listing();
        listing3.setId("1");
        listing3.setSellerUsername("rafi");
        listing3.setName("produk1");

        Listing listing4 = new Listing();
        listing4.setId("2");
        listing4.setSellerUsername("rafi");
        listing4.setName("produk2");

        List<Listing> Listings = Arrays.asList(listing3, listing4);

        when(featuredListingService.findAll()).thenReturn(featuredListings);
        try(MockedStatic<ListingMiddleware> mockListingMiddleware = Mockito.mockStatic(ListingMiddleware.class)) {
            mockListingMiddleware.when(() -> ListingMiddleware.getAllListings(token)).thenReturn(Listings);

            ResultActions resultActions = mockMvc.perform(get("/featured-listing/get-all")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON));

            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[0].featuredExpiryTime[0]").value (LocalDate.now().plusDays(5).getYear()))
                    .andExpect(jsonPath("$[0].featuredExpiryTime[1]").value (LocalDate.now().plusDays(5).getMonthValue()))
                    .andExpect(jsonPath("$[0].featuredExpiryTime[2]").value (LocalDate.now().plusDays(5).getDayOfMonth()))
                    .andExpect(jsonPath("$[0].name").value("produk1"))
                    .andExpect(jsonPath("$[0].sellerUsername").value("rafi"))
                    .andExpect(jsonPath("$[1].id").value("2"))
                    .andExpect(jsonPath("$[1].featuredExpiryTime[0]").value (LocalDate.now().plusDays(10).getYear()))
                    .andExpect(jsonPath("$[1].featuredExpiryTime[1]").value (LocalDate.now().plusDays(10).getMonthValue()))
                    .andExpect(jsonPath("$[1].featuredExpiryTime[2]").value (LocalDate.now().plusDays(10).getDayOfMonth()))
                    .andExpect(jsonPath("$[1].name").value("produk2"))
                    .andExpect(jsonPath("$[1].sellerUsername").value("rafi"));
        }
    }

    @Test
    public void editFeaturedSuccess() throws Exception {
        String token = "valid_token_with_staff_role";
        String requestBody = "{\"id\": \"65cb5b49-fa43-4e87-9e56-8c9bfc7\", " +
                "\"featuredExpiryTime\": \"2030-12-31\"}";

        when(featuredListingService.editFeatured(any(FeaturedListing.class))).thenReturn(new FeaturedListing());
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn(null);
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn(null);

            ResultActions resultActions = mockMvc.perform(put("/featured-listing/edit")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isUnauthorized());
        }
    }

    @Test
    public void editFeaturedForbidden() throws Exception {
        String token = "valid_token_with_other_role";
        String requestBody = "{\"id\": \"65cb5b49-fa43-4e87-9e56-8c9bfc7\", " +
                "\"featuredExpiryTime\": \"2030-12-31\"}";

        when(featuredListingService.editFeatured(any(FeaturedListing.class))).thenReturn(new FeaturedListing());
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("BUYER");

            ResultActions resultActions = mockMvc.perform(put("/featured-listing/edit")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isForbidden());
        }
    }

    @Test
    public void editFeaturedUnauthorized() throws Exception {
        String token = "invalid_token";
        String requestBody = "{\"id\": \"65cb5b49-fa43-4e87-9e56-8c9bfc7\", " +
                "\"featuredExpiryTime\": \"2030-12-31\"}";

        when(featuredListingService.editFeatured(any(FeaturedListing.class))).thenReturn(new FeaturedListing());
        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("STAFF");

            ResultActions resultActions = mockMvc.perform(put("/featured-listing/edit")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody));

            resultActions.andExpect(status().isOk());
        }
    }

    @Test
    public void deleteFeaturedSuccess() throws Exception {
        String token = "valid_token";
        doNothing().when(featuredListingService).deleteFeatured("1");

        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("STAFF");

            mockMvc.perform(delete("/featured-listing/delete")
                            .header("Authorization", token)
                            .param("listingId", "1"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void deleteFeaturedForbidden() throws Exception {
        String token = "valid_token";
        doNothing().when(featuredListingService).deleteFeatured("1");

        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("BUYER");

            mockMvc.perform(delete("/featured-listing/delete")
                            .header("Authorization", token)
                            .param("listingId", "1"))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    public void deleteFeaturedUnauthorized() throws Exception {
        String token = "invalid_token";
        doNothing().when(featuredListingService).deleteFeatured("1");

        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn(null);
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn(null);

            mockMvc.perform(delete("/featured-listing/delete")
                            .header("Authorization", token)
                            .param("listingId", "1"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    public void deleteFeaturedNotFound() throws Exception {
        String token = "valid_token";
        doThrow(new NoSuchElementException()).when(featuredListingService).deleteFeatured("1");

        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("STAFF");

            mockMvc.perform(delete("/featured-listing/delete")
                            .header("Authorization", token)
                            .param("listingId", "1"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    public void deleteAllFeaturedSuccess() throws Exception {
        String token = "valid_token";
        doNothing().when(featuredListingService).deleteAll();

        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("STAFF");

            mockMvc.perform(delete("/featured-listing/delete-all")
                            .header("Authorization", token)
                            .param("listingId", "1"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void deleteAllFeaturedForbidden() throws Exception {
        String token = "valid_token";
        doNothing().when(featuredListingService).deleteAll();

        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("BUYER");

            mockMvc.perform(delete("/featured-listing/delete-all")
                            .header("Authorization", token)
                            .param("listingId", "1"))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    public void deleteAllFeaturedUnauthorized() throws Exception {
        String token = "invalid_token";
        doNothing().when(featuredListingService).deleteAll();

        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn(null);
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn(null);

            mockMvc.perform(delete("/featured-listing/delete-all")
                            .header("Authorization", token)
                            .param("listingId", "1"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    public void deleteAllFeaturedNotFound() throws Exception {
        String token = "valid_token";
        doThrow(new NoSuchElementException()).when(featuredListingService).deleteAll();

        try(MockedStatic<AuthMiddleware> mockAuthMiddleware = Mockito.mockStatic(AuthMiddleware.class)) {
            mockAuthMiddleware.when(() -> AuthMiddleware.getUsernameFromToken(anyString())).thenReturn("username");
            mockAuthMiddleware.when(() -> AuthMiddleware.getRoleFromToken(anyString())).thenReturn("STAFF");

            mockMvc.perform(delete("/featured-listing/delete-all")
                            .header("Authorization", token)
                            .param("listingId", "1"))
                    .andExpect(status().isNotFound());
        }
    }
}
