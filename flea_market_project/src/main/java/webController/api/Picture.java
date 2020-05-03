package webController.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import service.PictureService;
import webController.Utils;

import java.io.IOException;

@Slf4j
@RestController
public class Picture {
    private PictureService pictureService;

    @Autowired
    public Picture(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @RequestMapping(value = "/api/picture/upload", method = RequestMethod.POST, produces = {"application/json"})
    public String storePic(MultipartFile pic) {
        if (pic == null || pic.isEmpty()) {
            log.warn("store picture but multipart file is empty");
            return Utils.format(false, "file uploaded is empty");
        }

        try {
            byte[] picBytes = pic.getBytes();
            String picUid = pictureService.storePic(picBytes);
            log.info("picture '{}' stored.", picUid);
            return Utils.format(true, picUid);
        } catch (IOException e) {
            log.warn("IOException occurred: '{}'", e.toString());
            return Utils.format(false, "Server internal error while upload picture");
        }
    }

    @RequestMapping(value = "/api/picture/download")
    public ResponseEntity<byte[]> getPic(String picUid) {
        byte[] picByte = pictureService.getPic(picUid);
        if (picByte == null) {
            log.warn("no such pic '{}'", picUid);
            return ResponseEntity.badRequest().body(null);
        }

        log.info("picture '{}' gotten", picUid);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(picByte);
    }
}
