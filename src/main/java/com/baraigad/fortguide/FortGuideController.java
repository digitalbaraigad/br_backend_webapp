package com.baraigad.fortguide;
import com.baraigad.common.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.baraigad.fortguide.GuideData.*;

@RestController @RequiredArgsConstructor
public class FortGuideController {
    private final FortGuideService service;
    @GetMapping("/api/public/fort-guides") public Object catalogue(@RequestParam(defaultValue="0") int page) { return ResponseUtil.success("Fort guides",service.catalogue(page)); }
    @GetMapping("/api/public/fort-guides/{id}/package") public ResponseEntity<byte[]> download(@PathVariable long id) {
        return ResponseEntity.ok().header("Content-Type","application/json").header("Cache-Control","no-cache")
                .header("Content-Disposition","attachment; filename=fort-guide-"+id+".json").body(service.download(id));
    }
    @GetMapping("/api/v1/fort-guides/{id}") public Object draft(@PathVariable long id) { return ResponseUtil.success("Guide draft",service.draft(id)); }
    @PutMapping("/api/v1/fort-guides/{id}") public Object save(@PathVariable long id,@RequestBody Save request) { return ResponseUtil.success("Guide saved",service.save(id,request)); }
    @PostMapping("/api/v1/fort-guides/{id}/publish") public Object publish(@PathVariable long id,@RequestBody Publish request) { return ResponseUtil.success("Guide published",service.publish(id,request)); }
    @PostMapping("/api/v1/fort-guides/{id}/unpublish") public Object unpublish(@PathVariable long id,@RequestBody Publish request) { return ResponseUtil.success("Guide unpublished",service.unpublish(id,request)); }
}
