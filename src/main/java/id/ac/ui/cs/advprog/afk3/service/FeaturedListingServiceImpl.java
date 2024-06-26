package id.ac.ui.cs.advprog.afk3.service;

import id.ac.ui.cs.advprog.afk3.model.builder.FeaturedListingBuilder;
import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import id.ac.ui.cs.advprog.afk3.repository.FeaturedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class FeaturedListingServiceImpl implements FeaturedListingService {
    @Autowired
    private FeaturedListingBuilder featuredListingBuilder;

    @Autowired
    FeaturedRepository featuredRepository;

    @Override
    public FeaturedListing create(FeaturedListing listing){
        listing = featuredListingBuilder.reset().setCurrent(listing).build();
        featuredRepository.save(listing);
        return listing;
    }

    @Override
    public FeaturedListing getFeaturedById(String id) {
        return featuredRepository.findById(id).
                orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<FeaturedListing> findAll(){
        return featuredRepository.
                findFeaturedListingsByFeaturedExpiryTimeAfter(LocalDate.now());
    }

    @Override
    public FeaturedListing editFeatured(FeaturedListing featuredListing) {
        featuredListing = featuredListingBuilder.reset().setCurrent(featuredListing).build();
        featuredRepository.save(featuredListing);
        return featuredListing;
    }

    @Override
    public void deleteFeatured(String id) {
        FeaturedListing featuredListing = featuredRepository.findById(id).
                orElseThrow(NoSuchElementException::new);
        featuredRepository.delete(featuredListing);
    }

    @Override
    @Async("asyncTaskExecutor")
    public void deleteAll() {
        featuredRepository.deleteAll();
    }
}
