package com.application.settleApp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.application.settleApp.models.BaseEntity;
import com.application.settleApp.models.Cost;
import com.application.settleApp.models.Event;
import com.application.settleApp.models.User;
import com.application.settleApp.repositories.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventServiceImplTest {

  @Mock private EventRepository eventRepository;
  @Mock private UserService userService;
  @Mock private CostServiceImpl costService;

  @InjectMocks private EventServiceImpl eventService;

  private Event event1 = BaseEntity.getNewWithDefaultDates(Event.class);
  private Event event2 = BaseEntity.getNewWithDefaultDates(Event.class);
  private Event event3 = BaseEntity.getNewWithDefaultDates(Event.class);
  List<Event> eventList = new ArrayList<>();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    event1.setId(1L);
    event2.setId(2L);
    event3.setId(3L);
    eventList.add(event1);
    eventList.add(event2);
    eventList.add(event3);
  }

  @Test
  void testFindById() {
    event1.setId(1L);

    when(eventRepository.findById(1L)).thenReturn(Optional.of(event1));

    Event found = eventService.findById(1L);

    assertEquals(1L, found.getId());
    verify(eventRepository).findById(1L);
  }

  @Test
  void testFindById_NotFound() {
    when(eventRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> eventService.findById(1L));
  }

  @Test
  void testSave() {
    Event event99 = BaseEntity.getNewWithDefaultDates(Event.class);
    event99.setId(99L);

    User user = BaseEntity.getNewWithDefaultDates(User.class);
    when(userService.findById(anyLong())).thenReturn(user);
    when(costService.findById(anyLong())).thenReturn(BaseEntity.getNewWithDefaultDates(Cost.class));

    when(eventRepository.save(event99)).thenReturn(event99);

    Event savedEvent = eventService.save(event99, null, null);

    verify(eventRepository).save(event99);

    assertNotNull(savedEvent);
    assertEquals(99L, savedEvent.getId());
  }

  @Test
  void testFindAll() {

    when(eventRepository.findAll()).thenReturn(eventList);

    List<Event> retrievedEvents = eventService.findAll();

    verify(eventRepository).findAll();

    assertNotNull(retrievedEvents);
    assertEquals(3, retrievedEvents.size());
  }

  @Test
  void testDelete() {

    eventService.delete(event1);

    verify(eventRepository).delete(event1);
  }

  @Test
  void testDeleteById() {
    when(eventRepository.findById(1L)).thenReturn(Optional.of(event1));

    eventService.deleteById(1L);

    verify(eventRepository).delete(event1);
  }
}
