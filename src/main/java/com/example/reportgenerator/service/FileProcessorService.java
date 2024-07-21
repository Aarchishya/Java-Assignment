package com.example.reportgenerator.service;

import com.example.reportgenerator.model.InputData;
import com.example.reportgenerator.model.ReferenceData;
import com.example.reportgenerator.util.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileProcessorService {

    @Autowired
    private CsvUtil csvUtil;

    @Value("${input.file.path}")
    private String inputFilePath;

    @Value("${reference.file.path}")
    private String referenceFilePath;

    public List<InputData> processInputFile() throws FileNotFoundException {
        return csvUtil.readInputCsv(inputFilePath);
    }

    public Map<String, ReferenceData> processReferenceFile() throws FileNotFoundException {
        List<ReferenceData> referenceDataList = csvUtil.readReferenceCsv(referenceFilePath);
        return referenceDataList.stream()
                .collect(Collectors.toMap(
                        ref -> ref.getRefKey1() + "|" + ref.getRefKey2(),
                        ref -> ref
                ));
    }
}
