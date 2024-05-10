package id.ac.ui.cs.advprog.afk3.controller;

import id.ac.ui.cs.advprog.afk3.middleware.AuthMiddleware;
import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
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
public class FeaturedListingController {
    @Autowired
    private FeaturedListingService featuredListingService;

    @PostMapping("/createListing")
    public ResponseEntity<?> createListing(@RequestHeader("Authorization") String token, @RequestBody FeaturedListing listing) {
        String userRole = AuthMiddleware.getRoleFromToken(token);
        String username = AuthMiddleware.getRoleFromToken(token);

        if (username == null || userRole == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        if (!userRole.equals("STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Role must be Staff");
        }

        return ResponseEntity.ok(featuredListingService.create(listing));
    }

    @GetMapping("/{listingId}")
    public ResponseEntity<?> getFeaturedById(Model model, @PathVariable("listingId") String listingId) {
        try {
            return ResponseEntity.ok(featuredListingService.getFeaturedById(listingId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Listing not found");
        }
    }

    @GetMapping("/get-all")
    public List<FeaturedListing> getAllFeatured() {
        return featuredListingService.findAll();
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editFeatured(@RequestHeader("Authorization") String token, @RequestBody FeaturedListing listing) {
        String userRole = AuthMiddleware.getRoleFromToken(token);
        String username = AuthMiddleware.getRoleFromToken(token);

        if (username == null || userRole == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        if (!userRole.equals("STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Role must be Staff");
        }

        return ResponseEntity.ok(featuredListingService.editFeatured(listing));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFeatured(Model model, @RequestHeader("Authorization") String token, @RequestParam("listingId") String listingId) {
        String userRole = AuthMiddleware.getRoleFromToken(token);
        String username = AuthMiddleware.getRoleFromToken(token);

        if (username == null || userRole == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        if (!userRole.equals("STAFF")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Role must be Staff");
        }

        try {
            featuredListingService.deleteFeatured(listingId);
            return ResponseEntity.ok("Listing successfully removed from featured");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Listing not found");
        }
    }
}
