package model.dao.repo;

import model.dao.entity.ProductEntity;
import model.dao.entity.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long> {

    Sort sortByReleaseDesc = Sort.by(Sort.Direction.DESC, "releaseTime");

    List<ProductEntity> findByProductStatusEquals(String status, Sort sort);

    List<ProductEntity> findBySellerEquals(UserEntity seller, Sort sort);

    List<ProductEntity> findByBuyerEquals(UserEntity buyer, Sort sort);

    List<ProductEntity> findTopByProductStatusEquals(String status);
}
