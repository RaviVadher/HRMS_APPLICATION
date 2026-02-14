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
}
