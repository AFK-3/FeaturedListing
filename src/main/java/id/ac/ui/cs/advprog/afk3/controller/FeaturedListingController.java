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

@RestController
@RequestMapping("/featured-listing")
public class FeaturedListingController {
    @Autowired
    private FeaturedListingService featuredListingService;

    @PostMapping("/createListing")
    public FeaturedListing createListing(@RequestBody FeaturedListing listing) {
        return featuredListingService.create(listing);
    }

    @GetMapping("/get-all-featured")
    public List<FeaturedListing> getAllFeatured() {
        return featuredListingService.findAll();
    }

    @DeleteMapping("/delete-featured/{listingId}")
    public ResponseEntity<String> deleteFeatured(Model model, @PathVariable("listingId") String listingId) {
        featuredListingService.deleteFeatured(listingId);
        return ResponseEntity.ok("Listing berhasil dihapus dari daftar featured");
    }

    @PutMapping("/edit-featured/{listingId}")
    public ResponseEntity<String> editFeatured(Model model, @PathVariable("listingId") String listingId, @RequestBody String featuredExpiryTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Calendar cal = Calendar.getInstance();
            cal.setTime(format.parse(featuredExpiryTime));
            featuredListingService.editFeatured(listingId, cal);
            return ResponseEntity.ok("Listing berhasil diubah");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }
}
