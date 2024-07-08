package com.example.demo.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        // Load client secrets.
        InputStream in = GoogleCalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Event createGoogleMeetEvent(DateTime startDateTime, DateTime endDateTime, DateTime endRecurrenceDate) throws Exception {
        startDateTime = this.convertToGMTPlus7(startDateTime);
        endDateTime = this.convertToGMTPlus7(endDateTime);
        endRecurrenceDate = convertToGMTPlus7(endRecurrenceDate);
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event event = new Event()
                .setSummary("Google Meet Event")
                .setDescription("A Google Meet event created programmatically");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Ho_Chi_Minh");
        event.setStart(start);

        ConferenceData conferenceData = new ConferenceData();
        ConferenceSolutionKey conferenceSolutionKey = new ConferenceSolutionKey();
        conferenceSolutionKey.setType("hangoutsMeet");

        CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
        createConferenceRequest.setRequestId("unique-request-id"); // Generate a unique ID for each request
        createConferenceRequest.setConferenceSolutionKey(conferenceSolutionKey);
        conferenceData.setCreateRequest(createConferenceRequest);

        event.setConferenceData(conferenceData);

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Ho_Chi_Minh");
        event.setEnd(end);

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(
                        new EventReminder().setMethod("email").setMinutes(24 * 60),
                        new EventReminder().setMethod("popup").setMinutes(10)
                ));
        // Determine the day of the week from startDateTime
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date(startDateTime.getValue()));
        int dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK);

        // Map Java Calendar day of week to RRULE BYDAY format
        String[] daysOfWeek = {"SU", "MO", "TU", "WE", "TH", "FR", "SA"};
        String byDay = daysOfWeek[dayOfWeek - 1]; // Java Calendar's week starts with Sunday (1)

        // Format the end recurrence date to the required format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String until = dateFormat.format(new Date(endRecurrenceDate.getValue()));


        // Set the recurrence rule based on the start day of the week
        event.setRecurrence(Collections.singletonList("RRULE:FREQ=WEEKLY;BYDAY=" + byDay + ";UNTIL=" + until));


        event.setReminders(reminders);

        Event createdEvent = service.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .setSendUpdates("all")
                .execute();

        System.out.printf("Event created: %s\n", createdEvent.getHtmlLink());

        return createdEvent;
    }

    public static DateTime convertToGMTPlus7(DateTime inputDateTime) {
        // Create a calendar instance and set the time to the input DateTime
        java.util.Calendar calendar = java.util.Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(inputDateTime.getValue());

        // Subtract 7 hours from the time
        calendar.add(java.util.Calendar.HOUR_OF_DAY, -7);

        // Create a new DateTime object with the adjusted time
        return new DateTime(calendar.getTimeInMillis());
    }
}

