package com.amr.app.TheftCars.services;

import com.amr.app.TheftCars.models.Car;
import com.amr.app.TheftCars.repository.ICarRepository;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private ICarRepository carRepository;

    public List<Car> getCarsByCountryOfOrigin(String carBrand , Pageable pageable){
        Page<Car> pageCar = carRepository.getCarByCountryOfOrigin(carBrand, pageable);
        return pageCar.getContent();
    }
}
