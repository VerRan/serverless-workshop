package my.service;

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import my.service.domain.HacksonDomain;
import my.service.util.TestSparkJavaUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.junit.Assert.*;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserMgtTest extends TestSparkJavaUtil {

//    @Test
    public void TestA_cleanResouce(){
        String id = null;
        try {
            id = HacksonDomain.queryUserIdByUserName("lht");
            System.out.println(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(id!=null&&!id.equals("")){
            HacksonDomain.deleteHackson(id,"userId-"+id);
        }
    }



//        @Test
    public void signup() {
        InputStream requestStream = new AwsProxyRequestBuilder("/signup", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("userName","test")
                .queryString("password","Liuhengtao123*")
                .queryString("email","lht@amazon.com")
                
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




//    @Test
    public void verifyaccesscode() {
        InputStream requestStream = new AwsProxyRequestBuilder("/verifyaccesscode", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("userName","test")
                .queryString("accessCode","380726")

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


//    @Test
    public void signIn() {
        InputStream requestStream = new AwsProxyRequestBuilder("/signin", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("userName","test")
                .queryString("password","Liuhengtao123*")
                
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        System.out.println(response.getBody());
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }




//    @Test
    public void testUpdatePassword(){
        InputStream requestStream = new AwsProxyRequestBuilder("/api/user/updatepassword/", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("userName","lht")
                .queryString("password","123")
                .queryString("passwordNew","456")
                .buildStream();//构造请求参数

        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        handle(requestStream, responseStream);

        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertTrue(HacksonDomain.queryUserInfoByUserName("lht").contains("456"));

        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }




    @Test
    /**create user ,userId and metaData is same to describe the item is userInfo Item */
    public void TestB_testAddUser(){
        InputStream requestStream = new AwsProxyRequestBuilder("/api/user", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("userName","lht")
                .queryString("password","lht")
                .queryString("email","lht@123")
                .buildStream();//构造请求参数

        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        handle(requestStream, responseStream);

        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }

    @Test

    public void TestB_testQueryUserProfile(){
        InputStream requestStream = new AwsProxyRequestBuilder("/api/user/aa7693ff4bc041ecac6a3010d5ff93c9", HttpMethod.GET)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .buildStream();//构造请求参数

        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);


        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        System.out.println(response.getBody());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }


//    @Test
    /**when create user ,userId and metaData is same to describe the item is userInfo Item */
    /**use case : when user  join into the hackoson ,then add a item  Id is haksonId ,and user info with user detail(get from item metadata with userId) */
    public void TestC_joinHackathon() {
//        String  id = HacksonDomain.queryHacksonIdByHackSonName("java");
//        System.out.println("hacksonId is "+id);
//        String  userId = HacksonDomain.queryUserInfoByUserName("VerRan");
//        System.out.println("userId is "+userId);
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/join", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .queryString("id","39c8c9b97c6c430ab6748eb3300030de")
                .queryString("userId","aa7693ff4bc041ecac6a3010d5ff93c9")// next will update with queryUserIdByUserName function
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        System.out.println(response.getBody());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertTrue(response.getBody().contains("join"));
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }


//    @Test
    /**use case : when user  join  repeat will return error) */
    public void TestC_repeat_joinHackathon() {
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/join", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .queryString("id","39c8c9b97c6c430ab6748eb3300030de")
                .queryString("userId","aa7693ff4bc041ecac6a3010d5ff93c9")// next will update with queryUserIdByUserName function
                   .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        System.out.println(response.getBody());
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatusCode());

        assertTrue(response.getBody().contains("Sorry"));
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }



//    @Test
    /**use case : when user  exit  the hackoson ,then add a item  Id is haksonId ,and user info with user detail(get from item metadata with userId) */
    public void TestC_exitHackathon() {
//        String  id = HacksonDomain.queryHacksonIdByHackSonName("java");
//        System.out.println("hacksonId is "+id);
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/exit", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .queryString("id","39c8c9b97c6c430ab6748eb3300030de")
                .queryString("userId","aa7693ff4bc041ecac6a3010d5ff93c9")// next will update with queryUserIdByUserName function
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        System.out.println(response.getBody());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertTrue(response.getBody().contains("exit"));
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }





//    @Test
    /**when user finish the task ,the project status will changed to completed**/
    public void TestD_completeProject(){
        String  id = "23e57d553b294451abcc48ed2916258c";//HacksonDomain.queryHacksonIdByHackSonName("java");
        String userId = "9b734f5e8a534d62a933d7b38b6b7d5d";
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/complete", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("id",id)
                .queryString("userId",userId)
                .buildStream();

        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);

        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        System.out.println(response.getBody());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertTrue(response.getBody().contains("complete"));

        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));

    }

//    @Test
    /**sponsor can score for the project**/
    public void TestE_projectScore(){
        String  id = "23e57d553b294451abcc48ed2916258c";//HacksonDomain.queryHacksonIdByHackSonName("java");
        String userId = "9b734f5e8a534d62a933d7b38b6b7d5d";
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/score", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("id",id)
                .queryString("userId",userId)
                .queryString("score","80")
                .buildStream();

        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);

        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        System.out.println(response.getBody());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertTrue(response.getBody().contains("score success"));

        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));

    }

}
