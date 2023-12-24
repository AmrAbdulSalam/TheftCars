package com.amr.app.TheftCars.repository;

import com.amr.app.TheftCars.models.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface ICarRepository extends JpaRepository<Car,Long>{
    @Query("SELECT c FROM Car c WHERE c.CarBrand = :carBrand")
    Page<Car> getCarByCountryOfOrigin(@Param("carBrand")String carBrand , Pageable pageable);
}
