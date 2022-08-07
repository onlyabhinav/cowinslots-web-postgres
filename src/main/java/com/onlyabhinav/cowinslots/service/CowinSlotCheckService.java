package com.onlyabhinav.cowinslots.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyabhinav.cowinslots.configs.AppConfig;
import com.onlyabhinav.cowinslots.entity.EntityCowinSlots;
import com.onlyabhinav.cowinslots.models.Center;
import com.onlyabhinav.cowinslots.models.Root;
import com.onlyabhinav.cowinslots.models.Session;
import com.onlyabhinav.cowinslots.models.SlotStatus;
import com.onlyabhinav.cowinslots.repository.CowinSlotRepository;
import com.onlyabhinav.cowinslots.utils.HTTPHelper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CowinSlotCheckService {

    private static Logger logger = LoggerFactory.getLogger(CowinSlotCheckService.class);

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private List<SlotStatus> slotStatus;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private HTTPHelper httpHelper;
    
    @Autowired
    CowinSlotRepository repository;


    public List<SlotStatus> isAvailable(String urlStr) {

        slotStatus = new ArrayList<SlotStatus>();


        try {


            String result = httpHelper.openHttpURL(urlStr);

            if (result == null) {
                throw new RuntimeException("UNABLE TO OPEN CONNECTION");
            }

            Root root = objectMapper.readValue(result, Root.class);

            logger.info("Number of Centres: {}", root.centers.size());


            for (int i = 0; i < root.centers.size(); i++) {
                List<Session> sessions = root.centers.get(i).getSessions();

                Center center = root.centers.get(i);


                for (int j = 0; j < sessions.size(); j++) {
                    Session session = sessions.get(j);
                    if (session.min_age_limit == 18) {
                        //                      if(session.min_age_limit == 18 ){
                        logger.info("PIN: {} vaccine: {} fee: {}, dose2: {}, all_slots: {}, isValid: {} centreName: {}",
                                center.pincode, session.vaccine, center.fee_type,
                                session.available_capacity_dose1,
                                session.available_capacity,
                                isValidCentre(center, session), center.name);


                        EntityCowinSlots cowinData = new EntityCowinSlots();
                        
                        cowinData.setPincode(center.pincode);
                        cowinData.setCentreName((center.name));
                        cowinData.setVaccine(session.vaccine);
                        cowinData.setValid(true);
                        cowinData.setDose2(session.available_capacity_dose1);
                        
                        repository.save(cowinData);
                        
                        if (isValidCentre(center, session)) {

                            if (session.available_capacity_dose1 >= appConfig.getMinAvailability() &&
                                    center.fee_type.equalsIgnoreCase("free")) {
                                SlotStatus availableSlot = new SlotStatus();

                                availableSlot.setIsAvailable(true);//(Boolean.TRUE);
                                availableSlot.setRoot(root);
                                availableSlot.setCenter(center);
                                availableSlot.setSession(session);
                                
                                logger.info("---------------------------------");
                                logger.info("AVAILABLE slot :: {}", session.available_capacity_dose2);
                                logger.info("---------------------------------");

                                
                              //  availableSlot.

                                slotStatus.add(availableSlot);
                            } else {
                                logger.info("---------------------------------");
                                logger.info("SKIPPING as availabilty is less {}", session.available_capacity_dose2);
                                logger.info("---------------------------------");
                            }


                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error", e);
        }
        return slotStatus;
    }

    private Boolean isValidCentre(Center center, Session session) {
        Boolean res = Integer.toString(center.pincode).startsWith("208") 
				|| Integer.toString(center.pincode).startsWith("28200")
				|| Integer.toString(center.pincode).startsWith("411")
				|| Integer.toString(center.pincode).startsWith("458")
				|| Integer.toString(center.pincode).startsWith("452");

        if (res) {
            //res = session.vaccine.equalsIgnoreCase("COVAXIN");
            res = session.vaccine.equalsIgnoreCase("COVISHIELD");
        }

        if (session.available_capacity_dose1 < 1)
            res = Boolean.FALSE;

        //logger.info("PIN: {} available_capacity_dose1:{}, isValidCentre = {}", center.pincode, session.available_capacity_dose1, res);
        return res;
    }

    public void notifyOnMac() throws IOException {
        Runtime.getRuntime().exec(String.format("terminal-notifier -message Vaccine_Available"));
    }
}
