package com.baraigad.fortguide;

import com.baraigad.common.exception.ResourceNotFoundException;
import com.baraigad.fort.repository.FortRepository;
import com.baraigad.trail.TrailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import static com.baraigad.fortguide.GuideData.*;

@Service @RequiredArgsConstructor
public class FortGuideService {
    public static final int MAX_PACKAGE_BYTES = 32 * 1024 * 1024;
    private final FortGuideRepository repository;
    private final FortRepository forts;
    private final ObjectMapper json;

    private static void text(String value, int max, boolean required) {
        if (value == null || value.length() > max || (required && value.isBlank()))
            throw new IllegalArgumentException("Required guide text is missing or exceeds its length limit.");
    }
    public static void validate(GuideData guide, boolean publishing) {
        if (guide == null || guide.fortId() == null || guide.fortId() < 1) throw new IllegalArgumentException("Choose a fort.");
        text(guide.title(),160,true); text(guide.titleMr(),160,publishing);
        text(guide.description(),10000,false); text(guide.descriptionMr(),10000,false);
        text(guide.verifiedBy(),160,publishing);
        if (guide.points() == null || guide.points().size() > 100 || (publishing && guide.points().isEmpty()))
            throw new IllegalArgumentException("A published guide needs 1–100 monument points.");
        Set<String> ids = new HashSet<>();
        for (Point p : guide.points()) {
            if (p == null || p.id() == null || !p.id().matches("[A-Za-z0-9_-]{1,64}") || !ids.add(p.id()))
                throw new IllegalArgumentException("Point IDs must be unique and contain letters, numbers, underscores or hyphens.");
            text(p.name(),160,true); text(p.nameMr(),160,publishing);
            if (p.category() != null && !List.of("temple","gate","water","palace","memorial","market","viewpoint","transport","other").contains(p.category()))
                throw new IllegalArgumentException("Choose a valid monument category.");
            text(p.text(),10000,publishing); text(p.textMr(),10000,publishing);
            if (p.longitude() == null || p.latitude() == null) throw new IllegalArgumentException("Every point needs surveyed coordinates.");
            TrailService.validateCoordinate(p.longitude(),p.latitude());
            media(p.audioData(),true); media(p.audioDataMr(),true); media(p.imageData(),false);
            if (p.imageKind() == null || !List.of("reconstruction","archival","current").contains(p.imageKind())) throw new IllegalArgumentException("Choose an image type.");
            text(p.imageCredit(),500,publishing); text(p.imageSource(),2000,publishing);
            if (publishing && (p.audioData().isBlank() || p.audioDataMr().isBlank() || p.imageData().isBlank()))
                throw new IllegalArgumentException("Publishing requires English and Marathi audio plus an image for every monument.");
        }
    }
    /** Self-contained media prevents offline playback from depending on external URLs. */
    static void media(String value, boolean audio) {
        if (value == null) throw new IllegalArgumentException("Media field is required (empty is allowed in drafts).");
        if (value.isEmpty()) return;
        int limit = (audio ? 8 : 4) * 1024 * 1024;
        if (value.length() > limit * 4 / 3 + 128) throw new IllegalArgumentException("Audio is limited to 8 MB; images to 4 MB.");
        int comma = value.indexOf(',');
        if (comma < 0) throw new IllegalArgumentException("Media must be an embedded base64 file.");
        String prefix = value.substring(0,comma);
        List<String> allowed = audio ? List.of("data:audio/mpeg;base64","data:audio/mp4;base64","data:audio/ogg;base64","data:audio/wav;base64","data:audio/x-wav;base64")
                : List.of("data:image/jpeg;base64","data:image/png;base64","data:image/webp;base64");
        if (!allowed.contains(prefix)) throw new IllegalArgumentException("Unsupported media format. Use MP3/M4A/OGG/WAV or JPG/PNG/WebP.");
        try {
            byte[] bytes = Base64.getDecoder().decode(value.substring(comma+1));
            if (bytes.length == 0 || bytes.length > limit) throw new IllegalArgumentException("Media file is empty or too large.");
        } catch (IllegalArgumentException ex) { throw new IllegalArgumentException("Invalid or oversized base64 media file."); }
    }
    private String write(Object value) {
        try {
            String output = json.writeValueAsString(value);
            if (output.getBytes(StandardCharsets.UTF_8).length > MAX_PACKAGE_BYTES) throw new IllegalArgumentException("Guide package exceeds 32 MB. Compress media or shorten audio.");
            return output;
        } catch (IllegalArgumentException ex) { throw ex; }
        catch (Exception ex) { throw new IllegalArgumentException("Invalid guide data.",ex); }
    }
    private <T> T read(String value, Class<T> type) {
        try { return json.readValue(value,type); } catch (Exception ex) { throw new IllegalStateException("Stored guide is invalid.",ex); }
    }
    private void revision(FortGuide row, Long expected) {
        if (!Objects.equals(row.getRevision(),expected)) throw new IllegalArgumentException("Guide changed. Reload it before saving or publishing.");
    }
    private Draft dto(FortGuide row) { return new Draft(row.getFortId(),row.getRevision(),row.getPackageVersion(),row.isPublished(),read(row.getDraftJson(),GuideData.class)); }
    public Draft draft(long id) { return repository.findById(id).map(this::dto).orElse(null); }
    @Transactional public Draft save(long id, Save request) {
        if (request == null || request.guide() == null || !Objects.equals(request.guide().fortId(),id)) throw new IllegalArgumentException("Fort ID mismatch.");
        validate(request.guide(),false);
        var fort = forts.findById(id).orElseThrow(() -> new IllegalArgumentException("Fort was not found."));
        if (Boolean.TRUE.equals(fort.getDelFlg()) || Boolean.FALSE.equals(fort.getStatus())) throw new IllegalArgumentException("Fort is unavailable.");
        var row = repository.findById(id).orElseGet(() -> { var created = new FortGuide(); created.setFortId(id); return created; });
        revision(row,request.revision()); row.setDraftJson(write(request.guide()));
        return dto(repository.saveAndFlush(row));
    }
    @Transactional public Draft publish(long id, Publish request) {
        var row = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Save the guide first."));
        if (request == null || !request.reviewed()) throw new IllegalArgumentException("Confirm the historical content and media review.");
        revision(row,request.revision()); var guide = read(row.getDraftJson(),GuideData.class); validate(guide,true);
        row.setPackageVersion(row.getPackageVersion()+1);
        row.setPublishedJson(write(new GuideData.Package(1,"ba-raigad-fort-guide",id,row.getPackageVersion(),Instant.now().toString(),guide)));
        row.setPublished(true); return dto(repository.saveAndFlush(row));
    }
    @Transactional public Draft unpublish(long id, Publish request) {
        var row = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Guide was not found."));
        if (request == null) throw new IllegalArgumentException("Revision is required.");
        revision(row,request.revision()); row.setPublished(false); return dto(repository.saveAndFlush(row));
    }
    public List<Manifest> catalogue(int page) {
        if (page < 0) throw new IllegalArgumentException("Page must be zero or greater.");
        return repository.findByPublishedTrue(PageRequest.of(page,50)).stream().map(row -> {
            var pkg = read(row.getPublishedJson(),GuideData.Package.class); byte[] bytes = row.getPublishedJson().getBytes(StandardCharsets.UTF_8);
            try { return new Manifest(row.getFortId(),pkg.guide().title(),pkg.guide().titleMr(),pkg.guide().points().size(),pkg.version(),bytes.length,
                    HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)),pkg.publishedAt()); }
            catch (Exception ex) { throw new IllegalStateException(ex); }
        }).toList();
    }
    public byte[] download(long id) {
        var row = repository.findById(id).filter(FortGuide::isPublished).orElseThrow(() -> new ResourceNotFoundException("Fort guide is not published yet."));
        return row.getPublishedJson().getBytes(StandardCharsets.UTF_8);
    }
}
