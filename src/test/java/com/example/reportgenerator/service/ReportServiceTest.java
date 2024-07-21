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
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @Mock
    private FileProcessorService fileProcessorService;

    @Mock
    private CsvUtil csvUtil;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateReport() throws Exception {
        // Setup
        String testOutputPath = "/test/output/path.csv";
        ReflectionTestUtils.setField(reportService, "outputFilePath", testOutputPath);

        InputData inputData = new InputData();
        inputData.setField1("field1");
        inputData.setField2("field2");
        inputData.setField5(5.0);
        inputData.setRefKey1("EMP001");
        inputData.setRefKey2("DEPT001");

        List<InputData> inputDataList = Arrays.asList(inputData);

        ReferenceData refData = new ReferenceData();
        refData.setRefKey1("EMP001");
        refData.setRefData1("IT Department");
        refData.setRefKey2("DEPT001");
        refData.setRefData2("Information Technology");
        refData.setRefData3("Level 3");
        refData.setRefData4(100000.0);

        Map<String, ReferenceData> referenceDataMap = new HashMap<>();
        referenceDataMap.put("EMP001|DEPT001", refData);

        when(fileProcessorService.processInputFile()).thenReturn(inputDataList);
        when(fileProcessorService.processReferenceFile()).thenReturn(referenceDataMap);

        // Execute
        reportService.generateReport();

        // Verify
        verify(fileProcessorService).processInputFile();
        verify(fileProcessorService).processReferenceFile();
        verify(csvUtil).writeOutputToCsv(anyList(), eq(testOutputPath));
    }

    @Test
    public void testTransformData() {
        // Setup
        InputData input = new InputData();
        input.setField1("field1");
        input.setField2("field2");
        input.setField5(5.0);
        input.setRefKey1("EMP001");
        input.setRefKey2("DEPT001");

        ReferenceData refData = new ReferenceData();
        refData.setRefKey1("EMP001");
        refData.setRefData1("IT Department");
        refData.setRefKey2("DEPT001");
        refData.setRefData2("Information Technology");
        refData.setRefData3("Level 3");
        refData.setRefData4(10.0);

        Map<String, ReferenceData> referenceDataMap = new HashMap<>();
        referenceDataMap.put("EMP001|DEPT001", refData);

        // Execute
        OutputData result = reportService.transformData(input, referenceDataMap);

        // Verify
        assertEquals("field1field2", result.getOutField1());
        assertEquals("IT Department", result.getOutField2());
        assertEquals("Information TechnologyLevel 3", result.getOutField3());
        assertEquals(50.0, result.getOutField4());
        assertEquals(10.0, result.getOutField5());
    }

    @Test
    public void testTransformDataWithNullValues() {
        // Setup
        InputData input = new InputData();
        input.setField1("field1");
        input.setField2("field2");
        input.setField5(null);
        input.setRefKey1("EMP001");
        input.setRefKey2("DEPT001");

        ReferenceData refData = new ReferenceData();
        refData.setRefKey1("EMP001");
        refData.setRefData1("IT Department");
        refData.setRefKey2("DEPT001");
        refData.setRefData2("Information Technology");
        refData.setRefData3("Level 3");
        refData.setRefData4(null);

        Map<String, ReferenceData> referenceDataMap = new HashMap<>();
        referenceDataMap.put("EMP001|DEPT001", refData);

        // Execute
        OutputData result = reportService.transformData(input, referenceDataMap);

        // Verify
        assertEquals("field1field2", result.getOutField1());
        assertEquals("IT Department", result.getOutField2());
        assertEquals("Information TechnologyLevel 3", result.getOutField3());
        assertEquals(0.0, result.getOutField4());
        assertEquals(0.0, result.getOutField5());
    }

    @Test
    public void testTransformDataWithMissingReferenceData() {
        // Setup
        InputData input = new InputData();
        input.setField1("field1");
        input.setField2("field2");
        input.setField5(5.0);
        input.setRefKey1("EMP001");
        input.setRefKey2("DEPT001");

        Map<String, ReferenceData> referenceDataMap = new HashMap<>();

        // Execute & Verify
        assertThrows(IllegalArgumentException.class, () -> {
            reportService.transformData(input, referenceDataMap);
        });
    }
}