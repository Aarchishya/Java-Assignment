package com.example.reportgenerator.util;

import com.example.reportgenerator.model.InputData;
import com.example.reportgenerator.model.OutputData;
import com.example.reportgenerator.model.ReferenceData;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class CsvUtil {

    public List<InputData> readInputCsv(String filename) throws FileNotFoundException {
        try (FileReader reader = new FileReader(filename)) {
            CsvToBean<InputData> csvToBean = new CsvToBeanBuilder<InputData>(reader)
                    .withType(InputData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            throw new FileNotFoundException("Error reading input CSV file: " + e.getMessage());
        }
    }

    public List<ReferenceData> readReferenceCsv(String filename) throws FileNotFoundException {
        try (FileReader reader = new FileReader(filename)) {
            CsvToBean<ReferenceData> csvToBean = new CsvToBeanBuilder<ReferenceData>(reader)
                    .withType(ReferenceData.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            throw new FileNotFoundException("Error reading reference CSV file: " + e.getMessage());
        }
    }

    public void writeOutputToCsv(List<OutputData> outputDataList, String filename) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (FileWriter writer = new FileWriter(filename)) {
            StatefulBeanToCsv<OutputData> beanToCsv = new StatefulBeanToCsvBuilder<OutputData>(writer)
                    .build();

            beanToCsv.write(outputDataList);
        }
    }
}
