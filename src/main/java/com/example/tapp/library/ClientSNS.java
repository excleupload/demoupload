package com.example.tapp.library;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeletePlatformApplicationRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.example.tapp.common.utils.AppUtils;

public class ClientSNS {

    private static final Logger log = LoggerFactory.getLogger(ClientSNS.class);

    private static final String PLATFORM_PRINCIPAL = "PlatformPrincipal";
    private static final String PLATFORM_CREDENTIAL = "PlatformCredential";
    private static final String MESSAGE_STRUCTURE = "json";

    private AmazonSNS amazonSNS;

    public ClientSNS(AmazonSNS amazonSNS) {
        this.amazonSNS = amazonSNS;

    }

    protected CreatePlatformApplicationResult createPlatformApplication(String applicationName, Platform platform,
            String principal, String credential) {
        CreatePlatformApplicationRequest platformApplicationRequest = new CreatePlatformApplicationRequest();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(PLATFORM_PRINCIPAL, principal);
        attributes.put(PLATFORM_CREDENTIAL, credential);
        platformApplicationRequest.setAttributes(attributes);
        platformApplicationRequest.setName(applicationName);
        platformApplicationRequest.setPlatform(platform.name());
        return amazonSNS.createPlatformApplication(platformApplicationRequest);
    }

    protected CreatePlatformEndpointResult createPlatformEndpoint(Platform platform, String customData,
            String platformToken, String applicationArn) {
        CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
        platformEndpointRequest.setCustomUserData(customData);
        String token = platformToken;
        platformEndpointRequest.setToken(token);
        platformEndpointRequest.setPlatformApplicationArn(applicationArn);
        return amazonSNS.createPlatformEndpoint(platformEndpointRequest);
    }


    protected void deletePlatformApplication(String applicationArn) {
        DeletePlatformApplicationRequest request = new DeletePlatformApplicationRequest();
        request.setPlatformApplicationArn(applicationArn);
        amazonSNS.deletePlatformApplication(request);
    }

    protected PublishResult publish(String endpointArn, Platform platform, HashMap<String, Object> messageMap) {
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setMessageStructure(MESSAGE_STRUCTURE);

        String notification = AppUtils.jsonStringify(messageMap);
        // ************* Remove
        log.info("{Message Body: " + notification + "}");
        // ****************

        Map<String, String> finalMessage = new HashMap<>();
        finalMessage.put(platform.name(), notification);
        notification = AppUtils.jsonStringify(finalMessage);
        publishRequest.setTargetArn(endpointArn);

        // ************* Remove
        log.info("{Message Body: " + notification + "}");
        // ****************
        publishRequest.setMessage(notification);
        return amazonSNS.publish(publishRequest);
    }

}
