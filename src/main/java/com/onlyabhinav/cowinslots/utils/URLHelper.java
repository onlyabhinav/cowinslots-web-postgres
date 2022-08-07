package com.onlyabhinav.cowinslots.utils;

import com.onlyabhinav.cowinslots.models.URLObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class URLHelper {
    private static Logger logger = LoggerFactory.getLogger(URLHelper.class);

    private List<URLObj> urls;

    private String DATE_PLACEHOLDER = "_DATE_STR_";
    private String DISTRICT_ID_PLACEHOLDER = "_DISTT_STR_"; // 664 - Kanpur, Agra - 622
    private String districtUrl = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" + DISTRICT_ID_PLACEHOLDER + "&date=" + DATE_PLACEHOLDER; //28-05-2021

    private String lastUpdatedDate = null;

    private int lastURLIndex = -1;

    @PostConstruct
    public void loadData() {
        urls = new ArrayList<URLObj>();

        lastUpdatedDate = getTodayDateStr();
        logger.info("LAST UPDATED DATE Set to = {}", lastUpdatedDate);

        urls.add(new URLObj(getURL("664")));
        urls.add(new URLObj(getURL("622")));
        urls.add(new URLObj(getURL("363")));
        urls.add(new URLObj(getURL("314")));
        urls.add(new URLObj(getURL("319")));

    }

    public URLObj getNextURL() {

        if (!lastUpdatedDate.equals(getTodayDateStr())) {
            logger.info("Date Changed. OLD DATE = {}. Re-building URLs for NEW DATE = {}", lastUpdatedDate, getTodayDateStr());
            urls = null;

            // Refresh URL Data
            loadData();
        }

        if (lastURLIndex + 1 == urls.size()) {
            lastURLIndex = -1;
        }

        lastURLIndex++;

        return urls.get(lastURLIndex);
    }


    private String getURL(String distCode) {
        return districtUrl.replace(DATE_PLACEHOLDER, lastUpdatedDate).replace(DISTRICT_ID_PLACEHOLDER, distCode);
    }

    public List<URLObj> getUrls() {

        if (lastUpdatedDate.equals(getTodayDateStr())) {
            logger.info("Same date. {}", lastUpdatedDate);
            return urls;
        }

        logger.info("Date Changed. OLD DATE = {}. Re-building URLs for NEW DATE = {}", lastUpdatedDate, getTodayDateStr());
        urls = null;

        // Refresh URL Data
        loadData();

        return urls;
    }

    //28-05-2021
    private String getTodayDateStr() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

}
