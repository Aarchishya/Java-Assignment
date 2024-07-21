package com.example.reportgenerator.controller;
import com.example.reportgenerator.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    void testGenerateReport() throws Exception {
        doNothing().when(reportService).generateReport();

        mockMvc.perform(post("/api/reports/generate"))
                .andExpect(status().isOk())
                .andExpect(content().string("Report generation triggered"));

        verify(reportService).generateReport();
    }

    @Test
    void testTestEndpoint() throws Exception {
        mockMvc.perform(get("/api/reports/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Test endpoint working"));
    }
}
