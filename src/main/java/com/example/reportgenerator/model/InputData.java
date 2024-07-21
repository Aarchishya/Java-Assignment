package com.example.reportgenerator.model;

import lombok.Data;

@Data
public class InputData {
    private String field1;
    private String field2;
    private String field3; // Designation
    private String field4; // Status
    private Double field5; // Salary
    private String refKey1; // Employee ID
    private String refKey2; // Department ID
}
