package com.competition.files;

import com.itextpdf.text.DocumentException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/file")
    public ResponseEntity<?> importFromFile(@RequestParam MultipartFile file) {

        return ResponseEntity.ok(fileService.importDataFromCSV(file));
    }

    @GetMapping("/all")
    public ResponseEntity<byte[]> printAll() throws DocumentException, IOException {
        File file = fileService.printAll();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_TYPE, file.getType())
                .header("filename", file.getName())
                .body(file.getData());
    }

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generate(@RequestParam Integer number) throws IOException {
        File file = fileService.generateCSV(number);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_TYPE, file.getType())
                .header("filename", file.getName())
                .body(file.getData());
    }
}
