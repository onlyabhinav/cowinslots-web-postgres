package com.onlyabhinav.cowinslots.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlyabhinav.cowinslots.models.SlotStatus;
import com.onlyabhinav.cowinslots.models.URLObj;
import com.onlyabhinav.cowinslots.service.CowinSlotCheckService;
import com.onlyabhinav.cowinslots.utils.URLHelper;

@RestController
public class CowinSlotsController {
	
    private static Logger logger = LoggerFactory.getLogger(CowinSlotsController.class);

	
    @Autowired
    private URLHelper urlHelper;
    
    @Autowired
    public CowinSlotCheckService vaccineService;
	
	@GetMapping("hi")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@GetMapping("check")
	public List<String>  checkSlots() {
		logger.info("Checking for slots at :: {}",System.currentTimeMillis());
		
		//runJob();
		
		return runJob();
	}

	
	private List<String> runJob() {
		{

	        URLObj urlObj = urlHelper.getNextURL();

	        String urlDynamic = urlObj.getUrl() + "&t=" + System.currentTimeMillis();
	        
	        List<String> result = new ArrayList<>();

	        logger.info(" --- Checking Slots for URL = {} --- ", urlDynamic);

	        List<SlotStatus> availableStatuses = vaccineService.isAvailable(urlDynamic);

	        if (availableStatuses.size() > 0) {

	            logger.info("==================== A L E R T (Start) ====================");
	            logger.info("{} Slots Found in City {}", availableStatuses.size(), availableStatuses.get(0).getCenter().district_name);

	            Boolean specialCenter = Boolean.FALSE;

	            StringBuilder logData = new StringBuilder();

	            for (SlotStatus status : availableStatuses) {
	                String centreInfo = String.format("Date:[%12s], Slots:[%8s], PIN=[%6d], [%30s] || [%20s]",
	                        status.getSession().date,
	                        status.getSession().available_capacity + "/" + status.getSession().available_capacity_dose1,
	                        status.getCenter().pincode,
	                        status.getCenter().name,
	                        status.getCenter().address);

	                result.add(centreInfo);
	                logData.append("\n").append(centreInfo);


	                if (status.getCenter().pincode == 282007 || status.getCenter().pincode == 282004) {
	                    specialCenter = Boolean.TRUE;
	                }
	            }

	            logger.info(logData.toString());
	            logger.info("==================== A L E R T (End) ====================");

	            if (specialCenter) {
	               // soundUtil.setSoundFile(appConfig.getSoundFileMed());
	            } else {
	               // soundUtil.setSoundFile(appConfig.getSoundFileLow());
	            }

	            //soundUtil.playSound();
	        }
	        
	        return result;

	    }
	}
	
}
