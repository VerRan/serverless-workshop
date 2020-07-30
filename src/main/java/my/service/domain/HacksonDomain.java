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
            .withRegion(Regions.AP_NORTHEAST_1).build();

    private static DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);

    public static HacksonPO loadHacksonDeatilByHacksonId(String hacksonId) {
        System.out.println("loadHacksonDeatilByHacksonId: hacksonId is "+hacksonId);
        return mapper.load(HacksonPO.class,hacksonId,"Details");
    }


    public static void addHackson(HacksonPO hacksonPO) {
        mapper.save(hacksonPO);
    }

    public static void updateHackson(HacksonPO hacksonPO) {
        try {
            mapper.save(hacksonPO);
            System.out.println("Item updated:"+hacksonPO.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static HacksonPO deleteHackson(String id,String metaData) {
        HacksonPO itemRetrieved = mapper.load(HacksonPO.class, id,metaData);
        System.out.println("Item retrieved:");
        System.out.println(itemRetrieved);
        mapper.delete(itemRetrieved);
        return itemRetrieved;
    }


    public static String findHacksonByName(String hacksoName)  {
        System.out.println("findHacksonByName: query hacksoName:"+hacksoName);
        return DynamoDBUtil.queryHacksonInfoByHackSonName(hacksoName);

    }


    public static String queryHacksonIdByHackSonName(String hacksoName) {
        System.out.println("queryHacksonIdByHackSonName: query hacksoName: "+hacksoName);
        return DynamoDBUtil.queryHacksonIdByHackSonName(hacksoName);

    }

    public static List<HacksonPO> findAllHackathons() {
        List<HacksonPO> scanResult = null  ;
        try {
            System.out.println("findAllwithStatusIsCreate: Scan hackson.");

            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":val1", new AttributeValue().withS("Details"));
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                    .withFilterExpression("metaData =  :val1 ").withExpressionAttributeValues(eav);
            scanResult = mapper.scan(HacksonPO.class, scanExpression);
        }catch(Exception e){
            e.printStackTrace();

        }
        return scanResult;
    }

    public static HacksonPO findUserByUserid(String userId) {
        return mapper.load(HacksonPO.class,userId,"userId-"+userId);
    }

    public static List<HacksonPO> querAttendUsersByHaksonId(String id) {
        List<HacksonPO> attendUsers = null;
        try {
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":val1", new AttributeValue().withS(id));
//            eav.put(":val2", new AttributeValue().withS("Details"));
            DynamoDBQueryExpression<HacksonPO> queryExpression = new DynamoDBQueryExpression<HacksonPO>()
                    .withKeyConditionExpression("Id = :val1")
//                    .withConditionalOperator("metaData NOT_CONTAINS :val2")
//                    .withFilterExpression("metaData <> :val2")//only for no key attribure
                    .withExpressionAttributeValues(eav);

             attendUsers = mapper.query(HacksonPO.class, queryExpression);
             System.out.println("attendUsers.size() = "+attendUsers.size());
        }catch (Exception e){
            e.printStackTrace();
        }
        return attendUsers;
    }

    public static boolean isAtened(String id, String userId) {
        List<HacksonPO> attendUsers = null;
        try {
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":val1", new AttributeValue().withS(id));
            eav.put(":val2", new AttributeValue().withS("userId-"+userId));
            DynamoDBQueryExpression<HacksonPO> queryExpression = new DynamoDBQueryExpression<HacksonPO>()
                    .withKeyConditionExpression("Id = :val1 and metaData = :val2")
                    .withExpressionAttributeValues(eav);

            attendUsers = mapper.query(HacksonPO.class, queryExpression);
            System.out.println("attendUsers.size() = "+attendUsers.size());
            if(attendUsers.size()>0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public static HacksonPO queryProjectInfo(String id, String userId) {
        List<HacksonPO> attendUsers = null;
        try {
            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
            eav.put(":val1", new AttributeValue().withS(id));
            eav.put(":val2", new AttributeValue().withS("userId-"+userId));
            DynamoDBQueryExpression<HacksonPO> queryExpression = new DynamoDBQueryExpression<HacksonPO>()
                    .withKeyConditionExpression("Id = :val1 and metaData = :val2")
                    .withExpressionAttributeValues(eav);

            attendUsers = mapper.query(HacksonPO.class, queryExpression);
            System.out.println("attendUsers.size() = "+attendUsers.size());
            if(attendUsers.size()>0){
                return attendUsers.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null ;
    }

    public static HacksonPO loadUserInfoByUserId(String userId) {
        System.out.println("loadUserInfoByUserId: userId is "+userId);
        return mapper.load(HacksonPO.class,userId,"userId-"+userId);
    }

    public static String queryUserInfoByUserName(String userName) {
        return DynamoDBUtil.queryUserIdByUserName(userName);
    }

    public static String queryUserIdByUserName(String name) {
        return DynamoDBUtil.queryUserIdByUserName(name);
    }


//    public static String findUserByName(String userName) {
//        List<HacksonPO> attendUsers = null;
//        try {
//            HacksonPO hacksonPO =new HacksonPO();
//            hacksonPO.setUserName(userName);
//            Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
//            eav.put(":v_userName", new AttributeValue().withS(userName));
//            DynamoDBQueryExpression<HacksonPO> queryExpression = new DynamoDBQueryExpression<HacksonPO>()
//                    .withIndexName("userName-index")
//                    .withHashKeyValues(hacksonPO)
//                    .withKeyConditionExpression("userName = :v_userName")
//                    .withExpressionAttributeValues(eav);
//
//            attendUsers = mapper.query(HacksonPO.class, queryExpression);
//            System.out.println("attendUsers.size() = "+attendUsers.size());
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return "";
//    }


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
