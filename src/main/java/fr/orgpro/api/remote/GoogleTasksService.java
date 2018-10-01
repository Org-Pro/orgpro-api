package fr.orgpro.api.remote;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksScopes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class GoogleTasksService {
    private static GoogleTasksService INSTANCE = new GoogleTasksService();

    private NetHttpTransport HTTP_TRANSPORT;
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Google Tasks API Java Quickstart";
    private static final List<String> SCOPES = Collections.singletonList(TasksScopes.TASKS);

    /**
     * Constructor, initialize HTTP_TRANSPORT for future request
     */
    GoogleTasksService() {
        try {
            this.HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, String credential_user) throws IOException {
        // Load client secrets.
        InputStream in = GoogleTasksApi.class.getResourceAsStream(credential_user);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH + credential_user)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * @param credentialFilePath path to the Google credential file
     * @return A Tasks that can be use by the application to get
     * or send data from or to google
     * the google api
     * @throws IOException  If the credentials.json file cannot be found.
     */
    public Tasks getTasks(String credentialFilePath) throws IOException {
        return new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT, credentialFilePath))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static GoogleTasksService getInstance() {
        return INSTANCE;
    }
}
