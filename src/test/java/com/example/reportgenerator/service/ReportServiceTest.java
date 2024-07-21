package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputData;
import com.example.reportgenerator.model.OutputData;
import com.example.reportgenerator.model.ReferenceData;
import com.example.reportgenerator.util.CsvUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private FileProcessorService fileProcessorService;

    @Mock
    private CsvUtil csvUtil;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateReport() {
        List<InputData> inputDataList = List.of(new InputData());
        Map<String, ReferenceData> referenceDataMap = Map.of("EMP001|DEP01", new ReferenceData());

        try {
            when(fileProcessorService.processInputFile()).thenReturn(inputDataList);
            when(fileProcessorService.processReferenceFile()).thenReturn(referenceDataMap);

            doNothing().when(csvUtil).writeOutputToCsv(anyList(), anyString());

            reportService.generateReport();

            verify(fileProcessorService, times(1)).processInputFile();
            verify(fileProcessorService, times(1)).processReferenceFile();
            verify(csvUtil, times(1)).writeOutputToCsv(anyList(), anyString());
        } catch (FileNotFoundException e) {
            fail("File not found: " + e.getMessage());
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }

    @Test
    void testTransformData() {
        InputData inputData = new InputData();
        inputData.setField1("John");
        inputData.setField2("Doe");
        inputData.setField3("Engineer");
        inputData.setField4("Active");
        inputData.setField5(75000.5);
        inputData.setRefKey1("EMP001");
        inputData.setRefKey2("DEP01");

        ReferenceData referenceData = new ReferenceData();
        referenceData.setRefKey1("EMP001");
        referenceData.setRefData1("IT");
        referenceData.setRefKey2("DEP01");
        referenceData.setRefData2("Technology");
        referenceData.setRefData3("Level 3");
        referenceData.setRefData4(50000.0);

        Map<String, ReferenceData> referenceDataMap = Map.of("EMP001|DEP01", referenceData);

        OutputData result = reportService.transformData(inputData, referenceDataMap);

        assertNotNull(result);
        assertEquals("JohnDoe", result.getOutField1());
        assertEquals("IT", result.getOutField2());
        assertEquals("TechnologyLevel 3", result.getOutField3());
        assertEquals(75000.5 * Math.max(75000.5, 50000.0), result.getOutField4());
        assertEquals(75000.5, result.getOutField5());
    }
}
