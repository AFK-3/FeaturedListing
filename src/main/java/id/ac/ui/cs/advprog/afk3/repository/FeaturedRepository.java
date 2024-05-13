package id.ac.ui.cs.advprog.afk3.repository;

import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FeaturedRepository extends JpaRepository<FeaturedListing, String> {
    List<FeaturedListing> findFeaturedListingsByFeaturedExpiryTimeAfter(LocalDate featuredExpiryTime);
}
