package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.ProductEntity;
import model.dao.entity.ProductPicEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDetail {

    private Long productId;

    private String productName;

    private String productDetail;

    private String productStatus;

    private Double expectedPrice;

    private Date publishTime;

    private UserDetail seller;

    private UserDetail buyer;

    private Date clinchTime;

    private List<ProductPic> pics;

    public ProductDetail(ProductEntity productEntity) {
        BeanUtils.copyProperties(productEntity, this);

        List<ProductPicEntity> productPicList = productEntity.getProductPicList();
        this.pics = new ArrayList<>(productPicList.size());
        for (ProductPicEntity productPicEntity : productPicList) {
            this.pics.add(new ProductPic(productPicEntity));
        }

        this.seller = new UserDetail(productEntity.getSeller());

        if (productEntity.getBuyer() != null) {
            this.buyer = new UserDetail(productEntity.getBuyer());
        }
    }
}
