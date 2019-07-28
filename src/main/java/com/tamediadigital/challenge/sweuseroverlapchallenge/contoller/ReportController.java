package com.tamediadigital.challenge.sweuseroverlapchallenge.contoller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportDTO;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportType;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.ZipCodeReportDTO;
import com.tamediadigital.challenge.sweuseroverlapchallenge.service.ReportService;

/**
 * Report controller - mapped to "/report" api
 * Used as access point for getting reports via rest services
 */
@RestController
@RequestMapping("report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * @param reportType - path param contains type
     * of report (e.g. All, UNSEEN_PSEUDOPOLIS)
     * @return OverlapReportDTO obj that contains total user count,
     * overlap user count and percentage of overlap users
     */
    @GetMapping("/overlapUsers/{reportType}")
    OverlapReportDTO overlapUsers(@PathVariable OverlapReportType reportType) {
        return reportService.findOverlapUsers(reportType);
    }

    /**
     * @param recordNumber - request param that contains number of records
     * @return List of ZipCodeReportDTO obj that contains zip code number and count
     */
    @GetMapping("/mostFrequentZipCodes")
    List<ZipCodeReportDTO> mostFrequentZipCodes(@RequestParam Integer recordNumber) {
        return reportService.findMostFrequentZipCodes(recordNumber);
    }

}
