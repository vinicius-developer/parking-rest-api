package com.parking.menagement.controller;

import com.parking.menagement.dtos.ParkingSpotDto;
import com.parking.menagement.model.ParkingSpotModel;
import com.parking.menagement.service.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingController {

    private final ParkingSpotService parkingSpotService;

    public ParkingController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        if (this.parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: licence car exists");
        }

        if (this.parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: parking spot in use");
        }

        if (this.parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: apartment and block already registred");
        }

        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();

        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);

        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(this.parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ParkingSpotModel>> list(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingSpotService.findAll(pageable));
    }

    @GetMapping("/take/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") Long id) {
        Optional<ParkingSpotModel> parkingSpotModel = this.parkingSpotService.findById(id);

        if (parkingSpotModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This id not exists");
        }

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModel.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id) {
        Optional<ParkingSpotModel> parkingSpotModel = this.parkingSpotService.findById(id);

        if (parkingSpotModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This id not exists");
        }

        this.parkingSpotService.delete(parkingSpotModel.get());

        return ResponseEntity.status(HttpStatus.OK).body("item deleted");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id, @RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        Optional<ParkingSpotModel> parkingSpotModelOption = this.parkingSpotService.findById(id);

        if (parkingSpotModelOption.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This id not exists");
        }

        ParkingSpotModel parkingSpotModel = parkingSpotModelOption.get();

        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);

        this.parkingSpotService.save(parkingSpotModel);

        return ResponseEntity.status(HttpStatus.OK).body("updated");

    }

}
