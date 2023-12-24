USE theftcars;

LOAD DATA INFILE "D:/Harri/cars.csv"
INTO TABLE car
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\r\n'
IGNORE 1 ROWS(car_brand,country_of_origin); 


SELECT * FROM car





