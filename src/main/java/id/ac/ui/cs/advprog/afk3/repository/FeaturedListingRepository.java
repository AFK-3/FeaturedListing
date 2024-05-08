package id.ac.ui.cs.advprog.afk3.repository;

import id.ac.ui.cs.advprog.afk3.model.Builder.FeaturedListingBuilder;
import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@Repository
public class FeaturedListingRepository {
    private final List<FeaturedListing> featuredListingData = new ArrayList<>();

    @Autowired
    private FeaturedListingBuilder featuredListingBuilder;
    public FeaturedListing createFeaturedListing(FeaturedListing listing){
        listing = featuredListingBuilder.reset().setCurrent(listing).build();
        featuredListingData.add(listing);
        return listing;
    }

    public Iterator<FeaturedListing> findAll(){
        return featuredListingData.iterator();
    }

    public FeaturedListing findById(String id){
        for (FeaturedListing FeaturedListing: featuredListingData){
            if (FeaturedListing.getId().equals(id)){
                return FeaturedListing;
            }
        }
        return null;
    }

    public boolean checkIfListingPresent(String id){
        for (FeaturedListing FeaturedListing: featuredListingData){
            if (FeaturedListing.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public void delete(String id) {
        featuredListingData.removeIf(FeaturedListing -> FeaturedListing.getId().equals(id));
    }

    public void editFeatured(String id, Calendar featuredExpiryTime) {
        for (FeaturedListing FeaturedListing: featuredListingData){
            if (FeaturedListing.getId().equals(id)){
                FeaturedListing.setFeaturedExpiryTime(featuredExpiryTime);
            }
        }
    }
}
