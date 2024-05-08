package id.ac.ui.cs.advprog.afk3.service;

import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;

import java.util.Calendar;
import java.util.List;

public interface FeaturedListingService {
    public FeaturedListing create(FeaturedListing listing);
    public List<FeaturedListing> findAll();
    public boolean checkIfListingPresent(String id);
    public void deleteFeatured(String id);
    public void editFeatured(String id, Calendar featuredExpiryTime);
}
