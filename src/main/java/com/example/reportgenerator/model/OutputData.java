package com.example.reportgenerator.model;

import lombok.Data;

@Data
public class OutputData {
    private String outField1;
    private String outField2;
    private String outField3;
    private Double outField4;
    private Double outField5;

    public OutputData(String outField1, String outField2, String outField3, Double outField4, Double outField5) {
        this.outField1 = outField1;
        this.outField2 = outField2;
        this.outField3 = outField3;
        this.outField4 = outField4;
        this.outField5 = outField5;
    }
}
