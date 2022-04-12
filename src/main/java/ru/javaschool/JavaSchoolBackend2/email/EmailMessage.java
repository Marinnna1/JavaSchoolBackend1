package ru.javaschool.JavaSchoolBackend2.email;

import ru.javaschool.JavaSchoolBackend2.entity.Appointment;
import ru.javaschool.JavaSchoolBackend2.entity.Event;

public class EmailMessage {

    public String getMessageForAppointmentCreation(Appointment appointment) {
        return "Dear " + appointment.getPatient().getName() + ",\n" +
                "We've created appointment for you.\n"  + getAppointmentInfo(appointment);
    }


    private String getAppointmentInfo(Appointment appointment) {
        return "Treatment: " + appointment.getTreatment().getName() + ",\n" +
                "Dose: " + (appointment.getTreatment().getType().equals("procedure") ? "-" : appointment.getDose()) + ",\n" +
                "Time Pattern: " + appointment.getTimePattern() + ",\n" +
                "Period: " + appointment.getStartDate() + " - " + appointment.getEndDate() + ".";
    }


    public String getMessageForEditAppointment(Appointment appointment) {
        return "Dear " + appointment.getPatient().getName() + ",\n" +
                "We've changed your appointment" + ",\n" + getAppointmentInfo(appointment);
    }


    public String getMessageForTodayEvent(Event event) {
        return "Dear " + event.getAppointment().getPatient().getName() + ",\n" +
                "We'd like to remind you about today's event.\n" +
                "Date and time: " + event.getDate() + "\n\n" +
                "Appointment for this event: \n" +
                getAppointmentInfo(event.getAppointment());

    }
}
