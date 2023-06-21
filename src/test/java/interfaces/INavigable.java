package interfaces;

import enums.WaitTimes;
import helper.Log;
import helper.ScenarioContext;
import utility.TimeLimit;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public interface INavigable {

    Set<Integer> FAILED_RESPONSES = new HashSet<>() {
        {
            add(HttpURLConnection.HTTP_INTERNAL_ERROR);// HTTP-500
            add(HttpURLConnection.HTTP_NOT_IMPLEMENTED);// HTTP-501
            add(HttpURLConnection.HTTP_BAD_GATEWAY);// HTTP-502
            add(HttpURLConnection.HTTP_UNAVAILABLE);// HTTP-503
            add(HttpURLConnection.HTTP_GATEWAY_TIMEOUT);// HTTP-504
            add(HttpURLConnection.HTTP_VERSION);// HTTP-505
            add(HttpURLConnection.HTTP_BAD_REQUEST);// HTTP-400
            add(HttpURLConnection.HTTP_UNAUTHORIZED);// HTTP-401
            add(HttpURLConnection.HTTP_FORBIDDEN);// HTTP-403
            add(HttpURLConnection.HTTP_NOT_FOUND);// HTTP-404
        }
    };


    String getURL();

    default void navigateTo() {
        //Navigate to URL
        String url = getURL();
        ScenarioContext.driver.get(url);

        //Evaluate connection status of URL site
        TimeLimit limit = new TimeLimit(WaitTimes.WEBSITE_CONNECTIVITY_TIMEOUT);
        String responseMsg = null;
        int responseCode = 0;
        while(limit.timeLeft()) {
            HttpURLConnection.setFollowRedirects(false);
            try {
                HttpURLConnection con = (HttpURLConnection) new URL(ScenarioContext.driver.getCurrentUrl()).openConnection();
                con.setRequestMethod("GET");
                con.setDoOutput(false);
                con.setConnectTimeout((int) WaitTimes.HTTP_REQUEST_TIMEOUT.WAIT_TIME.toMillis()); // In Milliseconds
                con.setReadTimeout((int) WaitTimes.HTTP_REQUEST_TIMEOUT.WAIT_TIME.toMillis()); // In Milliseconds
                responseCode = con.getResponseCode();
                responseMsg = con.getResponseMessage();
                // Specify error codes to action a retry request here
                if (FAILED_RESPONSES.contains(responseCode)) {
                    // Unsuccessful Response - Retry request
                    Log.error("Error response: HTTP " + responseCode + " - " + responseMsg.trim());
                    ScenarioContext.driver.manage().deleteAllCookies();
                    ScenarioContext.driver.get(url);
                    con.disconnect();
                    Log.info("Retrying to navigate to URL");
                    TimeLimit.waitFor(2);
                    continue;
                }
                // Successful Response - Continue
                Log.info("Successful response: HTTP " + responseCode + " from " + ScenarioContext.driver.getCurrentUrl());
                return;
            } catch (IOException cannotVerifyResponse) {
                // Cannot Verify Response - Continue
                Log.warn("Unable to verify HTTP response from \"" + ScenarioContext.driver.getCurrentUrl() + "\", proceeding");
                return;
            }
        }
        // Unsuccessful Response/Failed to Connect - Throw Error
        throw new RuntimeException(String.format("Received HTTP %d (%s) response when navigating to %s",
                responseCode,
                responseMsg,
                ScenarioContext.driver.getCurrentUrl()));
    }

}
