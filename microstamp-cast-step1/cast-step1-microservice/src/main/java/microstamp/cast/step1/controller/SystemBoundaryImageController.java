package microstamp.cast.step1.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import microstamp.cast.step1.dto.systemboundaryimage.SystemBoundaryImageReadDto;
import microstamp.cast.step1.exception.Step1NotFoundException;
import microstamp.cast.step1.service.SystemBoundaryImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@AllArgsConstructor
@Tag(name = "CAST - System Boundary Image")
@RequestMapping("/cast/system-boundary-images")
public class SystemBoundaryImageController {

    private final SystemBoundaryImageService service;

    @GetMapping("/analysis/{id}")
    public ResponseEntity<List<SystemBoundaryImageReadDto>> findByAnalysisId(@PathVariable UUID id) {
        log.info("Request received to find CAST system boundary images by analysis id {}", id);
        return new ResponseEntity<>(service.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping(value = "/analysis/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SystemBoundaryImageReadDto> insert(@PathVariable("id") UUID analysisId, @RequestParam("file") MultipartFile file) throws Exception {
        log.info("Request received to insert CAST system boundary image for analysis id {}", analysisId);
        return new ResponseEntity<>(service.insert(analysisId, file), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws Step1NotFoundException {
        log.info("Request received to delete CAST system boundary image with id {}", id);
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
