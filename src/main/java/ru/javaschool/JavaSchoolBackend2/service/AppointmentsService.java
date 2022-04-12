package ru.javaschool.JavaSchoolBackend2.service;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.javaschool.JavaSchoolBackend2.dao.AppointmentsDao;
import ru.javaschool.JavaSchoolBackend2.dao.PatientsDao;
import ru.javaschool.JavaSchoolBackend2.dao.TreatmentsDao;
import ru.javaschool.JavaSchoolBackend2.dto.AppointmentDto;
import ru.javaschool.JavaSchoolBackend2.dto.MessageDto;
import ru.javaschool.JavaSchoolBackend2.email.EmailMessage;
import ru.javaschool.JavaSchoolBackend2.entity.Appointment;
import ru.javaschool.JavaSchoolBackend2.mq.CustomMessage;
import ru.javaschool.JavaSchoolBackend2.mq.MessagePublisher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentsService {

    private final AppointmentsDao appointmentsDao;

    private final PatientsDao patientsDao;

    private final TreatmentsDao treatmentsDao;

    private final EventsService eventsService;

    private final EmailService emailService;

    private final MessagePublisher messagePublisher;

    private static final Logger LOGGER = LogManager.getLogger(AppointmentsService.class);


    public List<AppointmentDto> getAll(Integer pageNumber) {
        if(pageNumber != null) {
            List<Appointment> appointments = appointmentsDao.findAll(pageNumber);
            List<AppointmentDto> appointmentDtos = new ArrayList<>();
            AppointmentDto currentAppointmentDto = new AppointmentDto();
            int iteration = 0;
            for (Appointment appointment: appointments) {
                currentAppointmentDto.setId(iteration);
                currentAppointmentDto.setPatientName(appointment.getPatient().getName());
                currentAppointmentDto.setTreatmentName(appointment.getTreatment().getName());
                currentAppointmentDto.setTreatmentType(appointment.getTreatment().getType());
                currentAppointmentDto.setTimePattern(appointment.getTimePattern());
                currentAppointmentDto.setStartDate(appointment.getStartDate());
                currentAppointmentDto.setEndDate(appointment.getEndDate());
                currentAppointmentDto.setDose(String.valueOf(appointment.getDose()));
                appointmentDtos.add(currentAppointmentDto);
                currentAppointmentDto = new AppointmentDto();
                iteration++;
            }
            return appointmentDtos;
        }
        return Collections.emptyList();
    }


    public Long getAppointmentsCount() {
        return appointmentsDao.getAppointmentsCount();
    }

    public MessageDto editTimePattern(AppointmentDto appointmentDto) {
        if(appointmentDto != null && appointmentDto.getTimePattern() != null) {
            if (appointmentDto.getTimePattern().equals("Once a day") || appointmentDto.getTimePattern().equals("Twice a day")
                    || appointmentDto.getTimePattern().equals("Once a week")) {
                Appointment appointment = appointmentsDao.editTimePattern(appointmentDto.getId(), appointmentDto.getTimePattern(), appointmentDto.getPageNumber());
                eventsService.editEvents(appointment);
                emailService.sendMessage(appointment.getPatient().getEmail(), "edit appointment",
                        new EmailMessage().getMessageForEditAppointment(appointment));
                return new MessageDto("Time pattern has been successfully changed");
            }
        }
        return new MessageDto("Invalid time pattern");
    }


    public MessageDto editDose(AppointmentDto appointmentDto) {
        double dose;
        try{
            dose = Double.parseDouble(appointmentDto.getDose());
        } catch (Exception e) {
            return new MessageDto("Invalid dose");
        }
        if(appointmentsDao.findAll(appointmentDto.getPageNumber()).get(appointmentDto.getId()).getTreatment().getType().equals("procedure")) {
            return new MessageDto("You can't initialize dose in procedure");
        }
        if(dose < 1 || dose > 1000) {
            return new MessageDto("Dose must be a positive number and less than 1000");
        }
        Appointment appointment = appointmentsDao.editDose(appointmentDto.getId(), dose, appointmentDto.getPageNumber());
        emailService.sendMessage(appointment.getPatient().getEmail(), "edit appointment",
                new EmailMessage().getMessageForEditAppointment(appointment));
        return new MessageDto("Dose has been successfully changed");
    }


    public MessageDto editPeriod(AppointmentDto appointmentDto) {
        if(appointmentDto.getStartDate() == null || appointmentDto.getEndDate() == null) {
            return new MessageDto("Invalid input");
        }
        Date date = new Date();
        if((appointmentDto.getStartDate().before(appointmentDto.getEndDate()) || appointmentDto.getStartDate().equals(appointmentDto.getEndDate()))
                && date.before(appointmentDto.getStartDate())) {
            Appointment appointment = appointmentsDao.editPeriod(appointmentDto.getId(), appointmentDto.getStartDate(),
                    appointmentDto.getEndDate(), appointmentDto.getPageNumber());
            eventsService.editEvents(appointment);
            emailService.sendMessage(appointment.getPatient().getEmail(), "edit appointment",
                    new EmailMessage().getMessageForEditAppointment(appointment));
            return new MessageDto("Period has been successfully changed");
        }
        return new MessageDto("Invalid input");
    }





    public MessageDto addAppointment(AppointmentDto appointmentDto) {
        if(patientsDao.findPatientByName(appointmentDto.getPatientName()) == null) {
            return new MessageDto("Invalid patient name");
        }
        Double dose;
        try {
            dose = Double.parseDouble(appointmentDto.getDose());
        } catch(Exception e) {
            return new MessageDto("Invalid dose");
        }
        if(dose < 1 || dose > 1000) {
            return new MessageDto("Dose must be positive and less than 1000");
        }
        if(treatmentsDao.findTreatmentByName(appointmentDto.getTreatmentName()) == null) {
            return new MessageDto("Invalid treatment");
        }
        if(treatmentsDao.findTreatmentByName(appointmentDto.getTreatmentName()).getType().equals("procedure")) {
            dose = null;
        }

        Date date = new Date();
        if(!((appointmentDto.getStartDate().before(appointmentDto.getEndDate()) || appointmentDto.getStartDate().equals(appointmentDto.getEndDate()))
                && date.before(appointmentDto.getStartDate()))) {
            return new MessageDto("Invalid period");
        }

        Appointment appointment = appointmentsDao.addAppointment(appointmentDto.getPatientName(), dose,
                appointmentDto.getStartDate(), appointmentDto.getEndDate(), appointmentDto.getTimePattern(), appointmentDto.getTreatmentName());

        eventsService.addEvents(appointment);

        LOGGER.info("add appointment");
        emailService.sendMessage(appointment.getPatient().getEmail(), "add appointment",
                new EmailMessage().getMessageForAppointmentCreation(appointment));
        return new MessageDto("Add new appointment");
    }




    public void deleteAppointment(Integer appointmentNumber) {
        if (appointmentNumber != null) {
            if(appointmentNumber >= 0) {
                appointmentsDao.deleteAppointment(appointmentNumber);
                CustomMessage customMessage = new CustomMessage();
                customMessage.setMessage("update");
                messagePublisher.publishMessage(customMessage);

            }
        }
    }
}