package com.example.sparkpart;

import com.example.sparkpart.services.CountryOfOriginAPIService;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class SparkPi {
    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession.builder()
                .appName("TheftCars")
                .master("local")
                .getOrCreate();

        Dataset<Row> carsTheftData = spark.read()
                .option("header", true)
                .csv("/home/yosefabdelsalam/Desktop/amr/2015_State_Top10Report_wTotalThefts.csv");
        CountryOfOriginAPIService countryOfOriginAPIService = new CountryOfOriginAPIService();

        Dataset<Row> carsModel = carsTheftData.select("Make/Model");
        Row[] rows = (Row[]) carsModel.collect();

        List<Row> newData = new ArrayList<>();

        for(Row row : rows){
            String[] model = row.getString(0).split(" ");
            String countryOfOrigin = countryOfOriginAPIService.getCountryOfOrigin(model[0]);
            Row newRow = RowFactory.create(row.getString(0), countryOfOrigin);
            newData.add(newRow);
        }

        StructType schema = new StructType(new StructField[]{
                new StructField("CarModel", DataTypes.StringType, false, Metadata.empty()),
                new StructField("CountryOfOrigin", DataTypes.StringType, false, Metadata.empty())
        });

        Dataset<Row> newFile = spark.createDataFrame(newData, schema);

        newFile
                .write()
                .option("header", true)
                .mode("overwrite")
                .csv("/home/yosefabdelsalam/Desktop/amr/data/model_CountryOfOrigin");

        spark.stop();
    }
}
