package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.ProductPicEntity;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class ProductPic {

    private String productPicUrl;

    private Long productPicId;

    public ProductPic(ProductPicEntity productPicEntity) {
        BeanUtils.copyProperties(productPicEntity, this);
    }
}
