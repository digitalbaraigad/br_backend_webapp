package com.baraigad.ourwork.controller;

import com.baraigad.common.response.ApiResponse;
import com.baraigad.common.response.ResponseUtil;
import com.baraigad.fort.controller.FortMediaStorageService;
import com.baraigad.ourwork.dto.OurWorkDto;
import com.baraigad.ourwork.service.OurWorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController @RequestMapping("/api/v1/our-work") @RequiredArgsConstructor @CrossOrigin(origins = "*")
public class OurWorkController {
 private final OurWorkService service;
 private final FortMediaStorageService mediaStorage;
 @PostMapping public ResponseEntity<ApiResponse<OurWorkDto>> create(@RequestBody OurWorkDto dto){return ResponseUtil.created("Our Work item created successfully",service.create(dto));}
 @GetMapping public ResponseEntity<ApiResponse<List<OurWorkDto>>> all(){return ResponseUtil.success("Our Work items fetched successfully",service.getAll());}
 @GetMapping("/{workId}") public ResponseEntity<ApiResponse<OurWorkDto>> get(@PathVariable Long workId){return ResponseUtil.success("Our Work item fetched successfully",service.get(workId));}
 @PutMapping("/{workId}") public ResponseEntity<ApiResponse<OurWorkDto>> update(@PathVariable Long workId,@RequestBody OurWorkDto dto){return ResponseUtil.success("Our Work item updated successfully",service.update(workId,dto));}
 @PostMapping(value="/{workId}/media",consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public ResponseEntity<ApiResponse<OurWorkDto>> media(@PathVariable Long workId,@RequestParam("files") List<MultipartFile> files){return ResponseUtil.success("Our Work media uploaded successfully",service.addMedia(workId,mediaStorage.storeAll(files)));}
 @DeleteMapping("/{workId}") public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long workId){service.delete(workId);return ResponseUtil.success("Our Work item deleted successfully","SUCCESS");}
}
