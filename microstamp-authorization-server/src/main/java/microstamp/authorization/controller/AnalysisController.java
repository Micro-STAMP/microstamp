package microstamp.authorization.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import microstamp.authorization.dto.AnalysisInsertDto;
import microstamp.authorization.dto.AnalysisReadDto;
import microstamp.authorization.dto.AnalysisUpdateDto;
import microstamp.authorization.dto.ImageReadDto;
import microstamp.authorization.exception.NotFoundException;
import microstamp.authorization.service.AnalysisService;
import microstamp.authorization.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/analyses")
@AllArgsConstructor
@Tag(name = "Analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<List<AnalysisReadDto>> findAll() {
        return new ResponseEntity<>(analysisService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisReadDto> findById(@PathVariable("id") UUID id) throws NotFoundException {
        return new ResponseEntity<>(analysisService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"user/{id}"})
    public ResponseEntity<List<AnalysisReadDto>> findByUserId(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(analysisService.findByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AnalysisReadDto> insert(@Valid @RequestBody AnalysisInsertDto analysisInsertDto) throws NotFoundException {
        return new ResponseEntity<>(analysisService.insert(analysisInsertDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnalysisReadDto> update(@PathVariable("id") UUID id, @Valid @RequestBody AnalysisUpdateDto analysisUpdateDto) throws NotFoundException {
        analysisService.update(id, analysisUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) throws NotFoundException {
        analysisService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageReadDto> insertImage(@PathVariable("id") UUID analysisId, @RequestParam("file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(imageService.insert(analysisId, file), HttpStatus.CREATED);
    }

    @DeleteMapping(path = {"/{id}/image"})
    public ResponseEntity<Void> deleteImage(@PathVariable("id") UUID id) throws Exception {
        imageService.deleteByAnalysisId(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
