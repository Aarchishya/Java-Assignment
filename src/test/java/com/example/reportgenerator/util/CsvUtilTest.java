package com.example.reportgenerator.util;

import com.example.reportgenerator.model.InputData;
import com.example.reportgenerator.model.ReferenceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilTest {

    private CsvUtil csvUtil;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        csvUtil = new CsvUtil();
    }

    @Test
    void testReadInputCsv() throws IOException {
        // Arrange
        File inputFile = tempDir.resolve("input.csv").toFile();
        writeInputCsvFile(inputFile);

        // Act
        List<InputData> result = csvUtil.readInputCsv(inputFile.getAbsolutePath());

        // Assert
        assertEquals(2, result.size());
        assertEquals("field1_1", result.get(0).getField1());
        assertEquals("field2_2", result.get(1).getField2());
        assertEquals(200.0, result.get(1).getField5());
    }

    @Test
    void testReadInputCsvFileNotFound() {
        // Act & Assert
        assertThrows(FileNotFoundException.class, () ->
                csvUtil.readInputCsv("non_existent_file.csv")
        );
    }

    @Test
    void testReadReferenceCsv() throws IOException {
        // Arrange
        File referenceFile = tempDir.resolve("reference.csv").toFile();
        writeReferenceCsvFile(referenceFile);

        // Act
        List<ReferenceData> result = csvUtil.readReferenceCsv(referenceFile.getAbsolutePath());

        // Assert
        assertEquals(2, result.size());
        assertEquals("key1", result.get(0).getRefKey1());
        assertEquals("dept2", result.get(1).getRefData1());
        assertEquals(2000.0, result.get(1).getRefData4());
    }

    @Test
    void testReadReferenceCsvFileNotFound() {
        // Act & Assert
        assertThrows(FileNotFoundException.class, () ->
                csvUtil.readReferenceCsv("non_existent_file.csv")
        );
    }

    private void writeInputCsvFile(File file) throws IOException {
        List<String> lines = Arrays.asList(
                "field1,field2,field3,field4,field5,refKey1,refKey2",
                "field1_1,field2_1,field3_1,field4_1,100.0,key1,key2",
                "field1_2,field2_2,field3_2,field4_2,200.0,key3,key4"
        );
        java.nio.file.Files.write(file.toPath(), lines);
    }

    private void writeReferenceCsvFile(File file) throws IOException {
        List<String> lines = Arrays.asList(
                "refKey1,refData1,refKey2,refData2,refData3,refData4",
                "key1,dept1,deptKey1,desc1,level1,1000.0",
                "key2,dept2,deptKey2,desc2,level2,2000.0"
        );
        java.nio.file.Files.write(file.toPath(), lines);
    }

}
