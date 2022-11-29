package vincentbaertsch.advend.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vincentbaertsch.advend.security.SecurityConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/admin/uploads/videos")
public class VideoController {
    @GetMapping(value = "/{challenge}/{user}/{filename}", produces = "video/mp4")
    public ResponseEntity<ByteArrayResource> image(@PathVariable("challenge") long challenge, @PathVariable("user") String user, @PathVariable("filename") String filename) throws IOException {

        final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(
                "./challenges/"+challenge+"/"+user+"/"+filename
        )));
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);

    }
}
