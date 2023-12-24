package com.amr.app.TheftCars.controller;

import com.amr.app.TheftCars.models.Car;
import com.amr.app.TheftCars.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final int MAX_SIZE = 5;

    @Autowired
    private CarService carService;

    @GetMapping("/{carBrand}")
    public ResponseEntity<List<Car> > getCarsByCountryOfOrigin(
            @PathVariable String  carBrand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size)
    {
        if(size > MAX_SIZE){
            size = MAX_SIZE;
        }

        Pageable pageable = PageRequest.of(page,size);
        List<Car> cars = carService.getCarsByCountryOfOrigin(carBrand,pageable);
        return ResponseEntity.ok(cars);
    }
}
