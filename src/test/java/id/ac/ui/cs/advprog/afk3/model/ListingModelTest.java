package id.ac.ui.cs.advprog.afk3.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ListingModelTest {
    @Test
    public void testSetId() {
        Listing listing = new Listing();
        String expectedId = "listing123";
        listing.setId(expectedId);
        assertThat(listing.getId()).isEqualTo(expectedId);
    }

    @Test
    public void testSetSellerUsername() {
        Listing listing = new Listing();
        String expectedUsername = "sellerUsername";
        listing.setSellerUsername(expectedUsername);
        assertThat(listing.getSellerUsername()).isEqualTo(expectedUsername);
    }

    @Test
    public void testSetName() {
        Listing listing = new Listing();
        String expectedName = "Gadget";
        listing.setName(expectedName);
        assertThat(listing.getName()).isEqualTo(expectedName);
    }

    @Test
    public void testSetDescription() {
        Listing listing = new Listing();
        String expectedDescription = "Latest model of the gadget with enhanced features.";
        listing.setDescription(expectedDescription);
        assertThat(listing.getDescription()).isEqualTo(expectedDescription);
    }

    @Test
    public void testSetQuantity() {
        Listing listing = new Listing();
        int expectedQuantity = 50;
        listing.setQuantity(expectedQuantity);
        assertThat(listing.getQuantity()).isEqualTo(expectedQuantity);
    }

    @Test
    public void testSetIsFeatured() {
        Listing listing = new Listing();
        listing.setFeatured(true);
        assertThat(listing.isFeatured()).isTrue();
    }
}
