package com.baraigad.trail;

import com.baraigad.common.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController @RequestMapping("/api/v1/treks") @RequiredArgsConstructor
public class TrekController {
    private final TrekService service;
    @PostMapping("/sync")
    public Object sync(Principal principal, @RequestBody TrekService.Trek trek) {
        return ResponseUtil.success("Trek synced", Map.of("id", service.sync(principal.getName(), trek), "clientId", trek.clientId()));
    }
    @GetMapping
    public Object history(Principal principal, @RequestParam(defaultValue = "0") int page) {
        return ResponseUtil.success("Your trek history", service.history(principal.getName(), page));
    }
}
