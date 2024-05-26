package id.ac.ui.cs.advprog.afk3.controller;

import id.ac.ui.cs.advprog.afk3.middleware.AuthMiddleware;
import id.ac.ui.cs.advprog.afk3.middleware.ListingMiddleware;
import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import id.ac.ui.cs.advprog.afk3.model.Listing;
import id.ac.ui.cs.advprog.afk3.service.FeaturedListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/featured-listing")
@CrossOrigin(origins = "*")
public class FeaturedListingController {
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String ROLE_MUST_BE_STAFF = "Role must be Staff";
    public static final String LISTING_NOT_FOUND = "Listing not found";

    @Autowired
    private FeaturedListingService featuredListingService;

    @PostMapping("/createListing")
    public ResponseEntity<?> createListing(@RequestHeader("Authorization") String token, @RequestBody FeaturedListing listing) {
        String userRole = AuthMiddleware.getRoleFromToken(token);
        String username = AuthMiddleware.getRoleFromToken(token);

        if (username == null || userRole == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_TOKEN);
        }
        if (!userRole.equals("STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ROLE_MUST_BE_STAFF);
        }

        return ResponseEntity.ok(featuredListingService.create(listing));
    }

    @GetMapping("/{listingId}")
    public ResponseEntity<?> getFeaturedById(Model model, @PathVariable("listingId") String listingId, @RequestHeader("Authorization") String token) {
        Listing listing = ListingMiddleware.getListing(listingId, token);

        try {
            FeaturedListing featuredListing = featuredListingService.getFeaturedById(listingId);
            featuredListing.setName(listing.getName());
            featuredListing.setSellerUsername(listing.getSellerUsername());

            return ResponseEntity.ok(featuredListing);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LISTING_NOT_FOUND);
        }
    }

    @GetMapping("/get-all")
    public List<FeaturedListing> getAllFeatured(@RequestHeader("Authorization") String token) {
        List<FeaturedListing> featuredListingListBefore = featuredListingService.findAll();
        List<Listing> listingList = ListingMiddleware.getAllListings(token);

        for (FeaturedListing featuredListing : featuredListingListBefore) {
            assert listingList != null;
            for (Listing listing : listingList) {
                if (featuredListing.getId().equals(listing.getId())) {
                    featuredListing.setName(listing.getName());
                    featuredListing.setSellerUsername(listing.getSellerUsername());
                }
            }
        }

        return featuredListingListBefore;
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editFeatured(@RequestHeader("Authorization") String token, @RequestBody FeaturedListing listing) {
        String userRole = AuthMiddleware.getRoleFromToken(token);
        String username = AuthMiddleware.getRoleFromToken(token);

        if (username == null || userRole == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_TOKEN);
        }
        if (!userRole.equals("STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ROLE_MUST_BE_STAFF);
        }

        return ResponseEntity.ok(featuredListingService.editFeatured(listing));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFeatured(Model model, @RequestHeader("Authorization") String token, @RequestParam("listingId") String listingId) {
        String userRole = AuthMiddleware.getRoleFromToken(token);
        String username = AuthMiddleware.getRoleFromToken(token);

        if (username == null || userRole == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_TOKEN);
        }
        if (!userRole.equals("STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ROLE_MUST_BE_STAFF);
        }

        try {
            featuredListingService.deleteFeatured(listingId);
            return ResponseEntity.ok("Listing successfully removed from featured");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LISTING_NOT_FOUND);
        }
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAll(Model model, @RequestHeader("Authorization") String token) {
        String userRole = AuthMiddleware.getRoleFromToken(token);
        String username = AuthMiddleware.getRoleFromToken(token);

        if (username == null || userRole == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_TOKEN);
        }
        if (!userRole.equals("STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ROLE_MUST_BE_STAFF);
        }

        try {
            featuredListingService.deleteAll();
            return ResponseEntity.ok("All listing successfully removed from featured");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LISTING_NOT_FOUND);
        }
    }
}
