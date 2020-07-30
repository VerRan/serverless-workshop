package my.service;


import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;

import my.service.domain.HacksonDomain;
import my.service.util.TestSparkJavaUtil;
import org.junit.Assert;
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
public class HackathonMgtTest extends TestSparkJavaUtil {

    @Test
    public void TestA_cleanResouce(){
        String id = null;
        try {
            id = HacksonDomain.queryHacksonIdByHackSonName("spring");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(id!=null&&!id.equals("")){
            HacksonDomain.deleteHackson(id,"Details");
        }
    }


    @Test
    /**use case : sponsor can add the hackathon, for example start a serverless workshop hackathon*/
    public void TestB_addhackson() {
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("hacksonName","spring")
                .queryString("descrption","spring description")
                
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
    /**use case : sponsor/developer can find the hackathon*/
    public void TestC_findHacksonByNameNotExsits() {
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/getbyName/spring", HttpMethod.GET)
                .header(HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON
                )

                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        System.out.println(response.getBody());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getBody().contains("not exists"));
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }

    @Test
    /**use case : sponsor/developer can find the hackathon*/
    public void TestC_findHacksonByName() {
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/getbyName/spring-test", HttpMethod.GET)
                .header(HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON
                )
                
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        System.out.println(response.getBody());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getBody().contains("spring"));
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }


    @Test
    /**use case : front page can list the hackathon ,when use click the hackathon can use this method to list the detail */
    public void TestD_queryHacksonDeatilByHacksonId() {
        String  id = HacksonDomain.queryHacksonIdByHackSonName("spring");
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/"+id, HttpMethod.GET)
                .header(HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON
                )
                
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        System.out.println(response.getBody());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getBody().contains("spring"));
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }



    @Test
    /**use case : sponsor can modify the hackathon*/
    public void TestE_updateHackson() {
//        String  id = HacksonDomain.queryHacksonIdByHackSonName("spring");
        String id ="4049798849b3438f8f0cfcd7531fa81a";
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/"+id, HttpMethod.PUT)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("hacksonName","spring-test")
                .queryString("descrption","spring workshop description")
                
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
    /**use case : sponsor can start the  hackathon ,then the project will be created for every user*/
    public void TestF_startHackathon() {
        String  id = HacksonDomain.queryHacksonIdByHackSonName("spring");
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/start/"+id, HttpMethod.PUT)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        System.out.println(response.getBody());
        assertTrue(response.getBody().contains("start"));
//        assertTrue(response.getBody().contains("VerRan"));
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }


    @Test
    /**use case : front use this method to list the hackathons Paganization need to implement! */
    public void TestG_listHackathons() {
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hacksons", HttpMethod.GET)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        System.out.println(response.getBody());
//        assertTrue(response.getBody().contains("create"));
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }

    @Test
    /**use case : sponsor can delete the hackathon,when the hackson was started the record can't be deleted */
    public void TestK_deleteHackson() {
        String  id = HacksonDomain.queryHacksonIdByHackSonName("spring");
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/"+id, HttpMethod.DELETE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                
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





}
