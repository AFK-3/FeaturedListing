package id.ac.ui.cs.advprog.afk3.controller;

import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import id.ac.ui.cs.advprog.afk3.service.FeaturedListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/featured-listing")
public class FeaturedListingController {
    @Autowired
    private FeaturedListingService featuredListingService;

    @PostMapping("/createListing")
    public FeaturedListing createListing(@RequestBody FeaturedListing listing) {
        return featuredListingService.create(listing);
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

    @DeleteMapping("/delete/{listingId}")
    public ResponseEntity<String> deleteFeatured(Model model, @PathVariable("listingId") String listingId) {
        try {
            featuredListingService.deleteFeatured(listingId);
            return ResponseEntity.ok("Listing berhasil dihapus dari daftar featured");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Listing not found");
        }
    }

    @PutMapping("/edit")
    public FeaturedListing editFeatured(@RequestBody FeaturedListing listing) {
        return featuredListingService.editFeatured(listing);
    }
}
