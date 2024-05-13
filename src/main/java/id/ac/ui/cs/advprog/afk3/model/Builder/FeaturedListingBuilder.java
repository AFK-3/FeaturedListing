package id.ac.ui.cs.advprog.afk3.model.Builder;

import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import id.ac.ui.cs.advprog.afk3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FeaturedListingBuilder {
    private FeaturedListing currentFeaturedListing;

    @Autowired
    private UserRepository userRepository;

    public FeaturedListingBuilder(){
        this.reset();
    }

    public FeaturedListingBuilder reset(){
        currentFeaturedListing = new FeaturedListing();
        return this;
    }

    public FeaturedListingBuilder addId(String id){
        currentFeaturedListing.setId(id);
        return this;
    }

    public FeaturedListingBuilder setCurrent(FeaturedListing listing){
        currentFeaturedListing = listing;
        return this;
    }

    public FeaturedListingBuilder setFeaturedExpiry(LocalDate expiryTime) {
        currentFeaturedListing.setFeaturedExpiryTime(expiryTime);
        return this;
    }

    public FeaturedListing build(){
        return currentFeaturedListing;
    }
}
