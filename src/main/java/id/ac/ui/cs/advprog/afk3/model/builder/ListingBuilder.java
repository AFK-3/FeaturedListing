package id.ac.ui.cs.advprog.afk3.model.builder;

import id.ac.ui.cs.advprog.afk3.model.Listing;
import org.springframework.stereotype.Component;

@Component
public class ListingBuilder {
    private Listing currentListing;

    public ListingBuilder(){
        this.reset();
    }

    public ListingBuilder reset(){
        currentListing = new Listing();
        return this;
    }

    public ListingBuilder addName(String name){
        currentListing.setName(name);
        return this;
    }

    public ListingBuilder addQuantity(int quantity){
        currentListing.setQuantity(quantity);
        return this;
    }

    public ListingBuilder addDescription(String description){
        currentListing.setDescription(description);
        return this;
    }

    public ListingBuilder addId(String id){
        currentListing.setId(id);
        return this;
    }

    public ListingBuilder addSellerUsername(String username){
        currentListing.setSellerUsername(username);
        return this;
    }

    public ListingBuilder setCurrent(Listing listing){
        currentListing = listing;
        return this;
    }

    public Listing build(){
        return currentListing;
    }
}