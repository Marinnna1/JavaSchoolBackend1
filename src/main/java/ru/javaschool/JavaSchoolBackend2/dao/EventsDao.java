package ru.javaschool.JavaSchoolBackend2.dao;


import org.springframework.stereotype.Repository;
import ru.javaschool.JavaSchoolBackend2.dto.EventDto;
import ru.javaschool.JavaSchoolBackend2.dto.MessageDto;
import ru.javaschool.JavaSchoolBackend2.entity.Appointment;
import ru.javaschool.JavaSchoolBackend2.entity.Event;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Repository
public class EventsDao {


    @PersistenceContext
    private EntityManager entityManager;



    public List<Event> findAll(Integer pageNumber, String status, String filter) {
        int pageSize = 3;
        int currentPage = pageNumber;
            Query query;
            if(filter.equals("By patients")) {
                query = entityManager.createQuery("From Event Where status = \'" + status + "\' ORDER BY appointment.patient.name");
            }
            else if(filter.equals("Today")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                query = entityManager.createQuery("From Event Where status = \'" + status + "\' And cast(date as date) = \'" + dateFormat.format(date) + "\'");
            }
            else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar c = Calendar.getInstance();
                Date date = new Date();
                c.setTime(date);
                c.add(Calendar.HOUR_OF_DAY, 1);
                Date dateAfterHour = c.getTime();
                query = entityManager.createQuery("From Event Where status = \'" + status + "\' And date Between \'" + dateFormat.format(date) + "\' And \'" + dateFormat.format(dateAfterHour) + "\'");
            }
            query.setFirstResult(((currentPage - 1) * pageSize));
            query.setMaxResults(pageSize);
            return (List<Event>) query.getResultList();

    }

    public Long getEventsCount(String status, String filter) {
            Long recordsCount;
            if(filter.equals("By patients")) {
                recordsCount = (Long) entityManager
                        .createQuery("SELECT COUNT(*) FROM Event WHERE status=\'" + status + "\'")
                        .getSingleResult();

            }
            else if(filter.equals("Today")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                recordsCount = (Long) entityManager
                        .createQuery("SELECT COUNT(*) From Event Where status = \'" + status + "\' And cast(date as date) = \'" + dateFormat.format(date) + "\'")
                        .getSingleResult();
            }
            else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar c = Calendar.getInstance();
                Date date = new Date();
                c.setTime(date);
                c.add(Calendar.HOUR_OF_DAY, 1);
                Date dateAfterHour = c.getTime();
                recordsCount = (Long) entityManager
                        .createQuery("SELECT COUNT(*) From Event Where status = \'" + status + "\' And date Between \'" + dateFormat.format(date) + "\' And \'" + dateFormat.format(dateAfterHour) + "\'")
                        .getSingleResult();
            }
            return recordsCount;

    }

    public MessageDto changeStatus(EventDto eventDto) {
        Event event = findAll(eventDto.getPageNumber(), eventDto.getOldStatus(), eventDto.getFilter()).get(eventDto.getId());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if(dateFormat.format(event.getDate()).equals(dateFormat.format(date))) {
            if(eventDto.getStatus().equals("Cancelled")) {
                if(eventDto.getReason() == null) {
                    return new MessageDto("Reason can't be null");
                }
                if(eventDto.getReason().trim().equals("")) {
                     return new MessageDto("Reason can't be empty");
                }
                event.setReason(eventDto.getReason());
            }
            event.setStatus(eventDto.getStatus());
            entityManager.merge(event);
            return new MessageDto("Status successfully changed");
        }
        return new MessageDto("Can't change status");
    }


    public List<Event> findTodayEvents() {
        return entityManager.createQuery("FROM Event Where DATE(date) = CURDATE()", Event.class).getResultList();
    }




    public void deleteEvents(Appointment appointment) {
        List<Event> events = entityManager.createQuery("FROM Event where appointment.id = " + appointment.getId(), Event.class).getResultList();
        for(Event event: events) {
            entityManager.remove(event);
        }

    }


    void saveEvent(Event event) {
        entityManager.persist(event);
        entityManager.flush();
    }



    public void generateEvents(Appointment appointment) {

        Date currentDate = appointment.getStartDate();
        if(currentDate.before(new Date())) {
            currentDate = new Date();
        }

        Calendar calendar;
        switch (appointment.getTimePattern()) {
            case "Once a day":
                calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                currentDate = calendar.getTime();
                while(currentDate.before(appointment.getEndDate())) {
                    Event event = new Event();
                    event.setAppointment(appointment);
                    event.setStatus("Planned");
                    currentDate.setMinutes(0);
                    currentDate.setSeconds(0);
                    event.setDate(currentDate);
                    saveEvent(event);
                    calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.DATE, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    currentDate = calendar.getTime();
                }
            case "Twice a day":
                calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.set(Calendar.HOUR_OF_DAY, 8);
                currentDate = calendar.getTime();
                while(currentDate.before(appointment.getEndDate())) {
                    Event event = new Event();
                    event.setAppointment(appointment);
                    event.setStatus("Planned");
                    currentDate.setMinutes(0);
                    currentDate.setSeconds(0);
                    event.setDate(currentDate);
                    saveEvent(event);
                    calendar.set(Calendar.HOUR_OF_DAY, 20);
                    currentDate = calendar.getTime();
                    event = new Event();
                    event.setAppointment(appointment);
                    event.setStatus("Planned");
                    event.setDate(currentDate);
                    saveEvent(event);
                    calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.DATE, 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    currentDate = calendar.getTime();
                }


            case "Once a week":
                calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.set(Calendar.HOUR_OF_DAY, 20);
                currentDate = calendar.getTime();
                while(currentDate.before(appointment.getEndDate())) {
                    Event event = new Event();
                    event.setAppointment(appointment);
                    event.setStatus("Planned");
                    currentDate.setMinutes(0);
                    currentDate.setSeconds(0);
                    event.setDate(currentDate);
                    saveEvent(event);
                    calendar = Calendar.getInstance();
                    calendar.setTime(currentDate);
                    calendar.add(Calendar.DATE, 7);
                    calendar.set(Calendar.HOUR_OF_DAY, 20);
                    currentDate = calendar.getTime();
                }
        }
    }
}
