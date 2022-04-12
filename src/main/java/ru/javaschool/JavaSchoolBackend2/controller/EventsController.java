package ru.javaschool.JavaSchoolBackend2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.javaschool.JavaSchoolBackend2.dto.EventDto;
import ru.javaschool.JavaSchoolBackend2.dto.MessageDto;
import ru.javaschool.JavaSchoolBackend2.service.EventsService;
import java.util.List;



@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("events/")
@RequiredArgsConstructor
public class EventsController {

    private final EventsService eventsService;

    @PostMapping("all")
    public List<EventDto> getData(@RequestBody EventDto eventDto) {
        return eventsService.getAll(eventDto.getPageNumber(), eventDto.getStatus(), eventDto.getFilter());
    }

    @PostMapping("events_count")
    public Long getEventsCount(@RequestBody EventDto eventDto) {
        return eventsService.getEventsCount(eventDto);
    }

    @PostMapping("edit_status")
    public MessageDto editStatus(@RequestBody EventDto eventDto) {
        return eventsService.editStatus(eventDto);
    }


}
