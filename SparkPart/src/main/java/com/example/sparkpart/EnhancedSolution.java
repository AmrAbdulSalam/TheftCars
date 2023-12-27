package com.example.sparkpart;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;
import java.util.ArrayList;
import java.util.List;

public class EnhancedSolution {
    public static void main(String[] args)  {
        SparkSession spark = SparkSession.builder()
                .appName("ParallelWriting")
                .master("local[*]")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        JavaRDD<String> carsData = sc.textFile("/home/yosefabdelsalam/Desktop/amr/data/model_countryOfOrigin.csv");

        JavaPairRDD<String ,String> carPairs = carsData
                .filter(line -> !line.startsWith("CarModel"))
                .mapToPair(line -> {
                    String[] arr = line.split(",");
                    String country = arr[1];
                    return new Tuple2<>(country, line);
                });

        JavaPairRDD<String, Iterable<String>> groupedCars = carPairs.groupByKey().cache();

        List<Tuple2<String, Iterable<String>>> groupedCarsList = groupedCars.collect();

        StructType schema = new StructType()
                .add("Model", DataTypes.StringType)
                .add("Country", DataTypes.StringType);

        for (Tuple2<String, Iterable<String>> countryData : groupedCarsList) {
            String country = countryData._1();
            Iterable<String> cars = countryData._2();

            List<String> carsList = new ArrayList<>();
            cars.forEach(carsList::add);

            JavaRDD<Row> rowRDD = sc.parallelize(carsList,1)
                    .map(line -> {
                        String[] parts = line.split(",");
                        return RowFactory.create(parts[0], parts[1]);
                    });

            Dataset<Row> convertToDataset = spark.createDataFrame(rowRDD, schema).cache();

            convertToDataset.write()
                    .option("header", true)
                    .csv("/home/yosefabdelsalam/Desktop/amr/data/" + country + "_cars");
        }

        sc.stop();
        spark.stop();
    }
}
