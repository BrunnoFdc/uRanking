package ga.brunnofdc.uranking.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import ga.brunnofdc.uranking.uRanking;
import ga.brunnofdc.uranking.utils.entities.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import static ga.brunnofdc.uranking.uRanking.GITHUB_REPO_NAME;

public class UpdateChecker implements Runnable {

    private final Logger logger = uRanking.getInstance().getLogger();
    private static final String LATEST_RELEASE_URL =
            "https://api.github.com/repos/" + GITHUB_REPO_NAME + "/releases/latest";

    public void run() {
        logger.info("Checking for updates...");
        Gson gson = new GsonBuilder().serializeNulls().create();
        try {
            JsonObject releaseDetails = gson.fromJson(fetchWebContent(LATEST_RELEASE_URL), JsonObject.class);
            Version latestVersion = new Version(releaseDetails.get("tag_name").getAsString());
            Version actualVersion = new Version(uRanking.getInstance().getDescription().getVersion());
            if(actualVersion.compareTo(latestVersion) < 0) {
                logger.info("Update available! New version: " + latestVersion.getValue());
                logger.info("Click to download: https://bit.ly/uRankingUpdate");
            }

        } catch (IOException ignored) {}
    }

    private static String fetchWebContent(String webAdress) throws IOException {

        final int OK = 200;
        URL url = new URL(webAdress);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int responseCode = connection.getResponseCode();
        if(responseCode == OK){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        }

        return null;
    }

}
