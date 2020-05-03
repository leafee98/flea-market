package webController;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

/**
 * response body should like the follow.
 * data is serialized bean if success is true,
 * or else data is error message.
 * {
 *     "success": true,
 *     "data": null
 * }
 */
@Slf4j
public class Utils {
    @Nullable
    public static String format(Boolean succeed, Object data) {
        JsonMapper mapper = new JsonMapper();
        try {
            String dataJson = mapper.writeValueAsString(data);
            if (succeed) {
                return "{\"success\": true, \"data\": " + dataJson + "}";
            } else {
                return "{\"success\": false, \"data\": " + dataJson + "}";
            }
        } catch (JsonProcessingException e) {
            log.error("format json response failed", e);
            return null;
        }
    }
}
