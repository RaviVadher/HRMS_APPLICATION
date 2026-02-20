package com.roima.hrms.mail;

import com.roima.hrms.gamescheduling.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmailTemplate {

    public static String travelAssigned(String name, String travelTitle, LocalDate start, LocalDate end)
    {
       return """
               Hello %s,
               
               You have been assigned for the travel.
               
               Details:
               Travel:%s
               Start Date:%s
               End Date:%s
               
               please login into HRMS portal for more details.
               
               Regards,
               HR Team
               """.formatted(name,travelTitle,start,end);
    }

    public static String expenseSubmission(String reciever,String travelTitle,String sender )
    {
        return """
               Hello %s,
               
               %s has submit expense for %s travel.
               
               please login into HRMS portal and take proper action for that.
               
               Regards,
               HRMS Team
               """.formatted(reciever,sender,travelTitle);
    }

    public static String sharedJob(String jobTitle,String userName )
    {
        return """
               Hello,
               
               In our company we have job opening for %s.
               
               please find the attached Job description for more details.It is great opportunity
               for you.
               
               Regards,
               %s
               """.formatted(jobTitle,userName);
    }

    public static String referJob(Long jobId,String jobTitle,String referName,String friendName,String email )
    {
        return """
               Hello,
               
               JOBID: %s
               JOB Title: %s
               
               I %s Refer %s for new Job opening.Please find the attached CV for more details.
               Refered Mail:%s
               
               Best Regards,
               HRMS
               """.formatted(jobId,jobTitle,referName,friendName,email);
    }

    public static String gameScheduling( String name,String bookedUserName, LocalTime start, LocalTime end, String game, BookingStatus status)
    {
        return """
               Hello %s,
               
               Your friend %s booked slot for %s game.
               Booking Information:
               Status:%s
               Slot Start Time:%s
               Slot End Time:%s
               
               please login into HRMS portal for more details.
               
               Regards,
               HRMS
               """.formatted(name,bookedUserName,game,status,start,end);
    }
}
