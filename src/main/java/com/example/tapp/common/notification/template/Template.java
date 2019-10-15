package com.example.tapp.common.notification.template;

public class Template {
	

	    public static final String FP_MAIL_SUBJECT = "Forgot Password - GK";
	  
	    public static String getForgotPasswordTemplate() {
	        return "<blockquote><blockquote><blockquote> <blockquote> <p><h1>{0}</h1></p> "
	                + "</blockquote> </blockquote> </blockquote></blockquote><div><b>Hi {1},</b></div> "
	                + "<div> <b> Your password reset link given below.</b></div><div><b>"
	                + " <a href=\"{2}\" title=\"Link: http://\">Click Here.</a></b></div>";
	    }

	  
}
