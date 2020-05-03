package service;

import org.springframework.lang.Nullable;

public interface PictureService {
    String storePic(byte[] f);

    @Nullable
    byte[] getPic(String picUid);
}
