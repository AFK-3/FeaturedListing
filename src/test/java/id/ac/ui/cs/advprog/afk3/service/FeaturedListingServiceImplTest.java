package id.ac.ui.cs.advprog.afk3.service;

import id.ac.ui.cs.advprog.afk3.model.Builder.FeaturedListingBuilder;
import id.ac.ui.cs.advprog.afk3.model.FeaturedListing;
import id.ac.ui.cs.advprog.afk3.repository.FeaturedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FeaturedListingServiceImplTest {
    @Mock
    private FeaturedListingBuilder featuredListingBuilder;

    @Mock
    private FeaturedRepository featuredRepository;

    @InjectMocks
    private FeaturedListingServiceImpl featuredListingService;

    List<FeaturedListing> featuredListingList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        featuredListingList = new ArrayList<>();
        FeaturedListing featuredListing1 = new FeaturedListing(
                "1", "user1", "product1", LocalDate.now().plusDays(7));
        FeaturedListing featuredListing2 = new FeaturedListing(
                "2", "user2", "product2", LocalDate.now().plusDays(7));
        featuredListingList.add(featuredListing1);
        featuredListingList.add(featuredListing2);
    }

    @Test
    public void testCreate() {
        FeaturedListing inputListing = new FeaturedListing(
                "1", "user1", "product1", LocalDate.now().plusDays(7));

        when(featuredListingBuilder.reset()).thenReturn(featuredListingBuilder);
        when(featuredListingBuilder.setCurrent(inputListing)).thenReturn(featuredListingBuilder);
        when(featuredListingBuilder.build()).thenReturn(featuredListingList.getFirst());

        FeaturedListing result = featuredListingService.create(inputListing);

        verify(featuredListingBuilder).reset();
        verify(featuredListingBuilder).setCurrent(inputListing);
        verify(featuredListingBuilder).build();
        verify(featuredRepository).save(featuredListingList.getFirst());

        assertNotNull(result);
        assertEquals("product1", result.getName());
        assertEquals(LocalDate.now().plusDays(7), result.getFeaturedExpiryTime());
    }

    @Test
    public void testGetFeaturedByIdFound() {
        String id = "1";
        when(featuredRepository.findById(id)).thenReturn(Optional.of(featuredListingList.getFirst()));

        FeaturedListing result = featuredListingService.getFeaturedById(id);

        assertNotNull(result);
        assertEquals(featuredListingList.getFirst(), result);
    }

    @Test
    public void testGetFeaturedByIdNotFound() {
        String id = "3";
        when(featuredRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            featuredListingService.getFeaturedById(id);
        });
    }

    @Test
    public void testFindAll() {
        when(featuredRepository.findFeaturedListingsByFeaturedExpiryTimeAfter(any(LocalDate.class)))
                .thenReturn(featuredListingList);

        List<FeaturedListing> resultListings = featuredListingService.findAll();

        assertNotNull(resultListings);
        assertFalse(resultListings.isEmpty());
        assertEquals(2, resultListings.size());
        assertEquals(featuredListingList, resultListings);
    }

    @Test
    public void testEditFeatured() {
        FeaturedListing inputListing = new FeaturedListing(
                "1", "user1", "product1", LocalDate.now().plusDays(10));

        when(featuredListingBuilder.reset()).thenReturn(featuredListingBuilder);
        when(featuredListingBuilder.setCurrent(inputListing)).thenReturn(featuredListingBuilder);
        when(featuredListingBuilder.build()).thenReturn(featuredListingList.getFirst());
        when(featuredRepository.save(any(FeaturedListing.class))).thenReturn(featuredListingList.getFirst());

        FeaturedListing result = featuredListingService.editFeatured(inputListing);

        verify(featuredListingBuilder).reset();
        verify(featuredListingBuilder).setCurrent(inputListing);
        verify(featuredListingBuilder).build();
        verify(featuredRepository).save(featuredListingList.getFirst());

        assertNotNull(result);
        assertEquals("product1", result.getName());
        assertEquals(LocalDate.now().plusDays(7), result.getFeaturedExpiryTime());
    }

    @Test
    public void deleteFeaturedFound() {
        String id = "1";
        when(featuredRepository.findById(id)).thenReturn(Optional.of(featuredListingList.getFirst()));

        featuredListingService.deleteFeatured(id);

        verify(featuredRepository).findById(id);
        verify(featuredRepository).delete(featuredListingList.getFirst());
    }

    @Test
    public void deleteFeaturedNotFound() {
        String id = "3";
        when(featuredRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            featuredListingService.deleteFeatured(id);
        });

        verify(featuredRepository).findById(id);
        verify(featuredRepository, never()).delete(any(FeaturedListing.class));
    }

    @Test
    public void deleteAllFeaturedListings() {
        featuredListingService.deleteAll();

        verify(featuredRepository).deleteAll();
    }
}
