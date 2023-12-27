package com.example.sparkpart;

import com.example.sparkpart.services.CountryOfOriginAPIService;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.io.IOException;


public class Queries {
    public static void main(String[] args) throws IOException {

        SparkSession spark = SparkSession.builder()
                .appName("Queries")
                .master("local[*]")
                .getOrCreate();


        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        JavaRDD<String> linesRDD = sc.textFile("/home/yosefabdelsalam/Desktop/amr/2015_State_Top10Report_wTotalThefts.csv");
        String header = linesRDD.first();
        JavaRDD<String> replacedRDD = linesRDD.map(line -> line.replaceAll("\"", ""));

        JavaRDD<Row> rowRDD = replacedRDD.map(line -> {
            String[] parts = line.split(",");
            if (parts.length > 5) {
                return RowFactory.create(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4]+parts[5]
                );
            }
            else{
                return RowFactory.create(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4]
                );
            }
        });

        StructType schema = new StructType()
                .add("State", DataTypes.StringType)
                .add("Rank", DataTypes.StringType)
                .add("Make/Model", DataTypes.StringType)
                .add("Model_Year", DataTypes.StringType)
                .add("Thefts", DataTypes.StringType);

        Dataset<Row> cleanedData = spark.createDataFrame(rowRDD, schema);

        cleanedData.createOrReplaceTempView("cleanedData");

        Dataset<Row> mostTheftedModel =
                spark.sql("SELECT `Make/Model`, SUM(`Thefts`) AS TOTAL_CASES FROM cleanedData GROUP BY `Make/Model` ORDER BY TOTAL_CASES DESC LIMIT 5");
        mostTheftedModel.show();

        Dataset<Row> mostTheftedCarsBasesOnState =
                spark.sql("SELECT `State` , SUM(`Thefts`) AS TOTAL_THEFTED_CARS FROM cleanedData GROUP BY `State` ORDER BY TOTAL_THEFTED_CARS DESC LIMIT 5");
        mostTheftedCarsBasesOnState.show();


        CountryOfOriginAPIService countryOfOriginAPIService = new CountryOfOriginAPIService();

        String[] model = mostTheftedModel.first().getString(0).split(" ");

        String countryWhereAmericansBuyCarsFrom = countryOfOriginAPIService.getCountryOfOrigin(model[0]);

        System.out.println(countryWhereAmericansBuyCarsFrom);

        spark.stop();
    }
}
