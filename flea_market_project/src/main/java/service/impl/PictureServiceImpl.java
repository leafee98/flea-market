package service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import service.PictureService;

import java.util.UUID;

@Slf4j
@Service
public class PictureServiceImpl implements PictureService {

    RedisTemplate<String, byte[]> redisTemplate;

    @Autowired
    public PictureServiceImpl(@Qualifier(value = "redisTemplate") RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public String storePic(byte[] fileByte) {
        String picUid = UUID.randomUUID().toString();
        ValueOperations<String, byte[]> ops = redisTemplate.opsForValue();
        ops.set(picUid, fileByte);
        log.info("pic {} saved", picUid);
        return picUid;
    }

    @Override
    @Nullable
    public byte[] getPic(String picUid) {
        ValueOperations<String, byte[]> ops = redisTemplate.opsForValue();
        byte[] fileByte = ops.get(picUid);
        if (fileByte == null) {
            log.warn("getting not existed pic {}", picUid);
            return null;
        } else {
            log.info("got pic {}", picUid);
            return fileByte;
        }
    }
}
