package model.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ProductSummary {

    Long productId;

    String productSummary;

    Date publishTime;

    UserSummary seller;
}
