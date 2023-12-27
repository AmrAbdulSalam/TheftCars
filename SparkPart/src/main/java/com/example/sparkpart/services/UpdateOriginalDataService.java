package com.example.sparkpart.services;

import org.apache.spark.sql.*;
import org.apache.spark.sql.SparkSession;

public class UpdateOriginalDataService {
    public boolean UpdateFiles(String originalPath , String updatedPath,String destinationFile){
       try{
           SparkSession spark = SparkSession.builder()
                   .appName("UpdateOriginalDataSet")
                   .master("local")
                   .getOrCreate();

           Dataset<Row> originalFile = spark.read()
                   .option("header", "true")
                   .option("ignoreTrailingWhiteSpace", "true")
                   .option("ignoreLeadingWhiteSpace", "true")
                   .csv(originalPath);

           Dataset<Row> updatedFile = spark.read()
                   .option("header", "true")
                   .option("ignoreLeadingWhiteSpace", "true")
                   .option("ignoreTrailingWhiteSpace", "true")
                   .csv(updatedPath);
           updatedFile.show(20,30);

           Dataset<Row> mergedFiles = originalFile.union(updatedFile);

           Dataset<Row> uniqueRows = mergedFiles.dropDuplicates();

           uniqueRows.coalesce(1)
                   .write()
                   .mode("overwrite")
                   .option("header", "true")
                   .csv(destinationFile);

           spark.stop();

           return true;
       }
       catch (Error e){
           return false;
       }
    }
}
