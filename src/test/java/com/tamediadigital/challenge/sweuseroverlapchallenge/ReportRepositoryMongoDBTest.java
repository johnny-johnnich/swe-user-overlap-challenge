package com.tamediadigital.challenge.sweuseroverlapchallenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.tamediadigital.challenge.sweuseroverlapchallenge.dto.OverlapReportType;
import com.tamediadigital.challenge.sweuseroverlapchallenge.entity.Address;
import com.tamediadigital.challenge.sweuseroverlapchallenge.entity.MendedDrum;
import com.tamediadigital.challenge.sweuseroverlapchallenge.entity.Pseudopolis;
import com.tamediadigital.challenge.sweuseroverlapchallenge.entity.Unseen;
import com.tamediadigital.challenge.sweuseroverlapchallenge.repository.mongodb.ReportRepositoryMongoDBImpl;

@RunWith(SpringRunner.class)
public class ReportRepositoryMongoDBTest {

    @MockBean
    private ApplicationContext context;

    @MockBean
    private MongoTemplate mongoTemplate;

    private ReportRepositoryMongoDBImpl repositoryMongoDB;

    @Before
    public void init() {
        repositoryMongoDB = new ReportRepositoryMongoDBImpl(context, mongoTemplate);
    }

    @Test
    public void findOverlapUsers() throws Exception {
        Mockito.when(mongoTemplate.count(Mockito.any(Query.class), Mockito.anyString())).thenReturn(1L);
        Mockito.when(mongoTemplate.aggregate(Mockito.any(Aggregation.class), Mockito.anyString(),
                Mockito.any())).thenReturn(new AggregationResults<>(new ArrayList<>(), new Document()));

        assert repositoryMongoDB.findOverlapUsers(OverlapReportType.ALL).getTotalUserCount().equals(3L);
        assert repositoryMongoDB.findOverlapUsers(OverlapReportType.PSEUDOPOLIS_MENDEDDRUM).getTotalUserCount().equals(2L);
        assert repositoryMongoDB.findOverlapUsers(OverlapReportType.UNSEEN_MENDEDDRUM).getTotalUserCount().equals(2L);
        assert repositoryMongoDB.findOverlapUsers(OverlapReportType.UNSEEN_PSEUDOPOLIS).getTotalUserCount().equals(2L);

    }

    @Test
    public void findMostFrequentZipCodes() throws Exception {
        List<MendedDrum> mendedZipCodes = Arrays.asList(new MendedDrum(11111));
        List<Pseudopolis> pseudopolisZipCodes = Arrays.asList(new Pseudopolis(new Address(22222)));
        List<Unseen> unseenZipCodes = Arrays.asList(new Unseen(new Address(33333)));
        Mockito.doNothing().when(mongoTemplate).dropCollection(Mockito.anyString());
        Mockito.when(mongoTemplate.createCollection(Mockito.anyString())).thenReturn(null);
        Mockito.when(mongoTemplate.insert(Mockito.any())).thenReturn(null);
        Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(MendedDrum.class), Mockito.anyString()))
                .thenReturn(mendedZipCodes);
        Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(Pseudopolis.class), Mockito.anyString()))
                .thenReturn(pseudopolisZipCodes);
        Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(Unseen.class), Mockito.anyString()))
                .thenReturn(unseenZipCodes);
        Mockito.when(mongoTemplate.aggregate(Mockito.any(Aggregation.class), Mockito.anyString(),
                Mockito.any())).thenReturn(new AggregationResults<>(new ArrayList<>(), new Document()));

        assert repositoryMongoDB.findMostFrequentZipCodes(10).size() == 0;

    }

}
