package com.example.tapp.common.utils;


public class Route {

    public static final String AUTH = "/auth";

    public static final String USER = "/user";

    public static final String register = USER + "/register";

    public static final String facebook = USER + "/facebook";

    public static final String logout = AUTH + USER + "/logout";

    public static final String verifyMobile = AUTH + USER + "/verify/mobile";

    public static final String resendOtp = AUTH + USER + "/resend/otp";

    public static final String userDetails = AUTH + USER + "/details";

    public static final String addDevice = AUTH + USER + "/device/add";

    public static final String profile = AUTH + USER + "/profile";

    public static final String uploadProfile = profile + "/upload/image";

    public static final String userProfileImage = "/user/profile/image/{fileName:.+}";

    private static final String connections = "/connections";

    public static final String connectionRequest = AUTH + connections + "/request";

    public static final String connectionRequestOperation = connectionRequest + "/operation";

    public static final String connectionRequestList = connectionRequest + "/list";

    public static final String connectionList = AUTH + connections + "/list";

    public static final String removeConnection = AUTH + connections + "/remove";

    public static final String connectionUserNameList = AUTH + connections + USER + "/name/list";

    public static final String blockConnection = AUTH + connections + USER + "/block";

    public static final String unblockConnection = AUTH + connections + USER + "/unblock";

    public static final String file = "/file";

    public static final String report = "/report";

    public static final String reportFile = AUTH + report + file;

    public static final String reportUser = AUTH + report + "/save";

    public static final String reportUpdate = AUTH + report + "/update";

    public static final String reportDetails = AUTH + report + "/details";

    public static final String reportRemove = AUTH + report + "/remove";

    public static final String reportImage = AUTH + report + "/image/{reportId}";

    public static final String reportUserList = AUTH + report + USER + "/list";

    public static final String message = "/message";

    public static final String dialog = "/dialog";

    public static final String createDialog = AUTH + message + dialog + "/create";

    public static final String clearDialog = AUTH + message + dialog + "/clear";

    // Notification
    public static final String notification = "/notification";

    public static final String notificationList = AUTH + notification + "/list";

    public static final String notificationClear = AUTH + notification + "/clear";

    public static final String rating = "/rating";

    public static final String ratingSave = AUTH + rating + "/save";
    public static final String ratingdisplay = AUTH + rating + "/listofRating";
    public static final String about = "/about";
    public static final String aboutListofType = AUTH + about + "/listOfType";
    public static final String aboutdetails = AUTH + report + "/details/{type}";
    public static final String tutorial = "/app-tutorial";
    public static final String shareContent = AUTH + "/share-content";

}
