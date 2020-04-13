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
            .withRegion(Regions.US_EAST_1).build();
    private static  DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);
    private final static  String tableName="HacksonTable";
    private final static  String Index_metaData="GSI_1";

    private final static  String Index_hacksonName="hacksonName-index";


    public static String addHackson( HacksonPO hacksonPO) {
        Table hacksonTable = dynamoDB.getTable(tableName);
        Item item = new Item().withPrimaryKey("Id",
                hacksonPO.getId(), "metaData", hacksonPO.getMetaData())
                .withString("hacksonName",hacksonPO.getHacksonName())
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
            QuerySpec spec = new QuerySpec().withKeyConditionExpression("hacksonName = :v_hacksonName")
                    .withValueMap(new ValueMap()
                            .withString(":v_hacksonName", hacksonName));
            ItemCollection<QueryOutcome> items = index_hacksonName.query(spec);
            System.out.println("queryHacksonInfoByHackSonName");
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
            QuerySpec spec = new QuerySpec().withKeyConditionExpression("hacksonName = :v_hacksonName")
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



    public static String queryHacksonInfoByUserId(HacksonPO hacksonPO) {
        Table hacksonTable = dynamoDB.getTable(tableName);
        Index metaDataIndex = hacksonTable.getIndex(Index_metaData);//获取索引
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("metaData = :v_metaData")
                .withValueMap(new ValueMap()
                        .withString(":v_metaData", hacksonPO.getMetaData()));
        ItemCollection<QueryOutcome> items = metaDataIndex.query(spec);
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
        Iterator<Item> iterator = items.iterator();
        Item hacksonItem=null;
        while (iterator.hasNext()) {
            hacksonItem = iterator.next();
        }
        return  hacksonItem.toJSONPretty();
    }



    public void deleteHackson(String id){
        Table hacksonTable = dynamoDB.getTable(tableName);
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("Id", id, "metaData", "Details"));
        DeleteItemOutcome deleteItemOutcome=
                hacksonTable.deleteItem(deleteItemSpec);
    }


    private static void updateAddNewAttribute() {
        Table table = dynamoDB.getTable(tableName);

        try {

            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", 121)
                    .withUpdateExpression("set #na = :val1").withNameMap(new NameMap().with("#na", "NewAttribute"))
                    .withValueMap(new ValueMap().withString(":val1", "Some value")).withReturnValues(ReturnValue.ALL_NEW);

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

            // Check the response.
            System.out.println("Printing item after adding new attribute...");
            System.out.println(outcome.getItem().toJSONPretty());

        }
        catch (Exception e) {
            System.err.println("Failed to add new attribute in " + tableName);
            System.err.println(e.getMessage());
        }
    }



    private static void updateMultipleAttributes() {
        Table table = dynamoDB.getTable(tableName);

        try {

            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", 120)
                    .withUpdateExpression("add #a :val1 set #na=:val2")
                    .withNameMap(new NameMap().with("#a", "Authors").with("#na", "NewAttribute"))
                    .withValueMap(
                            new ValueMap().withStringSet(":val1", "Author YY", "Author ZZ").withString(":val2", "someValue"))
                    .withReturnValues(ReturnValue.ALL_NEW);

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

            // Check the response.
            System.out.println("Printing item after multiple attribute update...");
            System.out.println(outcome.getItem().toJSONPretty());

        }
        catch (Exception e) {
            System.err.println("Failed to update multiple attributes in " + tableName);
            System.err.println(e.getMessage());

        }
    }

    private static void updateExistingAttributeConditionally() {

        Table table = dynamoDB.getTable(tableName);

        try {

            // Specify the desired price (25.00) and also the condition (price =
            // 20.00)

            UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("Id", 120)
                    .withReturnValues(ReturnValue.ALL_NEW).withUpdateExpression("set #p = :val1")
                    .withConditionExpression("#p = :val2").withNameMap(new NameMap().with("#p", "Price"))
                    .withValueMap(new ValueMap().withNumber(":val1", 25).withNumber(":val2", 20));

            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

            // Check the response.
            System.out.println("Printing item after conditional update to new attribute...");
            System.out.println(outcome.getItem().toJSONPretty());

        }
        catch (Exception e) {
            System.err.println("Error updating item in " + tableName);
            System.err.println(e.getMessage());
        }
    }


}
