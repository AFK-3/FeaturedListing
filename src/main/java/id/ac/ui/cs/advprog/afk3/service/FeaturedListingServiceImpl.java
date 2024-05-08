package id.ac.ui.cs.advprog.afk3.service;

import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import id.ac.ui.cs.advprog.afk3.repository.FeaturedListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@Service
public class FeaturedListingServiceImpl implements FeaturedListingService {
    @Autowired
    FeaturedListingRepository featuredListingRepository;

    @Override
    public FeaturedListing create(FeaturedListing listing){
        if(!checkIfListingPresent(listing.getId())){
            featuredListingRepository.createFeaturedListing(listing);
            return listing;
        }
        return null;
    }

    @Override
    public List<FeaturedListing> findAll(){
        Iterator<FeaturedListing> listingIterator=featuredListingRepository.findAll();
        List<FeaturedListing> allListing = new ArrayList<>();
        listingIterator.forEachRemaining(allListing::add);
        return allListing;
    }

    @Override
    public boolean checkIfListingPresent(String id) {
        return featuredListingRepository.checkIfListingPresent(id);
    }

    @Override
    public void deleteFeatured(String id) {
        featuredListingRepository.delete(id);
    }

    @Override
    public void editFeatured(String id, Calendar featuredExpiryTime) {
        featuredListingRepository.editFeatured(id, featuredExpiryTime);
    }
}
