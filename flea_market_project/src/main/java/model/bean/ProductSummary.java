package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.ProductEntity;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProductSummary {

    Long productId;

    String productName;

    Date publishTime;

    UserSummary seller;

    public ProductSummary(ProductEntity product) {
        BeanUtils.copyProperties(product, this);
        this.setSeller(new UserSummary(product.getSeller()));
    }
}
