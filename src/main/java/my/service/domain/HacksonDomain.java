package my.service.domain;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import my.service.util.DynamoDBUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HacksonDomain {
    private static AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_1).build();

    private static DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);

    public static HacksonPO loadHacksonDeatilByHacksonId(String hacksonId) {

        return mapper.load(HacksonPO.class,hacksonId,"Details");
    }
    public static void addHackson(HacksonPO hacksonPO) {
        hacksonPO.setMetaData("Details");
        hacksonPO.setHacksonState("create");
        mapper.save(hacksonPO);
    }

    public static void updateHackson(HacksonPO hacksonPO) {
        mapper.save(hacksonPO);
        System.out.println("Item updated:");
    }

    public static HacksonPO deleteHackson(String id) {
        HacksonPO itemRetrieved = mapper.load(HacksonPO.class, id,"Details");
        System.out.println("Item retrieved:");
        System.out.println(itemRetrieved);
        mapper.delete(itemRetrieved);
        return itemRetrieved;
    }


    public static String findHacksonByName(String hacksoName)  {
        System.out.println("findHacksonByName: query hacksoName.");
        return DynamoDBUtil.queryHacksonInfoByHackSonName(hacksoName);

    }


    public static String queryHacksonIdByHackSonName(String hacksoName) {
        System.out.println("queryHacksonIdByHackSonName: query hacksoName.");
        return DynamoDBUtil.queryHacksonIdByHackSonName(hacksoName);

    }



//    public static List<HacksonPO> findHacksonByName(String hacksoName) throws Exception {
//        System.out.println("findHacksonByName: query hacksoName.");
//
//        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
//        eav.put(":val1", new AttributeValue().withS(hacksoName));
//
//        DynamoDBQueryExpression<HacksonPO> queryExpression = new DynamoDBQueryExpression<HacksonPO>()
//                .withKeyConditionExpression("hacksonName = :val1")
////                .withKeyConditionExpression("time between :val2 and :val3")
//                .withExpressionAttributeValues(eav);
//
//        List<HacksonPO> hacksonPOList = mapper.query(HacksonPO.class, queryExpression);
//
//        for (HacksonPO hacksonPO : hacksonPOList) {
//            System.out.format("Id=%s, HacsksonName=%s, startTime=%s %n", hacksonPO.getId(),
//                    hacksonPO.getHacksonName(), hacksonPO.getHacksonStartTime());
//        }
//
//        return hacksonPOList;
//
//    }





}
