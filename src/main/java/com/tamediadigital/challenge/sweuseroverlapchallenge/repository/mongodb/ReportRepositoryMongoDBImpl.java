package com.tamediadigital.challenge.sweuseroverlapchallenge.repository.mongodb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.tamediadigital.challenge.sweuseroverlapchallenge.config.MongoDBConfig;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportDTO;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportType;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.ZipCodeReportDTO;
import com.tamediadigital.challenge.sweuseroverlapchallenge.entity.MendedDrum;
import com.tamediadigital.challenge.sweuseroverlapchallenge.entity.Pseudopolis;
import com.tamediadigital.challenge.sweuseroverlapchallenge.entity.Unseen;
import com.tamediadigital.challenge.sweuseroverlapchallenge.entity.ZipCode;
import com.tamediadigital.challenge.sweuseroverlapchallenge.repository.ReportRepository;

import lombok.AllArgsConstructor;

/**
 *  MongoDB implementation of Report repository
 */
@Repository
@AllArgsConstructor
public class ReportRepositoryMongoDBImpl implements ReportRepository {

    ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoDBConfig.class);
    MongoTemplate mongoTemplate = (MongoTemplate)ctx.getBean("mongoTemplate");


    @Override
    public OverlapReportDTO findOverlapUsers(OverlapReportType overlapReportType) {


        LookupOperation pseudopolisLookup = LookupOperation.newLookup()
                .from("pseudopolis")
                .localField("email")
                .foreignField("email")
                .as("from_pseudopolis");

        LookupOperation mendedLookup = LookupOperation.newLookup()
                .from("mended_drum")
                .localField("email")
                .foreignField("email")
                .as("from_mended_drum");

        LookupOperation unseenLookup = LookupOperation.newLookup()
                .from("unseen")
                .localField("email")
                .foreignField("email")
                .as("from_unseen");

        Aggregation overlapUsers;
        List<BasicDBObject> dbResults = Collections.emptyList();
        Query emptyQuery = new Query();
        OverlapReportDTO overlapReport = new OverlapReportDTO();

        Long countOfPseudopolis = mongoTemplate.count(emptyQuery, "pseudopolis");
        Long countOfUnseen = mongoTemplate.count(emptyQuery, "unseen");
        Long countOfMended = mongoTemplate.count(emptyQuery, "mended_drum");
        Long totalUserCount = 0L;
        Long overlapUserCount = 0L;
        Float overlapPercentage = 0F;


        switch (overlapReportType) {
            case ALL:
                overlapUsers = Aggregation.newAggregation(
                        mendedLookup,
                        Aggregation.match(Criteria.where("from_mended_drum.email").exists(true)),
                        unseenLookup,
                        Aggregation.match(Criteria.where("from_unseen.email").exists(true)));
                dbResults = mongoTemplate.aggregate(overlapUsers, "pseudopolis",
                        BasicDBObject.class).getMappedResults();


                totalUserCount = countOfPseudopolis + countOfUnseen + countOfMended;
                overlapUserCount = Long.valueOf(dbResults.size());
                overlapPercentage = (overlapUserCount * 100.0f) /totalUserCount;

                overlapReport.setTotalUserCount(totalUserCount);
                overlapReport.setOverlapUserCount(overlapUserCount);
                overlapReport.setOverlapPercentage(BigDecimal.valueOf(overlapPercentage)
                        .setScale(2, RoundingMode.HALF_DOWN));
                break;
            case UNSEEN_PSEUDOPOLIS:
                overlapUsers = Aggregation.newAggregation(pseudopolisLookup,
                        Aggregation.match(Criteria.where("from_pseudopolis.email").exists(true)));
                dbResults = mongoTemplate.aggregate(overlapUsers, "unseen",
                        BasicDBObject.class).getMappedResults();

                countOfUnseen = mongoTemplate.count(emptyQuery, "unseen");
                countOfPseudopolis = mongoTemplate.count(emptyQuery, "pseudopolis");
                totalUserCount = countOfPseudopolis + countOfUnseen;
                overlapUserCount = Long.valueOf(dbResults.size());
                overlapPercentage = (overlapUserCount * 100.0f) /totalUserCount;

                overlapReport.setTotalUserCount(totalUserCount);
                overlapReport.setOverlapUserCount(overlapUserCount);
                overlapReport.setOverlapPercentage(BigDecimal.valueOf(overlapPercentage)
                        .setScale(2, RoundingMode.HALF_DOWN));
                break;
            case UNSEEN_MENDEDDRUM:
                overlapUsers = Aggregation.newAggregation(mendedLookup,
                        Aggregation.match(Criteria.where("from_mended_drum.email").exists(true)));
                dbResults = mongoTemplate.aggregate(overlapUsers, "unseen",
                        BasicDBObject.class).getMappedResults();

                totalUserCount = countOfUnseen + countOfMended;
                overlapUserCount = Long.valueOf(dbResults.size());
                overlapPercentage = (overlapUserCount * 100.0f) /totalUserCount;

                overlapReport.setTotalUserCount(totalUserCount);
                overlapReport.setOverlapUserCount(overlapUserCount);
                overlapReport.setOverlapPercentage(BigDecimal.valueOf(overlapPercentage)
                        .setScale(2, RoundingMode.HALF_DOWN));
                break;
            case PSEUDOPOLIS_MENDEDDRUM:
                overlapUsers = Aggregation.newAggregation(mendedLookup,
                        Aggregation.match(Criteria.where("from_mended_drum.email").exists(true)));
                dbResults = mongoTemplate.aggregate(overlapUsers, "pseudopolis",
                        BasicDBObject.class).getMappedResults();

                totalUserCount = countOfPseudopolis + countOfMended;
                overlapUserCount = Long.valueOf(dbResults.size());
                overlapPercentage = (overlapUserCount * 100.0f) /totalUserCount;

                overlapReport.setTotalUserCount(totalUserCount);
                overlapReport.setOverlapUserCount(overlapUserCount);
                overlapReport.setOverlapPercentage(BigDecimal.valueOf(overlapPercentage)
                        .setScale(2, RoundingMode.HALF_DOWN));
                break;
            default:
                break;
        }
        return overlapReport;

    }

    @Override
    public List<ZipCodeReportDTO> findMostFrequentZipCodes(Integer recordNumber) {

        mongoTemplate.dropCollection("all_zip_codes");
        mongoTemplate.createCollection("all_zip_codes");
        List<ZipCode> zipCodes = new ArrayList<>();

        Query mendedQuery = new Query();
        mendedQuery.fields().include("postcode").exclude("_id");

        List<MendedDrum> mendedZipCodes = mongoTemplate.find(mendedQuery,
                MendedDrum.class, "mended_drum");

        mendedZipCodes.forEach(mended -> {
            zipCodes.add(new ZipCode(mended.getPostcode()));
        });

        Query pseudopolisQuery = new Query();
        pseudopolisQuery.fields().include("address.zip").exclude("_id");

        List<Pseudopolis> pseudopolisZipCodes = mongoTemplate.find(pseudopolisQuery,
                Pseudopolis.class, "pseudopolis");

        pseudopolisZipCodes.forEach(pseudopolis -> {
            zipCodes.add(new ZipCode(pseudopolis.getAddress().getZip()));
        });

        Query unseenQuery = new Query();
        unseenQuery.fields().include("address.zip").exclude("_id");

        List<Unseen> unseenZipCodes = mongoTemplate.find(unseenQuery,
                Unseen.class, "unseen");

        unseenZipCodes.forEach(unseen -> {
            zipCodes.add(new ZipCode(unseen.getAddress().getZip()));
        });

        mongoTemplate.insert(zipCodes, "all_zip_codes");

        Aggregation countZip = Aggregation.newAggregation(
                Aggregation.group("zipCode").count().as("count"),
                Aggregation.project("count").and("zipCode").previousOperation(),
                Aggregation.sort(Sort.Direction.DESC, "count")
        );
        AggregationResults<ZipCodeReportDTO> all_zip_codes = mongoTemplate
                .aggregate(countZip, "all_zip_codes", ZipCodeReportDTO.class);

        List<ZipCodeReportDTO> results = new ArrayList<>();
        results.addAll(all_zip_codes.getMappedResults().stream().limit(recordNumber).collect(Collectors.toList()));

        return results;
    }
}
