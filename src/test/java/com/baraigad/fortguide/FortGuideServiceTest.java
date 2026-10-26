package com.baraigad.fortguide;
import com.baraigad.fort.entity.Fort;
import com.baraigad.fort.repository.FortRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.nio.charset.StandardCharsets;
import static com.baraigad.fortguide.GuideData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FortGuideServiceTest {
    GuideData guide(String title,String marathiAudio) {
        return new GuideData(3L,title,"चाचणी किल्ला","Test only","केवळ चाचणी","Test reviewer",List.of(
                new Point("gate-1","Test gate","चाचणी दरवाजा",73d,18d,"Synthetic test text","चाचणी माहिती",
                        "data:audio/wav;base64,UklGRg==",marathiAudio,"data:image/png;base64,iVBORw==","reconstruction","Test creator","Synthetic fixture only")));
    }
    @Test void requiresBilingualMediaAndRejectsRemoteUrls() {
        var valid=guide("Synthetic fort","data:audio/wav;base64,UklGRg==");FortGuideService.validate(valid,true);
        FortGuideService.validate(guide("Synthetic fort",""),false);
        assertThrows(IllegalArgumentException.class,()->FortGuideService.validate(guide("X",""),true));
        assertThrows(IllegalArgumentException.class,()->FortGuideService.media("https://example.test/audio.mp3",true));
        assertThrows(IllegalArgumentException.class,()->FortGuideService.media("data:image/svg+xml;base64,PHN2Zz4=",false));
        assertThrows(IllegalArgumentException.class,()->FortGuideService.media("data:audio/wav;base64,%%%",true));
    }
    @Test void draftEditsDoNotChangeOfflinePackageUntilReviewedPublication() throws Exception {
        var repository=mock(FortGuideRepository.class);var forts=mock(FortRepository.class);var json=new ObjectMapper();var service=new FortGuideService(repository,forts,json);
        var row=new FortGuide();row.setFortId(3L);row.setRevision(0L);row.setDraftJson(json.writeValueAsString(guide("First","data:audio/wav;base64,UklGRg==")));
        when(repository.findById(3L)).thenReturn(Optional.of(row));when(forts.findById(3L)).thenReturn(Optional.of(new Fort()));
        when(repository.saveAndFlush(any())).thenAnswer(call->{FortGuide r=call.getArgument(0);r.setRevision(r.getRevision()+1);return r;});
        assertThrows(IllegalArgumentException.class,()->service.publish(3,new Publish(0L,false)));
        assertEquals(1,service.publish(3,new Publish(0L,true)).version());
        byte[] original=service.download(3);assertTrue(new String(original,StandardCharsets.UTF_8).contains("ba-raigad-fort-guide"));
        service.save(3,new Save(1L,guide("Edited","data:audio/wav;base64,UklGRg==")));assertArrayEquals(original,service.download(3));
        assertThrows(IllegalArgumentException.class,()->service.publish(3,new Publish(1L,true)));
        assertEquals(2,service.publish(3,new Publish(2L,true)).version());assertFalse(Arrays.equals(original,service.download(3)));
        service.unpublish(3,new Publish(3L,false));assertThrows(com.baraigad.common.exception.ResourceNotFoundException.class,()->service.download(3));
    }
    @Test void missingFortAndIdMismatchCannotCreateGuide() {
        var repository=mock(FortGuideRepository.class);var forts=mock(FortRepository.class);var service=new FortGuideService(repository,forts,new ObjectMapper());
        when(forts.findById(3L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,()->service.save(3,new Save(null,guide("X",""))));
        assertThrows(IllegalArgumentException.class,()->service.save(4,new Save(null,guide("X",""))));
        verify(repository,never()).saveAndFlush(any());
    }
    @Test void managedPointDeletionReplacementAndCategoriesSurvivePublication() throws Exception {
        var repository=mock(FortGuideRepository.class);var forts=mock(FortRepository.class);var json=new ObjectMapper();var service=new FortGuideService(repository,forts,json);
        var initial=guide("Test fort","data:audio/wav;base64,UklGRg==");var p=initial.points().get(0);
        var edited=new Point(p.id(),"Edited name",p.nameMr(),73.1d,18.1d,p.text(),p.textMr(),
                "data:audio/ogg;base64,AQID","data:audio/mpeg;base64,BAUG",p.imageData(),p.imageKind(),p.imageCredit(),p.imageSource(),"water");
        var managed=new GuideData(3L,initial.title(),initial.titleMr(),initial.description(),initial.descriptionMr(),initial.verifiedBy(),List.of(edited),true);
        var row=new FortGuide();row.setFortId(3L);row.setRevision(0L);row.setDraftJson(json.writeValueAsString(initial));
        when(repository.findById(3L)).thenReturn(Optional.of(row));when(forts.findById(3L)).thenReturn(Optional.of(new Fort()));
        when(repository.saveAndFlush(any())).thenAnswer(call->{FortGuide r=call.getArgument(0);r.setRevision(r.getRevision()+1);return r;});
        assertTrue(service.save(3,new Save(0L,managed)).guide().managedPoints());
        service.publish(3,new Publish(1L,true));
        var published=json.readValue(service.download(3),GuideData.Package.class).guide();
        assertEquals(managed,published);assertEquals("water",published.points().get(0).category());
        // Previously stored JSON without these optional fields remains readable.
        var legacy=json.readValue(json.writeValueAsString(initial).replace(",\"managedPoints\":null","").replace(",\"category\":null",""),GuideData.class);
        assertNull(legacy.managedPoints());FortGuideService.validate(legacy,true);
    }
}
