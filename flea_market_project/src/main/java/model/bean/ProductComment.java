package model.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ProductComment {

    private Long commentId;

    private Long replyTo;

    private String content;

    private Date commentTime;

    private UserSummary user;
}
