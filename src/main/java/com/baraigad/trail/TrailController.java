package com.baraigad.trail;

import com.baraigad.common.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.baraigad.trail.TrailData.*;

@RestController @RequiredArgsConstructor
public class TrailController {
    private final TrailService service;
    @GetMapping("/api/public/trails")
    public Object catalogue(@RequestParam(defaultValue = "0") int page) {
        return ResponseUtil.success("Published trails", service.catalogue(page));
    }
    @GetMapping("/api/public/trails/{id}/package")
    public ResponseEntity<byte[]> download(@PathVariable long id) {
        return ResponseEntity.ok().header("Content-Type", "application/json")
                .header("Cache-Control", "no-cache")
                .header("Content-Disposition", "attachment; filename=trail-" + id + ".json")
                .body(service.download(id));
    }
    @GetMapping("/api/v1/trails")
    public Object drafts(@RequestParam(defaultValue = "0") int page) {
        return ResponseUtil.success("Trail drafts", service.drafts(page));
    }
    @PostMapping("/api/v1/trails")
    public Object create(@RequestBody SaveRequest request) {
        return ResponseUtil.created("Draft saved", service.save(null, request));
    }
    @PutMapping("/api/v1/trails/{id}")
    public Object save(@PathVariable long id, @RequestBody SaveRequest request) {
        return ResponseUtil.success("Draft saved", service.save(id, request));
    }
    @PostMapping("/api/v1/trails/{id}/publish")
    public Object publish(@PathVariable long id, @RequestBody PublishRequest request) {
        return ResponseUtil.success("Trail published", service.publish(id, request));
    }
    @PostMapping("/api/v1/trails/{id}/unpublish")
    public Object unpublish(@PathVariable long id, @RequestBody PublishRequest request) {
        return ResponseUtil.success("Trail unpublished; existing downloads remain on devices", service.unpublish(id, request));
    }
}
