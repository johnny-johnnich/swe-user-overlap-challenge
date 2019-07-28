package com.tamediadigital.challenge.sweuseroverlapchallenge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.tamediadigital.challenge.sweuseroverlapchallenge.contoller.ReportController;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportType;
import com.tamediadigital.challenge.sweuseroverlapchallenge.service.ReportService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReportController.class)
public class ReportControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService service;

    @Test
    public void shouldReturnOKStatusOverlapUsers() throws Exception {
        Mockito.when(service.findOverlapUsers(Mockito.any(OverlapReportType.class))).thenReturn(null);
        this.mockMvc.perform(get("/report/overlapUsers/ALL")).andExpect(status().isOk());
    }


    @Test
    public void shouldReturnNotOKStatusOverlapUsers() throws Exception {
        Mockito.when(service.findOverlapUsers(Mockito.any(OverlapReportType.class))).thenReturn(null);
        this.mockMvc.perform(get("/report/overlapUsers/test")).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnOKStatusMostFrequentCode() throws Exception {
        Mockito.when(service.findMostFrequentZipCodes(Mockito.anyInt())).thenReturn(null);
        this.mockMvc.perform(get("/report/mostFrequentZipCodes?recordNumber=1")).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotOKStatusMostFrequentCode() throws Exception {
        Mockito.when(service.findMostFrequentZipCodes(Mockito.anyInt())).thenReturn(null);
        this.mockMvc.perform(get("/report/mostFrequentZipCodes")).andExpect(status().is4xxClientError());
    }

}