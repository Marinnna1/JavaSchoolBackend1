package ru.javaschool.JavaSchoolBackend2.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.javaschool.JavaSchoolBackend2.dao.AppointmentsDao;
import ru.javaschool.JavaSchoolBackend2.dao.PatientsDao;
import ru.javaschool.JavaSchoolBackend2.dto.AppointmentDto;
import ru.javaschool.JavaSchoolBackend2.mq.CustomMessage;
import ru.javaschool.JavaSchoolBackend2.mq.MessagePublisher;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AppointmentsServiceTest {

    @Mock
    AppointmentsDao appointmentsDao;

    @Mock
    PatientsDao patientsDao;

    @Mock
    MessagePublisher messagePublisher;

    @Mock
    EventsService eventsService;

    @InjectMocks
    AppointmentsService appointmentsService;


    @Test
    @DisplayName("Test get all with null")
    void getAllNullTest() {
        Integer appointmentNumber = null;
        List actual = appointmentsService.getAll(appointmentNumber);
        List expected = Collections.emptyList();
        verify(appointmentsDao, times(0)).findAll(appointmentNumber);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("Test get all with number")
    void getAllNumberTest() {
        Integer appointmentNumber = 1;
        List actual = appointmentsService.getAll(appointmentNumber);
        verify(appointmentsDao, times(1)).findAll(appointmentNumber);
    }

    @Test
    @DisplayName("Test get appointments count")
    void getAppointmentsCountTest() {
        appointmentsService.getAppointmentsCount();
        verify(appointmentsDao, times(1)).getAppointmentsCount();
    }


    @Test
    @DisplayName("Test edit time pattern with null time pattern")
    void editTimePatternNullTest() {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentsService.editTimePattern(appointmentDto);
        verify(appointmentsDao, times(0)).editTimePattern(appointmentDto.getId(),
                appointmentDto.getTimePattern(), appointmentDto.getPageNumber());
    }


    @Test
    @DisplayName("Test edit time pattern with correct data pattern")
    void editTimePatternCorrectTest() {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setTimePattern("Once a week");
        appointmentsService.editTimePattern(appointmentDto);
        verify(appointmentsDao, times(1)).editTimePattern(appointmentDto.getId(),
                appointmentDto.getTimePattern(), appointmentDto.getPageNumber());
    }



    @Test
    @DisplayName("Test add appointment with null")
    void addAppointmentNullTest() {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentsService.addAppointment(appointmentDto);
        verify(patientsDao, times(1)).findPatientByName(appointmentDto.getPatientName());
    }



    @Test
    @DisplayName("Test delete appointment with null")
    void deleteAppointmentNullTest() {
        Integer appointmentNumber = null;
        appointmentsService.deleteAppointment(appointmentNumber);
        verify(appointmentsDao, times(0)).deleteAppointment(appointmentNumber);
    }

    @Test
    @DisplayName("Test delete appointment with number")
    void deleteAppointmentTest() {
        Integer appointmentNumber = 1;
        appointmentsService.deleteAppointment(appointmentNumber);
        verify(appointmentsDao, times(1)).deleteAppointment(appointmentNumber);
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMessage("update");
        verify(messagePublisher, times(1)).publishMessage(customMessage);
    }


}
