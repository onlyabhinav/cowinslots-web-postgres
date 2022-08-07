package com.onlyabhinav.cowinslots.utils;

import com.onlyabhinav.cowinslots.models.Root;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HTTPHelper {

    private static Logger logger = LoggerFactory.getLogger(HTTPHelper.class);


    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public String openHttpURL(String url) throws Exception {

        final HttpGet httpGet = new HttpGet(url);

        addHeaders(httpGet);

        try (final CloseableHttpResponse response = httpClient.execute(httpGet)) {
            logger.info("HTTP Status = {}", response.getStatusLine().toString());

            // Get hold of the response entity
            final HttpEntity entity = response.getEntity();

            if (entity != null) {
                // return it as a String
                return EntityUtils.toString(entity);
            }
        }

        return null;
    }

    public final static void main(final String[] args) throws Exception {

        String url = "https://cdn-api.co-vin.in/api/v2/admin/location/states";


        new HTTPHelper().openHttpURL(url);
    }


    private void addHeaders(HttpGet httpget) {
        httpget.addHeader("authority", "cdn-api.co-vin.in");
        httpget.addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"");
        httpget.addHeader("accept", "application/json, text/plain, */*");
        httpget.addHeader("sec-ch-ua-mobile", "?0");
        httpget.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        httpget.addHeader("origin", "https://www.cowin.gov.in");
        httpget.addHeader("sec-fetch-mode", "cors");
        httpget.addHeader("sec-fetch-dest", "empty");
        httpget.addHeader("referer", "https://www.cowin.gov.in/");
        httpget.addHeader("accept-language", "en-GB,en-US;q=0.9,en;q=0.8");
        httpget.addHeader("if-none-match", "W/\"755-kSDAGwS0dhuJu/VuZ3UJpZ2STnc\"");
    }

}
