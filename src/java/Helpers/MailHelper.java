/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import Models.Player;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Adam
 */
public class MailHelper {
    public static final String NEW_TRAINING = "TRÉNING";
    public static final String NEW_BRIGADE = "BRIGÁDA";
    public static final String NEW_INFO = "OZNAM";
    
    public static final String UPDATE_TRAINING = "ÚPRAVA TRÉNINGU";
    public static final String UPDATE_BRIGADE = "ÚPRAVA BRIGÁDY";
    public static final String UPDATE_INFO = "ÚPRAVA OZNAMU";
    
    private static final String FROM_EMAIL = "Grasshoppers.App@gmail.com";
    private static final String USERNAME = "Grasshoppers.App";
    private static final String PWD = "albi29otto8mino";
    
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static final SimpleDateFormat F_DAY_OF_WEEK = new SimpleDateFormat("E");
    private static final SimpleDateFormat F_DATE = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat F_TIME = new SimpleDateFormat("HH:mm");
    
    public static void sendEmail(List<String> toEmailList, String subject, String message) {
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            //props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.fallback", "false");
            
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(true);
            
            Message mailMessage = new MimeMessage(mailSession);
            
            mailMessage.setFrom(new InternetAddress(FROM_EMAIL));
            if (toEmailList != null) {
                for (String toEmail : toEmailList) {
                    mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                }
            }
            message += "\n\n Pre viac info navštívte aplikáciu Grasshoppers";
            mailMessage.setText(message);
            mailMessage.setSubject(subject);
            
            Transport transport = mailSession.getTransport("smtp");
            transport.connect("smtp.gmail.com", USERNAME, PWD);
            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
            
        } catch (MessagingException ex) {
            Logger.getLogger(MailHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String buildMessage(String startDateTimeString, String endDateTimeString, String locationName) {
        String message = "";
        try {
            
            Date startDateTime = FORMATTER.parse(startDateTimeString);
            Date endDateTime = FORMATTER.parse(endDateTimeString);
            String dayOfWeek = MailHelper.getSlovakDayOfWeek(F_DAY_OF_WEEK.format(startDateTime));
            String date = F_DATE.format(startDateTime);
            String from = F_TIME.format(startDateTime);
            String to = F_TIME.format(endDateTime);
            message = dayOfWeek + " (" + date + ") \n" + from + " - " + to + "\n" + locationName;
        } catch (ParseException ex) {
            Logger.getLogger(MailHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }

    public static String buildInfoMessage(Player creator, String content) {
        String message = "";
        String creatorName = creator.getFirstname() + " " + creator.getNick() + " " + creator.getSurname();
        message = "vytvoril: " + creatorName + "\n\n" + content;
        return message;
    }

    private static String getSlovakDayOfWeek(String format) {
        switch (format) {
            case "Po":
                return "Pondelok";
            case "Ut":
                return "Utorok";
            case "St":
                return "Streda";
            case "Št":
                return "Štvrtok";
            case "Pi":
                return "Piatok";
            case "So":
                return "Sobota";
            case "Ne":
                return "Nedeľa";
            default: 
                return "";
        }
    }
}
