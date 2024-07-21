package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputData;
import com.example.reportgenerator.model.ReferenceData;
import com.example.reportgenerator.util.CsvUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileProcessorServiceTest {

    @InjectMocks
    private FileProcessorService fileProcessorService;

    @Mock
    private CsvUtil csvUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(fileProcessorService, "inputFilePath", "/path/to/input.csv");
        ReflectionTestUtils.setField(fileProcessorService, "referenceFilePath", "/path/to/reference.csv");
    }

    @Test
    void testProcessInputFile() throws FileNotFoundException {
        // Arrange
        List<InputData> expectedInputData = Arrays.asList(
                createInputData("field1", "field2", "field3", "field4", 100.0, "key1", "key2"),
                createInputData("field1", "field2", "field3", "field4", 200.0, "key3", "key4")
        );
        when(csvUtil.readInputCsv("/path/to/input.csv")).thenReturn(expectedInputData);

        // Act
        List<InputData> result = fileProcessorService.processInputFile();

        // Assert
        assertEquals(expectedInputData, result);
        verify(csvUtil).readInputCsv("/path/to/input.csv");
    }

    // Helper method to create InputData objects
    private InputData createInputData(String field1, String field2, String field3, String field4, Double field5, String refKey1, String refKey2) {
        InputData inputData = new InputData();
        inputData.setField1(field1);
        inputData.setField2(field2);
        inputData.setField3(field3);
        inputData.setField4(field4);
        inputData.setField5(field5);
        inputData.setRefKey1(refKey1);
        inputData.setRefKey2(refKey2);
        return inputData;
    }

    @Test
    void testProcessInputFileThrowsFileNotFoundException() throws FileNotFoundException {
        // Arrange
        when(csvUtil.readInputCsv("/path/to/input.csv")).thenThrow(new FileNotFoundException());

        // Act & Assert
        assertThrows(FileNotFoundException.class, () -> fileProcessorService.processInputFile());
    }

    @Test
    void testProcessReferenceFile() throws FileNotFoundException {
        // Arrange
        List<ReferenceData> referenceDataList = Arrays.asList(
                createReferenceData("key1", "dept1", "deptKey1", "desc1", "level1", 1000.0),
                createReferenceData("key2", "dept2", "deptKey2", "desc2", "level2", 2000.0)
        );
        when(csvUtil.readReferenceCsv("/path/to/reference.csv")).thenReturn(referenceDataList);

        // Act
        Map<String, ReferenceData> result = fileProcessorService.processReferenceFile();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsKey("key1|deptKey1"));
        assertTrue(result.containsKey("key2|deptKey2"));
        assertEquals("desc1", result.get("key1|deptKey1").getRefData2());
        assertEquals("desc2", result.get("key2|deptKey2").getRefData2());
        verify(csvUtil).readReferenceCsv("/path/to/reference.csv");
    }
    @Test
    void testProcessReferenceFileThrowsFileNotFoundException() throws FileNotFoundException {
        // Arrange
        when(csvUtil.readReferenceCsv("/path/to/reference.csv")).thenThrow(new FileNotFoundException());

        // Act & Assert
        assertThrows(FileNotFoundException.class, () -> fileProcessorService.processReferenceFile());
    }

    private ReferenceData createReferenceData(String refKey1, String refData1, String refKey2, String refData2, String refData3, Double refData4) {
        ReferenceData referenceData = new ReferenceData();
        referenceData.setRefKey1(refKey1);
        referenceData.setRefData1(refData1);
        referenceData.setRefKey2(refKey2);
        referenceData.setRefData2(refData2);
        referenceData.setRefData3(refData3);
        referenceData.setRefData4(refData4);
        return referenceData;
    }
}