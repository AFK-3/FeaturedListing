package id.ac.ui.cs.advprog.afk3.model.Builder;

import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import id.ac.ui.cs.advprog.afk3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class FeaturedListingBuilder {
    private FeaturedListing currentFeaturedListing;
    private Timer featuredTimer;

    @Autowired
    private UserRepository userRepository;

    public FeaturedListingBuilder(){
        this.reset();
    }

    public FeaturedListingBuilder reset(){
        currentFeaturedListing = new FeaturedListing();
        return this;
    }

    public FeaturedListingBuilder addName(String name){
        currentFeaturedListing.setName(name);
        return this;
    }

    public FeaturedListingBuilder addQuantity(int quantity){
        currentFeaturedListing.setQuantity(quantity);
        return this;
    }

    public FeaturedListingBuilder addDescription(String description){
        currentFeaturedListing.setDescription(description);
        return this;
    }

    public FeaturedListingBuilder addId(String id){
        currentFeaturedListing.setId(id);
        return this;
    }

    public FeaturedListingBuilder addSellerUsername(String username){
        currentFeaturedListing.setSellerUsername(username);
        return this;
    }

    public FeaturedListingBuilder setCurrent(FeaturedListing listing){
        currentFeaturedListing = listing;
        return this;
    }

    public FeaturedListingBuilder setFeaturedExpiry(Calendar expiryTime) {
        featuredTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentFeaturedListing.setFeatured(false);
            }
        }, expiryTime.getTime());
        return this;
    }

    public FeaturedListing build(){
        return currentFeaturedListing;
    }
}
