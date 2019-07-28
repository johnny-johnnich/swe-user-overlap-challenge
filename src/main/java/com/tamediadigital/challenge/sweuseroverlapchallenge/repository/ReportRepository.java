package com.tamediadigital.challenge.sweuseroverlapchallenge.repository;

import java.util.List;

import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportDTO;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportType;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.ZipCodeReportDTO;


/**
 * Report repository interface, used to get reports
 * for overlap uses and most frequent zip codes
 */
public interface ReportRepository {

    /**
     * @param overlapReportType - obj that contains type
     * of report (e.g. All, UNSEEN_PSEUDOPOLIS)
     * @return OverlapReportDTO obj that contains total user count,
     * overlap user count and percentage of overlap users
     */
    OverlapReportDTO findOverlapUsers(OverlapReportType overlapReportType);


    /**
     * @param recordNumber - number of records
     * @return List of ZipCodeReportDTO obj that contains zip code number and count
     */
    List<ZipCodeReportDTO> findMostFrequentZipCodes(Integer recordNumber);
}
