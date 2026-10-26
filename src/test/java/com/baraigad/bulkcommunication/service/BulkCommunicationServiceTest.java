package com.baraigad.bulkcommunication.service;

import com.baraigad.bulkcommunication.dto.BulkRecipientDto;
import com.baraigad.common.email.service.EmailService;
import com.baraigad.dynamicform.repository.DynamicFormRepository;
import com.baraigad.dynamicform.repository.DynamicFormSubmissionRepository;
import com.baraigad.participant.repository.ParticipantRepository;
import com.baraigad.volunteer.entity.Volunteer;
import com.baraigad.volunteer.repository.VolunteerRepository;
import com.baraigad.whatsapp.service.WhatsAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BulkCommunicationServiceTest {

    @Test
    void returnsVolunteerRecipientsWithTheirUsableContactDetails() {
        ParticipantRepository participantRepository = mock(ParticipantRepository.class);
        VolunteerRepository volunteerRepository = mock(VolunteerRepository.class);
        DynamicFormRepository formRepository = mock(DynamicFormRepository.class);
        DynamicFormSubmissionRepository submissionRepository = mock(DynamicFormSubmissionRepository.class);
        EmailService emailService = mock(EmailService.class);
        WhatsAppService whatsAppService = mock(WhatsAppService.class);

        Volunteer volunteer = new Volunteer();
        volunteer.setVolunteerId(9);
        volunteer.setVolunteerFName("Asha");
        volunteer.setVolunteerLName("Patil");
        volunteer.setVolunteerMobile("9876543210");
        volunteer.setVolunteerEmail("asha@example.com");
        volunteer.setVolunteerDistrict("Raigad");
        volunteer.setVolunteerTaluka("Mahad");
        when(volunteerRepository.findByDelFlgFalse()).thenReturn(List.of(volunteer));

        BulkCommunicationService service = new BulkCommunicationService(
                participantRepository, volunteerRepository, formRepository, submissionRepository,
                emailService, whatsAppService, new ObjectMapper());

        List<BulkRecipientDto> recipients = service.recipients("VOLUNTEERS", null);

        assertEquals(1, recipients.size());
        assertEquals("VOLUNTEER:9", recipients.get(0).recipientKey());
        assertEquals("Asha Patil", recipients.get(0).name());
        assertEquals("9876543210", recipients.get(0).mobile());
        assertTrue(recipients.get(0).email().contains("@"));
        verifyNoInteractions(participantRepository, formRepository, submissionRepository);
    }
}
