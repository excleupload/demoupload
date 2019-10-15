package com.example.tapp.library;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.PublishResult;
import com.example.tapp.common.dto.NotificationDto;
import com.example.tapp.common.dto.Push;

public class PushNotification extends ClientSNS implements Notification {

    public PushNotification(AmazonSNS amazonSNS) {
        super(amazonSNS);
    }

    @Override
    public String android(String token, Push push) {
        return this.notification(Platform.GCM, token, push, 0);
    }

    @Override
    public String apple(String token, Push push) {
        return this.notification(Platform.APNS_SANDBOX, token, push, 0);
    }

    private String notification(Platform platform, final String deviceToken, Push push, int badge) {
        String platformArn = null;
        HashMap<String, Object> message = null;
        if (platform.name().equals(Platform.APNS_SANDBOX.name())) {
            platformArn = Arn.IOS.getAction();
            message = this.generateMessageApple(push);
        } else if (platform.name().equals(Platform.GCM.name())) {
            platformArn = Arn.ANDROID.getAction();
            message = this.generateMessageAndroid(push);
        }
        String endpointArn = this.createEndpointArn(platform, platformArn, deviceToken);
        PublishResult publishResult = publish(endpointArn, platform, message);
        return publishResult.getMessageId();
    }

    private String createEndpointArn(Platform platform, String platformArn, String platformToken) {
        CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(platform, "customData",
                platformToken, platformArn);
        return platformEndpointResult.getEndpointArn();
    }

    private HashMap<String, Object> generateMessageAndroid(Push push) {
        HashMap<String, Object> message = new HashMap<>();
        message.put("message", push);
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("collapse_key", "Welcome");
        messageMap.put("data", message);
        messageMap.put("notification", message);
        messageMap.put("delay_while_idle", true);
        messageMap.put("time_to_live", 125);
        messageMap.put("dry_run", false);
        return messageMap;
    }

    private HashMap<String, Object> generateMessageApple(Push push) {
        HashMap<String, Object> appleMessageMap = new HashMap<>();
        Map<String, Object> appMessageMap = new HashMap<String, Object>();
        appMessageMap.put("alert", push.getMessage());
        appMessageMap.put("badge", 0);
        appMessageMap.put("sound", "default");
        appMessageMap.put("data", push);
        appleMessageMap.put("aps", appMessageMap);
        return appleMessageMap;
    }

    // ================ Push Notification=========

    @Override
    public String adminIOS(String token, NotificationDto push) {
        return this.adminNotification(Platform.APNS_SANDBOX, token, push, 0);
    }

    private String adminNotification(Platform platform, final String deviceToken, NotificationDto push, int badge) {
        String platformArn = null;
        HashMap<String, Object> message = null;
        if (platform.name().equals(Platform.APNS_SANDBOX.name())) {
            platformArn = Arn.IOS.getAction();
            message = this.generateAdminIOS(push);
        }
        String endpointArn = this.createEndpointArn(platform, platformArn, deviceToken);
        PublishResult publishResult = publish(endpointArn, platform, message);
        return publishResult.getMessageId();
    }

    private HashMap<String, Object> generateAdminIOS(NotificationDto push) {
        HashMap<String, Object> appleMessageMap = new HashMap<>();
        Map<String, Object> appMessageMap = new HashMap<String, Object>();
        appMessageMap.put("alert", push.getBody());
        appMessageMap.put("badge", 0);
        appMessageMap.put("sound", "default");
        appMessageMap.put("data", push.getSubject());
        appleMessageMap.put("aps", appMessageMap);
        return appleMessageMap;
    }
}