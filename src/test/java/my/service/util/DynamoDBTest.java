package my.service.util;

import my.service.domain.HacksonPO;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DynamoDBTest {

//    @Test
    public  void  testA_cleanData(){

        DynamoDBUtil dynamoDBUtil = new DynamoDBUtil();
        String json =  dynamoDBUtil.queryHacksonDeatilByHacksonId("4");
        System.out.println(json);
        if(json.contains("test-add")){
            dynamoDBUtil.deleteHackson("4");
        }
    }


    @Test
    public  void  testB_testAdd(){
        DynamoDBUtil dynamoDBUtil = new DynamoDBUtil();
        HacksonPO hacksonPO =new HacksonPO();
        hacksonPO.setId("4");
        hacksonPO.setMetaData("Details");
        hacksonPO.setHacksonName("test-add");
        dynamoDBUtil.addHackson(hacksonPO);
        Assert.assertTrue(dynamoDBUtil.queryHacksonDeatilByHacksonId(hacksonPO.getId()).contains("test-add"));
    }

    @Test
    public  void  testC_queryHacksonInfoByHacksonId(){
        DynamoDBUtil dynamoDBUtil = new DynamoDBUtil();
        String json =  dynamoDBUtil.queryHacksonDeatilByHacksonId("4");
        Assert.assertTrue(json.contains("test-add"));
    }


//    @Test
//    public  void  queryHacksonInfoByUserId(){//query hackson list user 1 join in.
//        DynamoDBUtil dynamoDBUtil = new DynamoDBUtil();
//        HacksonPO hacksonPO =new HacksonPO();
//        hacksonPO.setMetaData("userId-1");
//        String json  =  dynamoDBUtil.queryHacksonInfoByUserId(hacksonPO);
//        System.out.println(json);
//        Assert.assertTrue(json.contains("liuhengtao"));
//    }
//
//
//    @Test
//    public  void  queryHacksonInfoByProjectId(){//query hackson list user 1 join in.
//        DynamoDBUtil dynamoDBUtil = new DynamoDBUtil();
//        HacksonPO hacksonPO =new HacksonPO();
//        hacksonPO.setMetaData("projId-2");
//        String json  =  dynamoDBUtil.queryHacksonInfoByProjectId(hacksonPO);
//        System.out.println(json);
//        Assert.assertTrue(json.contains("complete"));
//    }

}
