package model.dao.entity;

import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_product_pic")
public class ProductPicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_product_pic_id")
    Long productPicId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_product_id", nullable = false)
    ProductEntity product;

    @Column(name = "c_product_pic_url", nullable = false)
    String productPicUrl;
}
