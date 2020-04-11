package model.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProductDetail {

    private Long productId;

    private String productName;

    private String productDetail;

    private Date clinchTime;

    private Double expectedPrice;

    private Date publishTime;

    private UserDetail seller;

    private List<ProductPic> pics;
}
