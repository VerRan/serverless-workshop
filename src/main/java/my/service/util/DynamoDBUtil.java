package my.service.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import my.service.domain.HacksonPO;

import java.util.Iterator;

public class DynamoDBUtil {

    //create DynamoDBClient
    private static AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.AP_NORTHEAST_1).build();
    private static  DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
    private final static  String tableName="HackathonTable";

    private final static  String Index_hacksonName="hacksonName-index";
    private final static  String Index_userName="userName-index";

    public static String addHackson( HacksonPO hacksonPO) {
        Table hacksonTable = dynamoDB.getTable(tableName);
        Item item = new Item().withPrimaryKey("Id",
                hacksonPO.getId(), "metaData", hacksonPO.getMetaData())
                .withString("hackathonName",hacksonPO.getHacksonName())
                ;
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


    public static String queryHacksonInfoByHackSonName(String hacksonName) {
        Item hacksonItem = null;
        try {
            Table hacksonTable = dynamoDB.getTable(tableName);
            Index index_hacksonName = hacksonTable.getIndex(Index_hacksonName);//获取索引
            QuerySpec spec = new QuerySpec().withKeyConditionExpression("hackathonName = :v_hacksonName")
                    .withValueMap(new ValueMap()
                            .withString(":v_hacksonName", hacksonName));
            ItemCollection<QueryOutcome> items = index_hacksonName.query(spec);

            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                hacksonItem = iterator.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  hacksonItem.toJSONPretty();
    }


    public static String queryHacksonIdByHackSonName(String hacksonName) {
        Item hacksonItem = null;
        String id = "";

        try {
            Table hacksonTable = dynamoDB.getTable(tableName);
            Index index_hacksonName = hacksonTable.getIndex(Index_hacksonName);//获取索引
            QuerySpec spec = new QuerySpec().withKeyConditionExpression("hackathonName = :v_hacksonName")
                    .withValueMap(new ValueMap()
                            .withString(":v_hacksonName", hacksonName));
            ItemCollection<QueryOutcome> items = index_hacksonName.query(spec);
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                hacksonItem = iterator.next();
                id = hacksonItem.get("Id").toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  id;
    }




    public static String queryUserInfoByUserName(String userName) {
        Item hacksonItem = null;
        try {
            Table hacksonTable = dynamoDB.getTable(tableName);
            Index index_hacksonName = hacksonTable.getIndex(Index_userName);//获取索引
            QuerySpec spec = new QuerySpec().withKeyConditionExpression("userName = :v_userName")
                    .withValueMap(new ValueMap()
                            .withString(":v_userName", userName));
            ItemCollection<QueryOutcome> items = index_hacksonName.query(spec);
            System.out.println("queryUserInfoByUserName");
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                hacksonItem = iterator.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  hacksonItem.toJSONPretty();
    }



    public static String queryUserIdByUserName(String userName) {
        Item hacksonItem = null;
        String id = "";

        try {
            Table hacksonTable = dynamoDB.getTable(tableName);
            Index index_hacksonName = hacksonTable.getIndex(Index_userName);//获取索引
            QuerySpec spec = new QuerySpec().withKeyConditionExpression("userName = :v_userName")
                    .withValueMap(new ValueMap()
                            .withString(":v_userName", userName));
            ItemCollection<QueryOutcome> items = index_hacksonName.query(spec);
            System.out.println("queryUserInfoByUserName");
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                hacksonItem = iterator.next();
                id = hacksonItem.get("Id").toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  id;
    }


    public static void delelteUser(String userName) {
        Item hacksonItem = null;
        String id = "";

        try {
            Table hacksonTable = dynamoDB.getTable(tableName);
            Index index_hacksonName = hacksonTable.getIndex(Index_userName);//获取索引
            QuerySpec spec = new QuerySpec().withKeyConditionExpression("userName = :v_userName")
                    .withValueMap(new ValueMap()
                            .withString(":v_userName", userName));
            ItemCollection<QueryOutcome> items = index_hacksonName.query(spec);
            System.out.println("queryUserInfoByUserName");
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                hacksonItem = iterator.next();
                id = hacksonItem.get("Id").toString();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void deleteHackson(String id){
        Table hacksonTable = dynamoDB.getTable(tableName);
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("Id", id, "metaData", "Details"));
        DeleteItemOutcome deleteItemOutcome=
                hacksonTable.deleteItem(deleteItemSpec);
    }


}
