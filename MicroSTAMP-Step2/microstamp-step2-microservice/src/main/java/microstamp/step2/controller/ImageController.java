package microstamp.step2.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import microstamp.step2.dto.image.ImageReadDto;
import microstamp.step2.exception.Step2NotFoundException;
import microstamp.step2.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/images")
@Tag(name = "Image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<List<ImageReadDto>> findAll() {
        return new ResponseEntity<>(imageService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ImageReadDto> findById(@PathVariable("id") UUID id) throws Step2NotFoundException {
        return new ResponseEntity<>(imageService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/analysis/{id}"})
    public ResponseEntity<List<ImageReadDto>> findByAnalysisId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(imageService.findByAnalysisId(id), HttpStatus.OK);
    }

    @PostMapping(value = "/analysis/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageReadDto> insert(@PathVariable("id") UUID analysisId, @RequestParam("file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(imageService.insert(analysisId, file), HttpStatus.CREATED);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws Exception {
        imageService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
