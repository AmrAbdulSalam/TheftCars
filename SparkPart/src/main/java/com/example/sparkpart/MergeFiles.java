package com.example.sparkpart;

import com.example.sparkpart.services.UpdateOriginalDataService;

public class MergeFiles {
    public static void main(String[] args){

        String originalFile = "/home/yosefabdelsalam/Desktop/amr/2015_State_Top10Report_wTotalThefts.csv";
        String updatedFile = "/home/yosefabdelsalam/Desktop/amr/Sheet1.csv";
        String destinationFile = "/home/yosefabdelsalam/Desktop/amr/data/MergeFiles";
        UpdateOriginalDataService file = new UpdateOriginalDataService();

        file.UpdateFiles(originalFile,updatedFile,destinationFile);
    }
}
