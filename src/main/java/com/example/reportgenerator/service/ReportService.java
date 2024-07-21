package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputData;
import com.example.reportgenerator.model.OutputData;
import com.example.reportgenerator.model.ReferenceData;
import com.example.reportgenerator.util.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private FileProcessorService fileProcessorService;

    @Autowired
    private CsvUtil csvUtil;

    @Value("${output.file.path}")
    private String outputFilePath;

    @Scheduled(cron = "${report.generation.cron}")
    public void generateReport() {
        try {
            List<InputData> inputDataList = fileProcessorService.processInputFile();
            Map<String, ReferenceData> referenceDataMap = fileProcessorService.processReferenceFile();

            List<OutputData> outputDataList = inputDataList.stream()
                    .map(inputData -> transformData(inputData, referenceDataMap))
                    .collect(Collectors.toList());

            csvUtil.writeOutputToCsv(outputDataList, outputFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("Error generating report: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error generating report: " + e.getMessage());
        }
    }

    public OutputData transformData(InputData input, Map<String, ReferenceData> referenceDataMap) {
        ReferenceData refData = referenceDataMap.get(input.getRefKey1() + "|" + input.getRefKey2());

        if (refData == null) {
            throw new IllegalArgumentException("Reference data not found for keys: " + input.getRefKey1() + ", " + input.getRefKey2());
        }

        double field5Value = input.getField5() != null ? input.getField5() : 0.0;
        double refData4Value = refData.getRefData4() != null ? refData.getRefData4() : 0.0;

        // field3 is non-numeric and not used in calculations
        double outfield4 = field5Value * Math.max(field5Value, refData4Value);

        return new OutputData(
                input.getField1() + input.getField2(),
                refData.getRefData1(),
                refData.getRefData2() + refData.getRefData3(),
                outfield4,
                Math.max(field5Value, refData4Value)
        );
    }
}
