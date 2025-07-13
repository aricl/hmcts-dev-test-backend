package uk.gov.hmcts.reform.dev;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.gov.hmcts.reform.dev.models.Event;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM EVENTS WHERE id = ?1", nativeQuery = true)
    List<Event> getEventsByName(String title);
}
