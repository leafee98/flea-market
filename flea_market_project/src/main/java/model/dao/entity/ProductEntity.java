package model.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "t_product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_product_id")
    Long productId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_buyer_id")
    UserEntity buyer;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_seller_id", nullable = false)
    UserEntity seller;

    // censoring, selling, ordered, confirmed_seller, confirmed_buyer, withdrawn
    @Column(name = "c_product_status", nullable = false)
    String productStatus;

    @Column(name = "c_expected_price", nullable = false)
    Double expectedPrice;

    @Column(name = "c_publish_time", nullable = false)
    Date publishTime;

    @Column(name = "c_product_name", nullable = false)
    String productName;

    @Lob
    @Column(name = "c_product_detail", nullable = false)
    String productDetail;

    @Column(name = "c_clinch_time")
    Date clinchTime;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductPicEntity> productPicList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductCommentEntity> commentList;
}
