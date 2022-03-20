package com.parking.menagement.service;

import com.parking.menagement.model.ParkingSpotModel;
import com.parking.menagement.repository.ParkingSpotRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {

    private ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
          return this.parkingSpotRepository.save(parkingSpotModel);
    }

    public boolean existsByLicensePlateCar(String plateCar) {
        return this.parkingSpotRepository.existsByLicensePlateCar(plateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return this.parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return this.parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    public Page<ParkingSpotModel> findAll(Pageable pageable) {
        return this.parkingSpotRepository.findAll(pageable);
    }

    public Optional<ParkingSpotModel> findById(Long id) {
        return this.parkingSpotRepository.findById(id);
    }

    @Transactional
    public void delete(ParkingSpotModel parkingSpotModel) {
        this.parkingSpotRepository.delete(parkingSpotModel);
    }
}

