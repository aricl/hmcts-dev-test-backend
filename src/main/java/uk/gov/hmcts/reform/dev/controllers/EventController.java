package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dev.EventRepository;
import uk.gov.hmcts.reform.dev.models.Event;

@RestController
public class EventController {
    @Autowired
    private EventRepository eventRepository;

    // insert a product into database
    @GetMapping("/event")
    public ResponseEntity<Event> saveProduct() {
        Event event = new Event("test title");
        Event savedEvent = eventRepository.save(event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }
}
