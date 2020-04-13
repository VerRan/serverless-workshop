package my.service.dao;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import my.service.domain.HacksonPO;

import java.util.Iterator;

public class HacksonDao {

    private static AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_EAST_1).build();

//    private static  AmazonDynamoDB dynamoDBClient =
//            AmazonDynamoDBClientBuilder.standard()
//            .withEndpointConfiguration(
//                    new AwsClientBuilder.EndpointConfiguration
//                            ("http://localhost:8000", "us-east-1"))
//            .build(); // local DynamoDB

    private static DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
    private final static  String tableName="HacksonTable";
    private final static  String Index_metaData="GSI_1";



    public static String addHackson(HacksonPO hacksonPO) {
        Table hacksonTable = dynamoDB.getTable(tableName);
        Item item = new Item().withPrimaryKey("Id",
                hacksonPO.getId(), "metaData", hacksonPO.getMetaData());
        item.withString("hacksonName",hacksonPO.getHacksonName());
        item.withString("descrption",hacksonPO.getDescrption());
        item.withString("hacksonState",hacksonPO.getHacksonState());
        hacksonTable.putItem(item);
        System.out.println("insert into dynamoDB success");
        return hacksonPO.getId();
    }

    public static String queryHacksonDeatilByHacksonId(String id) {
        Table hacksonTable = dynamoDB.getTable(tableName);
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("Id", id,
                "metaData", "Details");
        Item outcome = null;
        try {
            System.out.println("Attempting to read the item...");
            outcome = hacksonTable.getItem(spec);
            System.out.println("GetItem succeeded: " + outcome);
        }
        catch (Exception e) {
            System.err.println("Unable to read item: " + id );
            System.err.println(e.getMessage());
        }
        return  outcome.toJSONPretty();
    }


    public static String queryHacksonInfoByUserId(HacksonPO hacksonPO) {
        Table hacksonTable = dynamoDB.getTable(tableName);
        Index metaDataIndex = hacksonTable.getIndex(Index_metaData);//获取索引
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("metaData = :v_metaData")
                .withValueMap(new ValueMap()
                        .withString(":v_metaData", hacksonPO.getMetaData()));
        ItemCollection<QueryOutcome> items = metaDataIndex.query(spec);
        System.out.println("queryHacksonInfoByUserId");
        Iterator<Item> iterator = items.iterator();
        Item hacksonItem=null;
        while (iterator.hasNext()) {
            hacksonItem = iterator.next();
        }
        return  hacksonItem.toJSONPretty();
    }


    public String queryHacksonInfoByProjectId(HacksonPO hacksonPO) {
        Table hacksonTable = dynamoDB.getTable(tableName);
        Index metaDataIndex = hacksonTable.getIndex(Index_metaData);//获取索引
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("metaData = :v_metaData")
                .withValueMap(new ValueMap()
                        .withString(":v_metaData", hacksonPO.getMetaData()));
        ItemCollection<QueryOutcome> items = metaDataIndex.query(spec);
        System.out.println("queryHacksonInfoByUserId");
        Iterator<Item> iterator = items.iterator();
        Item hacksonItem=null;
        while (iterator.hasNext()) {
            hacksonItem = iterator.next();
        }
        return  hacksonItem.toJSONPretty();
    }



    public String deleteHackson(String id){
        Table hacksonTable = dynamoDB.getTable(tableName);
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("Id", id, "metaData", "Details"));
        DeleteItemOutcome deleteItemOutcome=
                hacksonTable.deleteItem(deleteItemSpec);
        return  deleteItemOutcome.getItem().get("Id").toString();
    }

}
