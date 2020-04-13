package my.service.domain;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.util.UUID;

public class UserDomain {

    private static AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_1).build();

    private static DynamoDBMapper mapper = new DynamoDBMapper(dynamoDBClient);

    public static HacksonPO loadHacksonDeatilByHacksonId(String hacksonId) {

        return mapper.load(HacksonPO.class,hacksonId,"Details");
    }
    public static void addUser(HacksonPO hacksonPO) {
        hacksonPO.setMetaData("userId-"+ UUID.randomUUID());
        mapper.save(hacksonPO);
    }

    public static void updateUser(HacksonPO hacksonPO) {
        mapper.save(hacksonPO);
        System.out.println("Item updated:");
    }

    public static HacksonPO deleteUser(String id) {
        HacksonPO itemRetrieved = mapper.load(HacksonPO.class, id,"Details");
        System.out.println("Item retrieved:");
        System.out.println(itemRetrieved);
        mapper.delete(itemRetrieved);
        return itemRetrieved;
    }

//
//    public String queryHacksonDeatilByUserId(String userId) {
//        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
//        eav.put(":v1", new AttributeValue().withS(userId));
//        eav.put(":v2",new AttributeValue().withS("Details"));
//
//        DynamoDBQueryExpression<HacksonPO> queryExpression = new DynamoDBQueryExpression<HacksonPO>()
//                .withKeyConditionExpression("Id = :v1 and metaData > :v2")
//                .withExpressionAttributeValues(eav);
//
//
//        return HacksonDao.queryHacksonDeatilByHacksonId(userId);
//    }
}
