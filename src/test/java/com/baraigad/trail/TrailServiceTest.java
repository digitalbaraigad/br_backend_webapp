package com.baraigad.trail;

import com.baraigad.fort.entity.Fort;
import com.baraigad.fort.repository.FortRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import static com.baraigad.trail.TrailData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrailServiceTest {
    TrailRepository repository;
    FortRepository forts;
    TrailService service;
    ObjectMapper json = new ObjectMapper();
    @BeforeEach void setup() {
        repository = mock(TrailRepository.class); forts = mock(FortRepository.class);
        service = new TrailService(repository, forts, json);
    }
    TrailData trail(String title) {
        return new TrailData(1L, title, "Synthetic test route", List.of(List.of(73d,18d), List.of(73.01,18d)),
                List.of(new Waypoint("Test gate", "gate", 73,18)), "Test reviewer");
    }
    @Test void rejectsInvalidCoordinatesAndWaypoints() {
        assertThrows(IllegalArgumentException.class, () -> TrailService.validateCoordinate(181, 18));
        assertThrows(IllegalArgumentException.class, () -> TrailService.validateCoordinate(73, Double.NaN));
        var invalid = new TrailData(1L, "X", "", List.of(List.of(73d,18d)), List.of(), "");
        assertThrows(IllegalArgumentException.class, () -> TrailService.validate(invalid));
    }
    @Test void draftsDoNotReplacePublishedBytesAndPublicationIncrementsVersion() throws Exception {
        TrailRoute row = new TrailRoute(); row.setId(1L); row.setRevision(0L); row.setDraftJson(json.writeValueAsString(trail("First")));
        when(repository.findById(1L)).thenReturn(Optional.of(row));
        when(repository.saveAndFlush(any())).thenAnswer(call -> { TrailRoute r = call.getArgument(0); r.setRevision(r.getRevision() + 1); return r; });
        when(forts.findById(1L)).thenReturn(Optional.of(new Fort()));
        assertThrows(IllegalArgumentException.class, () -> service.publish(1L, new PublishRequest(0L, false)));
        assertEquals(1, service.publish(1L, new PublishRequest(0L, true)).version());
        byte[] first = service.download(1L);
        service.save(1L, new SaveRequest(1L, trail("Edited draft")));
        assertArrayEquals(first, service.download(1L));
        assertThrows(IllegalArgumentException.class, () -> service.publish(1L, new PublishRequest(1L, true)));
        assertEquals(2, service.publish(1L, new PublishRequest(2L, true)).version());
        byte[] next = service.download(1L);
        assertNotEquals(new String(first, StandardCharsets.UTF_8), new String(next, StandardCharsets.UTF_8));
        Manifest manifest = service.manifest(row);
        assertEquals(next.length, manifest.sizeBytes());
        assertEquals(HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(next)), manifest.sha256());
        service.unpublish(1L, new PublishRequest(3L, false));
        assertThrows(com.baraigad.common.exception.ResourceNotFoundException.class, () -> service.download(1L));
    }
    @Test void requiresExistingActiveFortAndReviewer() throws Exception {
        when(forts.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.save(null, new SaveRequest(null, trail("New"))));
        var data = trail("X");
        TrailRoute row = new TrailRoute(); row.setId(1L); row.setRevision(0L);
        row.setDraftJson(json.writeValueAsString(new TrailData(1L, "X", "", data.coordinates(), List.of(), "")));
        when(repository.findById(1L)).thenReturn(Optional.of(row));
        assertThrows(IllegalArgumentException.class, () -> service.publish(1L, new PublishRequest(0L,true)));
    }
}
