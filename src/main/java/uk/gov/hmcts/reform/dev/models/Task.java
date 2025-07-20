package uk.gov.hmcts.reform.dev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASKS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasks_generator")
    @SequenceGenerator(name = "tasks_generator", sequenceName = "tasks_seq", allocationSize = 1)
    private Long id;

    private String title;
    private String description;

    private String status;

    @Column(name = "due_date")
    private LocalDateTime due_date;
}
