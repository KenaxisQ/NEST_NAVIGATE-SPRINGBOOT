package com.kenaxisq.nestnavigate.amenity.service;

import com.kenaxisq.nestnavigate.amenity.entity.Amenity;
import com.kenaxisq.nestnavigate.amenity.repository.AmenityRepository;
import com.kenaxisq.nestnavigate.custom_exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AmenityServiceImpl implements AmenityService {
    AmenityRepository amenityRepository;

    public AmenityServiceImpl(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    @Override
    public List<Amenity> getAllAmenities() {
        try{
            List<Amenity> amenityList= new ArrayList<>();
            amenityList = (List<Amenity>) amenityRepository.findAll();

            if(amenityList.isEmpty()){
                throw new ApiException("AMENITIES_EMPTY", "No Amenities found", HttpStatus.NOT_FOUND);
            }
            return amenityList;
        } catch (ApiException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApiException("ERR_AMENITIES_FETCH", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Amenity createAmenity(Amenity amenity) {
        try {
            Amenity existingAmenity = amenityRepository.findAmenityByName(amenity.getName());
            if (existingAmenity != null) {
                throw new ApiException("DUPLICATE_EMENITY", "Amenity name already exists: " + amenity.getName(), HttpStatus.CONFLICT);
            }
            amenityRepository.save(amenity);
            return amenityRepository.findAmenityByName(amenity.getName());

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("ERR_AMENITY_CREATION", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public Amenity updateAmenity(Amenity amenity) {
        try {
            Optional<Amenity> existingAmenity = amenityRepository.findById(amenity.getId());

            if ((existingAmenity).isEmpty()) {
                throw new ApiException("AMENITY_NOT_FOUND", "Amenity not found with ID: " + amenity.getId(), HttpStatus.NOT_FOUND);
            }

            Amenity duplicateAmenity = amenityRepository.findAmenityByName(amenity.getName());

            if (duplicateAmenity != null && !duplicateAmenity.getId().equals(amenity.getId())) {
                throw new ApiException("DUPLICATE_EMENITY", "Amenity name already exists: " + amenity.getName(), HttpStatus.CONFLICT);
            }

            amenityRepository.save(amenity);
            return amenityRepository.findAmenityByName(amenity.getName());

        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("ERR_AMENITY_UPDATION", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Amenity getAmenityFromName(String amenityName) {
        try {
            Amenity amenity = amenityRepository.findAmenityByName(amenityName);
            if (amenity == null) {
                throw new ApiException("AMENITY_NOT_FOUND", "Amenity not found with name: " + amenityName, HttpStatus.NOT_FOUND);
            }
            return amenity;
        } catch (Exception e) {
            throw new ApiException("ERR_AMENITY_FETCH", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<Amenity> getAmenityFromCategory(String category) {
        try {
            List<Amenity> amenities = amenityRepository.findAmenityByCategory(category);
            if (amenities.isEmpty()) {
                throw new ApiException("AMENITY_NOT_FOUND", "No amenities found for category: " + category, HttpStatus.NOT_FOUND);
            }
            return amenities;
        } catch (Exception e) {
            throw new ApiException("ERR_AMENITIES_FETCH", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public String deleteAmenityFromId(String amenityId) {
        try {
            if (!amenityRepository.existsById(amenityId)) {
                throw new ApiException("AMENITY_NOT_FOUND", "Amenity not found with ID: " + amenityId, HttpStatus.NOT_FOUND);
            }
            amenityRepository.deleteById(amenityId);
            return "Amenity deleted successfully";
        } catch (Exception e) {
            throw new ApiException("ERR_AMENITY_DELETION", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String deleteAmenityFromName(String amenityName) {
        try {
            Amenity amenity = amenityRepository.findAmenityByName(amenityName);
            if (amenity == null) {
                throw new ApiException("AMENITY_NOT_FOUND", "Amenity not found with name: " + amenityName, HttpStatus.NOT_FOUND);
            }
            amenityRepository.delete(amenity);
            return "Amenity deleted successfully";
        } catch (Exception e) {
            throw new ApiException("ERR_AMENITY_DELETION", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public Amenity getAmenityFromId(String amenityId) {
        try {
            Optional<Amenity> amenity = amenityRepository.findById(amenityId);
            if (amenity.isEmpty()) {
                throw new ApiException("AMENITY_NOT_FOUND", "Amenity not found with ID: " + amenityId, HttpStatus.NOT_FOUND);
            }
            return amenity.get();
        } catch (Exception e) {
            throw new ApiException("ERR_AMENITY_FETCH", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String addAllAmenitiesToDatabase() {
        Arrays.stream(com.kenaxisq.nestnavigate.utils.property.Amenity.values())
                .forEach(enumAmenity -> {
                    Amenity amenityEntity = new Amenity(enumAmenity.getDescription(), enumAmenity.getCategory().name());
                    amenityRepository.save(amenityEntity);
                });
        return "All amenities added successfully";
    }
}
