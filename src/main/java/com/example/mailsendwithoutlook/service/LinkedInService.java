package com.example.mailsendwithoutlook.service;

import com.google.gson.JsonParser;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@Service
public class LinkedInService {
    @Value("${accessToken}")
    String accessToken;


    @Value("${LINKEDIN_POST_URL}")
    String LINKEDIN_POST_URL;

    @Value("${LINKEDIN_IMAGE_POST_URL}")
    String LINKEDIN_IMAGE_POST_URL;



    public void postToLinkedIn(String text) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(LINKEDIN_POST_URL);

        String json = "{"
                + "\"author\": \"urn:li:person:kmlfhsmKOQ\","
                + "\"lifecycleState\": \"PUBLISHED\","
                + "\"specificContent\": {"
                + "\"com.linkedin.ugc.ShareContent\": {"
                + "\"shareCommentary\": {\"text\": \"" + text + "\"},"
                + "\"shareMediaCategory\": \"NONE\""
                + "}"
                + "},"
                + "\"visibility\": {\"com.linkedin.ugc.MemberNetworkVisibility\": \"PUBLIC\"}"
                + "}";

        post.setEntity(new StringEntity(json));
        post.setHeader("Authorization", "Bearer " + accessToken);
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = client.execute(post)) {
            EntityUtils.consume(response.getEntity());

        }
    }


    public void postToLinkedInImage(String path) throws IOException {

        try {

            JsonObject uploadResponse = registerUpload(accessToken);
            File imageFile = new File(path);
            uploadImage(uploadResponse, imageFile, accessToken);

            createShare(accessToken, uploadResponse);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createShare(String accessToken, JsonObject uploadResponse) throws UnsupportedEncodingException {
        String asset = uploadResponse.get("value").getAsJsonObject()
                .get("asset").getAsString();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(LINKEDIN_POST_URL);

        String json = "{"
                + "\"author\": \"urn:li:person:kmlfhsmKOQ\","
                + "\"lifecycleState\": \"PUBLISHED\","
                + "\"specificContent\": {"
                + "\"com.linkedin.ugc.ShareContent\": {"
                + "\"shareCommentary\": {\"text\": \"Here is a photo!\"},"
                + "\"shareMediaCategory\": \"IMAGE\","
                + "\"media\": [{\"status\": \"READY\", \"description\": {\"text\": \"Description\"}, \"media\": \"" + asset + "\", \"title\": {\"text\": \"Title\"}}]"
                + "}"
                + "},"
                + "\"visibility\": {\"com.linkedin.ugc.MemberNetworkVisibility\": \"PUBLIC\"}"
                + "}";

        post.setEntity(new StringEntity(json));
        post.setHeader("Authorization", "Bearer " + accessToken);
        post.setHeader("Content-Type", "application/json");


        try (CloseableHttpResponse response = client.execute(post)) {
            EntityUtils.consume(response.getEntity());
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void uploadImage(JsonObject uploadResponse, File imageFile, String accessToken) throws IOException {

        String uploadUrl = uploadResponse.get("value").getAsJsonObject()
                .get("uploadMechanism").getAsJsonObject()
                .get("com.linkedin.digitalmedia.uploading.MediaUploadHttpRequest").getAsJsonObject()
                .get("uploadUrl").getAsString();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(uploadUrl);

        post.setHeader("Authorization", "Bearer " + accessToken);
        post.setHeader("Content-Type", "application/octet-stream");


        FileEntity fileEntity = new FileEntity(imageFile, ContentType.DEFAULT_BINARY);
        post.setEntity(fileEntity);
        try (CloseableHttpResponse response = client.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);
            if (statusCode == 401) {
                System.err.println("Unauthorized: Token geçerli değil veya gerekli izinlere sahip değil.");
            }
        }


    }


    private JsonObject registerUpload(String accessToken) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(LINKEDIN_IMAGE_POST_URL);

        String json = "{"
                + "\"registerUploadRequest\": {"
                + "\"recipes\": [\"urn:li:digitalmediaRecipe:feedshare-image\"],"
                + "\"owner\": \"urn:li:person:kmlfhsmKOQ\","
                + "\"serviceRelationships\": [{\"relationshipType\": \"OWNER\", \"identifier\": \"urn:li:userGeneratedContent\"}]"
                + "}"
                + "}";

        post.setEntity(new StringEntity(json));
        post.setHeader("Authorization", "Bearer " + accessToken);
        post.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = client.execute(post)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            return JsonParser.parseString(responseBody).getAsJsonObject();
        }
    }


}