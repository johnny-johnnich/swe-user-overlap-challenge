package com.tamediadigital.challenge.sweuseroverlapchallenge.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportDTO;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportType;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.ZipCodeReportDTO;
import com.tamediadigital.challenge.sweuseroverlapchallenge.repository.ReportRepository;
import com.tamediadigital.challenge.sweuseroverlapchallenge.repository.mongodb.ReportRepositoryMongoDBImpl;

/**
 * Service layer for Reporting
 * Dummy implementation since there is no business logic
 * Only forwards call to repository layer
 */
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepositoryMongoDBImpl reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * @param overlapReportType - obj that contains type
     * of report (e.g. All, UNSEEN_PSEUDOPOLIS)
     * @return OverlapReportDTO obj that contains total user count,
     * overlap user count and percentage of overlap users
     */
    public OverlapReportDTO findOverlapUsers(OverlapReportType overlapReportType) {
        return reportRepository.findOverlapUsers(overlapReportType);
    }

    /**
     * @param recordNumber - number of records
     * @return List of ZipCodeReportDTO obj that contains zip code number and count
     */
    public List<ZipCodeReportDTO> findMostFrequentZipCodes(Integer recordNumber) {
        return reportRepository.findMostFrequentZipCodes(recordNumber);
    }
}
