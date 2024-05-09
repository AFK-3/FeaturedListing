package id.ac.ui.cs.advprog.afk3.repository;

import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeaturedRepository extends JpaRepository<FeaturedListing, String> {
}
