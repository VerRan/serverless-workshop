package my.service;

import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import my.service.util.TestSparkJavaUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class UserMgtTest extends TestSparkJavaUtil {

    @Test
    public void testAddUser(){
        InputStream requestStream = new AwsProxyRequestBuilder("/api/user", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .queryString("userName","lht")
                .queryString("password","123")
                .queryString("email","123@123")
                .buildStream();//构造请求参数

        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        handle(requestStream, responseStream);

        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertFalse(response.isBase64Encoded());
//        assertTrue(response.getBody().contains("pong"));
        assertTrue(response.getBody().contains("Hello, World!"));

        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }




    @Test
    public void signup() {
        InputStream requestStream = new AwsProxyRequestBuilder("/signup", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("userName","lht121233")
                .queryString("password","Dove1985*")
                .queryString("email","270366200@qq.com")
                .queryString("token","123")
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }


    @Test
    public void signIn() {
        InputStream requestStream = new AwsProxyRequestBuilder("/signin", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("userName","liuhengtao")
                .queryString("password","Dove1985*")
                .queryString("token","123")
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
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
