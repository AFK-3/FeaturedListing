package id.ac.ui.cs.advprog.afk3.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;

class FeaturedListingModelTest {
    @Test
    void testNoArgsConstructor() {
        FeaturedListing featuredListing = new FeaturedListing();

        assertThat(featuredListing).isNotNull();
        assertThat(featuredListing.getId()).isNull();
        assertThat(featuredListing.getSellerUsername()).isNull();
        assertThat(featuredListing.getName()).isNull();
        assertThat(featuredListing.getFeaturedExpiryTime()).isEqualTo(LocalDate.now().plusDays(7));
    }

    @Test
    void testAllArgsConstructor() {
        String id = "1";
        String sellerUsername = "user123";
        String name = "Featured Product";
        LocalDate expiryTime = LocalDate.now().plusDays(10);

        FeaturedListing featuredListing = new FeaturedListing(id, sellerUsername, name, expiryTime);

        assertThat(featuredListing).isNotNull();
        assertThat(featuredListing.getId()).isEqualTo(id);
        assertThat(featuredListing.getSellerUsername()).isEqualTo(sellerUsername);
        assertThat(featuredListing.getName()).isEqualTo(name);
        assertThat(featuredListing.getFeaturedExpiryTime()).isEqualTo(expiryTime);
    }

    @Test
    void testSetId() {
        FeaturedListing featuredListing = new FeaturedListing();
        String expectedId = "12345";
        featuredListing.setId(expectedId);
        assertThat(featuredListing.getId()).isEqualTo(expectedId);
    }

    @Test
    void testSetSellerUsername() {
        FeaturedListing featuredListing = new FeaturedListing();
        String expectedUsername = "seller123";
        featuredListing.setSellerUsername(expectedUsername);
        assertThat(featuredListing.getSellerUsername()).isEqualTo(expectedUsername);
    }

    @Test
    void testSetName() {
        FeaturedListing featuredListing = new FeaturedListing();
        String expectedName = "Test Product";
        featuredListing.setName(expectedName);
        assertThat(featuredListing.getName()).isEqualTo(expectedName);
    }

    @Test
    void testSetFeaturedExpiryTime() {
        FeaturedListing featuredListing = new FeaturedListing();
        LocalDate expectedExpiryTime = LocalDate.now().plusDays(15);
        featuredListing.setFeaturedExpiryTime(expectedExpiryTime);
        assertThat(featuredListing.getFeaturedExpiryTime()).isEqualTo(expectedExpiryTime);
    }
}
