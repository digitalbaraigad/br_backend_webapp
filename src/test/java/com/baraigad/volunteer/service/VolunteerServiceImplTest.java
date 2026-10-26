package com.baraigad.volunteer.service;

import com.baraigad.common.exception.DuplicateResourceException;
import com.baraigad.notification.service.RegistrationNotificationService;
import com.baraigad.volunteer.dto.VolunteerDto;
import com.baraigad.volunteer.entity.Volunteer;
import com.baraigad.volunteer.repository.VolunteerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceImplTest {

    @Mock private VolunteerRepository volunteerRepository;
    @Mock private RegistrationNotificationService registrationNotificationService;
    @InjectMocks private VolunteerServiceImpl volunteerService;

    @Test
    void restoresSoftDeletedVolunteerWithTheSameMobileNumber() {
        Volunteer deletedVolunteer = new Volunteer();
        deletedVolunteer.setVolunteerId(42);
        deletedVolunteer.setVolunteerMobile("9503777850");
        deletedVolunteer.setDelFlg(true);
        deletedVolunteer.setVolunteerPassportPhotoUrl("/uploads/volunteers/existing-photo.jpg");

        when(volunteerRepository.findByVolunteerMobileAndDelFlgFalse("9503777850"))
                .thenReturn(Optional.empty());
        when(volunteerRepository.findByVolunteerMobileAndDelFlg("9503777850", true))
                .thenReturn(Optional.of(deletedVolunteer));
        when(volunteerRepository.save(any(Volunteer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        volunteerService.createVolunteer(volunteer("9503777850"));

        ArgumentCaptor<Volunteer> savedVolunteer = ArgumentCaptor.forClass(Volunteer.class);
        verify(volunteerRepository).save(savedVolunteer.capture());
        assertEquals(42, savedVolunteer.getValue().getVolunteerId());
        assertFalse(savedVolunteer.getValue().isDelFlg());
        assertEquals("/uploads/volunteers/existing-photo.jpg", savedVolunteer.getValue().getVolunteerPassportPhotoUrl());
        verify(registrationNotificationService).notifyVolunteerRegistration(savedVolunteer.getValue());
    }

    @Test
    void rejectsAnAlreadyActiveVolunteer() {
        Volunteer activeVolunteer = new Volunteer();
        activeVolunteer.setVolunteerMobile("9503777850");
        activeVolunteer.setDelFlg(false);
        when(volunteerRepository.findByVolunteerMobileAndDelFlgFalse("9503777850"))
                .thenReturn(Optional.of(activeVolunteer));

        assertThrows(DuplicateResourceException.class,
                () -> volunteerService.createVolunteer(volunteer("9503777850")));

        verify(volunteerRepository, never()).save(any());
    }

    private VolunteerDto volunteer(String mobile) {
        VolunteerDto volunteer = new VolunteerDto();
        volunteer.setVolunteerFName("Test");
        volunteer.setVolunteerLName("Volunteer");
        volunteer.setVolunteerMobile(mobile);
        return volunteer;
    }
}
