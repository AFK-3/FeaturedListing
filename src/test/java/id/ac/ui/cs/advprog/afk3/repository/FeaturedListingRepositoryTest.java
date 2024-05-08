package id.ac.ui.cs.advprog.afk3.repository;

import id.ac.ui.cs.advprog.afk3.model.Listing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FeaturedListingRepositoryTest {
    @InjectMocks
    FeaturedListingRepository featuredListingRepository;

    Listing listing = new Listing();
    @BeforeEach
    void setUp() {
        listing.setSellerUsername("Rafi");
        listing.setName("Listing1");
        listing.setDescription("Des");
        listing.setQuantity(2);
    }

    @Test
    void testMarkFeatured() {
        featuredListingRepository.addFeaturedListing(listing);
        Iterator<Listing> listingIterator = featuredListingRepository.findAll();
        assertTrue(listingIterator.hasNext());
        Listing featuredListing = listingIterator.next();
        assertEquals(listing.getSellerUsername(), featuredListing.getSellerUsername());
        assertEquals(listing.getName(), featuredListing.getName());
        assertEquals(listing.getDescription(), featuredListing.getDescription());
        assertEquals(listing.getQuantity(), featuredListing.getQuantity());
    }

}
