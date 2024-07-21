package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputData;
import com.example.reportgenerator.model.ReferenceData;
import com.example.reportgenerator.util.CsvUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileProcessorServiceTest {

    @Mock
    private CsvUtil csvUtil;

    @InjectMocks
    private FileProcessorService fileProcessorService;

    FileProcessorServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessInputFile() throws FileNotFoundException {
        List<InputData> inputDataList = List.of(new InputData());
        when(csvUtil.readInputCsv(anyString())).thenReturn(inputDataList);

        List<InputData> result = fileProcessorService.processInputFile();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testProcessReferenceFile() throws FileNotFoundException {
        List<ReferenceData> referenceDataList = List.of(new ReferenceData());
        when(csvUtil.readReferenceCsv(anyString())).thenReturn(referenceDataList);

        Map<String, ReferenceData> result = fileProcessorService.processReferenceFile();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
