package com.baraigad.trail;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrekServiceTest {
    TrekService.Trek trek() {
        long now = System.currentTimeMillis() - 10000;
        return new TrekService.Trek(UUID.randomUUID().toString(), 1, 1, "Synthetic test walk", Instant.ofEpochMilli(now).toString(),
                Instant.ofEpochMilli(now + 5000).toString(), List.of(new TrekService.Point(73,18,10,now + 1000)));
    }
    @Test void retriesReturnExistingReceiptWithinAccount() {
        var repository = mock(TrekRepository.class);
        var service = new TrekService(repository, new ObjectMapper());
        var trek = trek(); var existing = new TrekHistory(); existing.setId(7L);
        when(repository.findByOwnerEmailAndClientId("a@example.com", trek.clientId())).thenReturn(Optional.of(existing));
        assertEquals(7L, service.sync("a@example.com", trek));
        verify(repository, never()).saveAndFlush(any());
        verify(repository).findByOwnerEmailAndClientId("a@example.com", trek.clientId());
    }
    @Test void historyIsScopedToAuthenticatedOwner() {
        var repository = mock(TrekRepository.class); var service = new TrekService(repository, new ObjectMapper());
        when(repository.findByOwnerEmailOrderByIdDesc(eq("a@example.com"), any())).thenReturn(new PageImpl<>(List.of()));
        assertTrue(service.history("a@example.com", 0).isEmpty());
        verify(repository).findByOwnerEmailOrderByIdDesc(eq("a@example.com"), any());
    }
    @Test void rejectsMissingPointsAndBadDates() {
        var valid = trek(); TrekService.validate(valid);
        assertThrows(IllegalArgumentException.class, () -> TrekService.validate(new TrekService.Trek(valid.clientId(),1,1,"X",valid.startedAt(), valid.endedAt(),List.of())));
        assertThrows(IllegalArgumentException.class, () -> TrekService.validate(new TrekService.Trek("invalid",1,1,"X",valid.startedAt(),valid.endedAt(),valid.points())));
        assertThrows(IllegalArgumentException.class, () -> TrekService.validate(new TrekService.Trek(valid.clientId(),1,1,"X",valid.endedAt(),valid.startedAt(),valid.points())));
    }
}
