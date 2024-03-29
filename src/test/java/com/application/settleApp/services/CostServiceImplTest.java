package com.application.settleApp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.application.settleApp.models.BaseEntity;
import com.application.settleApp.models.Cost;
import com.application.settleApp.models.User;
import com.application.settleApp.repositories.CostRepository;
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

public class CostServiceImplTest {

  @Mock private UserService userService;

  @Mock private EventRepository eventRepository;

  @Mock private CostRepository costRepository;

  @InjectMocks private CostServiceImpl costService;

  private Cost cost1 = BaseEntity.getNewWithDefaultDates(Cost.class);
  private Cost cost2 = BaseEntity.getNewWithDefaultDates(Cost.class);
  private Cost cost3 = BaseEntity.getNewWithDefaultDates(Cost.class);
  List<Cost> costList = new ArrayList<>();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    cost1.setId(1L);
    cost2.setId(2L);
    cost3.setId(3L);
    costList.add(cost1);
    costList.add(cost2);
    costList.add(cost3);
  }

  @Test
  void testFindById() {
    cost1.setId(1L);

    when(costRepository.findById(1L)).thenReturn(Optional.of(cost1));

    Cost found = costService.findById(1L);

    assertEquals(1L, found.getId());
    verify(costRepository).findById(1L);
  }

  @Test
  void testFindById_NotFound() {
    when(costRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> costService.findById(1L));
  }

  @Test
  void testSave() {
    Cost cost99 = BaseEntity.getNewWithDefaultDates(Cost.class);
    cost99.setId(99L);

    User user = BaseEntity.getNewWithDefaultDates(User.class);
    when(userService.findById(anyLong())).thenReturn(user);

    when(costRepository.save(cost99)).thenReturn(cost99);

    Cost savedCost = costService.save(cost99);

    verify(costRepository).save(cost99);

    assertNotNull(savedCost);
    assertEquals(99L, savedCost.getId());
  }

  @Test
  void testFindAll() {

    when(costRepository.findAll()).thenReturn(costList);

    List<Cost> retrievedCosts = costService.findAll();

    verify(costRepository).findAll();

    assertNotNull(retrievedCosts);
    assertEquals(3, retrievedCosts.size());
  }

  @Test
  void testDelete() {

    costService.delete(cost1);

    verify(costRepository).delete(cost1);
  }

  @Test
  void testDeleteById() {
    when(costRepository.findById(1L)).thenReturn(Optional.of(cost1));

    costService.deleteById(1L);

    verify(costRepository).delete(cost1);
  }
}
