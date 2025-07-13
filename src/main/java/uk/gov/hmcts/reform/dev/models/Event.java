package uk.gov.hmcts.reform.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_generator")
    @SequenceGenerator(name = "events_generator", sequenceName = "events_seq", allocationSize = 1)
    private Long id;
    private String title;

    @Column(name = "event_date")
    private LocalDateTime date;

    public Event(String title) {
        this.title = title;
        this.date = LocalDateTime.now();
    }
}
