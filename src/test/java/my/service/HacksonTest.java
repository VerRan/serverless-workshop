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
public class HacksonTest extends TestSparkJavaUtil {

    @Test
    public void TestA_cleanResouce(){
        String id = null;
        try {
            id = HacksonDomain.queryHacksonIdByHackSonName("java");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(id!=null&&!id.equals("")){
            HacksonDomain.deleteHackson(id);
        }
    }


    @Test
    public void TestB_addhackson() {
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson", HttpMethod.POST)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("hacksonName","java")
                .queryString("descrption","java description")
                .queryString("token","123")
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void TestC_findHacksonByName() {
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/getbyName/java", HttpMethod.GET)
                .header(HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON
                )
                .queryString("token","123")
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getBody().contains("java"));
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }


    @Test
    public void TestD_queryHacksonDeatilByHacksonId() {
        String  id = HacksonDomain.queryHacksonIdByHackSonName("java");
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/"+id, HttpMethod.GET)
                .header(HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON
                )
                .queryString("token","123")
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        assertTrue(response.getBody().contains("java"));
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }



    @Test
    public void TestE_updateHackson() {
        String  id = HacksonDomain.queryHacksonIdByHackSonName("java");
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/"+id, HttpMethod.PUT)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_FORM_URLENCODED)
                .queryString("hacksonName","spring-workshop")
                .queryString("descrption","spring description")
                .queryString("token","123")
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        handle(requestStream, responseStream);
        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertFalse(response.isBase64Encoded());
        String ret = HacksonDomain.findHacksonByName("spring-workshop");
        assertTrue(ret.contains("spring-workshop"));
        assertTrue(response.getMultiValueHeaders().containsKey(HttpHeaders.CONTENT_TYPE));
        assertTrue(response.getMultiValueHeaders().getFirst(HttpHeaders.CONTENT_TYPE).startsWith(MediaType.APPLICATION_JSON));
    }



    @Test
    public void TestF_deleteHackson() {
        String  id = HacksonDomain.queryHacksonIdByHackSonName("spring-workshop");
        InputStream requestStream = new AwsProxyRequestBuilder("/api/hackson/"+id, HttpMethod.DELETE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
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


}
