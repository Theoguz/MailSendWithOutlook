package com.example.mailsendwithoutlook.service;


import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;
import com.example.mailsendwithoutlook.model.EventRequest;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.GraphServiceClient;

import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@Service
public class GraphAPIService {
    private final GraphServiceClient<Request> userClient;

    public GraphAPIService() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("app.clientId", "3282d08a-6408-4d6e-bfda-e50b2b7666e2");
        properties.setProperty("app.tenantId", "d348f9ab-4879-4a60-ab50-5c420b11470c");
        properties.setProperty("app.graphUserScopes", "User.Read,Mail.Send,Calendars.ReadWrite,OnlineMeetings.ReadWrite");

        final List<String> graphUserScopes = Arrays.asList(properties.getProperty("app.graphUserScopes").split(","));

        DeviceCodeCredential deviceCodeCredential = new DeviceCodeCredentialBuilder()
                .clientId(properties.getProperty("app.clientId"))
                .tenantId(properties.getProperty("app.tenantId"))
                .build();

        TokenCredentialAuthProvider authProvider =
                new TokenCredentialAuthProvider(graphUserScopes, deviceCodeCredential);

        this.userClient = GraphServiceClient.builder()
                .authenticationProvider(authProvider)
                .buildClient();
    }

    public void sendEmail(String subject, String body, String recipient) throws Exception {
        Message message = new Message();
        message.subject = subject;
        message.body = new ItemBody();
        message.body.content = body;
        message.body.contentType = BodyType.TEXT;

        Recipient toRecipient = new Recipient();
        toRecipient.emailAddress = new EmailAddress();
        toRecipient.emailAddress.address = recipient;
        message.toRecipients = List.of(toRecipient);

        userClient.me()
                .sendMail(UserSendMailParameterSet.newBuilder()
                        .withMessage(message)
                        .build())
                .buildRequest()
                .post();
    }

    public void sendEvent(EventRequest eventRequest) throws Exception {

        Event event = new Event();
        event.subject = eventRequest.getSubject();

        event.body = new ItemBody();
        event.body.content = eventRequest.getBody().content;
        event.body.contentType = BodyType.TEXT;

        event.start = new DateTimeTimeZone();
        event.start.dateTime = eventRequest.getStart().dateTime;
        event.start.timeZone = "UTC";

        event.end = new DateTimeTimeZone();
        event.end.dateTime = eventRequest.getEnd().dateTime;
        event.end.timeZone = "UTC";

        event.location = new Location();
        event.location.displayName = eventRequest.getLocation().displayName;

        for (Attendee attendee : eventRequest.getAttendees()) {
            event.attendees = new LinkedList<>();
            event.attendees.add(attendee);
        }

        event.isOnlineMeeting = true;
        event.onlineMeetingProvider = OnlineMeetingProviderType.SKYPE_FOR_CONSUMER;


        Event result=  userClient.me()
                .events()
                .buildRequest()
                .post(event);

        System.out.println("Event created: " + result.id);
        System.out.println("Event created: " + result.subject);
        System.out.println("Event created: " + result.webLink);
        System.out.println("Event created: " + result.onlineMeetingUrl);
        System.out.println("trans" +result.transactionId);
    }


    public void uploadImage(){

    }
}
