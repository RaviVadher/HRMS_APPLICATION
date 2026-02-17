package com.roima.hrms.mail;

import java.time.LocalDate;

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
}
