package service.impl;

import lombok.extern.slf4j.Slf4j;
import model.bean.ProductComment;
import model.bean.ProductDetail;
import model.bean.ProductSummary;
import model.dao.entity.ProductCommentEntity;
import model.dao.entity.ProductEntity;
import model.dao.entity.ProductPicEntity;
import model.dao.entity.UserEntity;
import model.dao.repo.ProductCommentRepo;
import model.dao.repo.ProductPicRepo;
import model.dao.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.ProductService;
import service.SystemService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private SystemService systemService;
    private ProductRepo productRepo;
    private ProductPicRepo productPicRepo;
    private ProductCommentRepo productCommentRepo;

    @Autowired
    public ProductServiceImpl(SystemService systemService, ProductRepo productRepo,
                              ProductPicRepo productPicRepo, ProductCommentRepo productCommentRepo) {
        this.systemService = systemService;
        this.productRepo = productRepo;
        this.productPicRepo = productPicRepo;
        this.productCommentRepo = productCommentRepo;
    }

    private Boolean productSwitchableToEdit(ProductEntity product) {
        return product.getProductStatus().equals(ProductService.PRODUCT_SELLING) ||
                product.getProductStatus().equals(ProductService.PRODUCT_CENSORING) ||
                product.getProductStatus().equals(ProductService.PRODUCT_EDITING) ||
                product.getProductStatus().equals(ProductService.PRODUCT_NOT_APPROVED);
    }

    private List<Object> productEditable(@Nullable String token, Long productId) {
        UserEntity user = systemService.token2User(token);

        if (user == null) {
            log.warn("no such user");
            return null;
        }

        Optional<ProductEntity> productOpt = productRepo.findById(productId);

        if (productOpt.isPresent()) {
            if (productOpt.get().getProductStatus().equals(ProductService.PRODUCT_EDITING)) {
                if (productOpt.get().getSeller().getUserId().equals(user.getUserId())) {
                    log.info("user '{}' can edit product '{}'", user.getUsername(), productId);
                    List<Object> list = new ArrayList<>();
                    list.add(user);
                    list.add(productOpt.get());
                    return list;
                } else {
                    log.warn("user '{}' is not the seller of product '{}'", user.getUsername(), productId);
                    return null;
                }
            } else {
                log.warn("product '{}' is not in editing status", productId);
                return null;
            }
        } else {
            log.warn("no such product: '{}'", productId);
            return null;
        }
    }

    @Override
    public List<ProductSummary> getProductList() {
        List<ProductEntity> productEntityList =
                productRepo.findByProductStatusEquals(ProductService.PRODUCT_SELLING, productRepo.sortByReleaseDesc);
        List<ProductSummary> productSummaryList = new ArrayList<>(productEntityList.size());
        for (ProductEntity productEntity : productEntityList) {
            productSummaryList.add(new ProductSummary(productEntity));
        }
        return productSummaryList;
    }

    @Override
    @Nullable
    public List<ProductSummary> getBoughtProductList(String token) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token");
            return null;
        } else {
            List<ProductEntity> productEntityList = productRepo.findByBuyerEquals(user, productRepo.sortByReleaseDesc);
            List<ProductSummary> productSummaryList = new ArrayList<>(productEntityList.size());
            for (ProductEntity productEntity : productEntityList) {
                productSummaryList.add(new ProductSummary(productEntity));
            }
            log.info("user '{}' get '{}' bought product summary", user.getUsername(), productSummaryList.size());
            return productSummaryList;
        }
    }

    @Override
    @Nullable
    public List<ProductSummary> getMyProductList(String token) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token");
            return null;
        } else {
            List<ProductEntity> productEntityList = productRepo.findBySellerEquals(user, productRepo.sortByReleaseDesc);
            List<ProductSummary> productSummaryList = new ArrayList<>(productEntityList.size());
            for (ProductEntity productEntity : productEntityList) {
                productSummaryList.add(new ProductSummary(productEntity));
            }
            log.info("user '{}' get '{}' sold product summary", user.getUsername(), productSummaryList.size());
            return productSummaryList;
        }
    }

    @Nullable
    @Override
    @Transactional
    public ProductDetail getProductDetail(Long productId) {
        Optional<ProductEntity> productOpt = productRepo.findById(productId);
        if (productOpt.isPresent()) {
            log.info("get product detail id='{}'", productId);
            return new ProductDetail(productOpt.get());
        } else {
            log.warn("no such product id='{}'", productId);
            return null;
        }
    }

    @Override
    @Nullable
    public Long newProduct(String token) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token");
            return null;
        } else {
            ProductEntity product = new ProductEntity();
            product.setProductStatus(ProductService.PRODUCT_EDITING);
            product.setSeller(user);
            product.setPublishTime(new Date());
            product.setExpectedPrice(0.0);
            product.setProductName("new product");
            product.setProductDetail("product detail");
            product = productRepo.save(product);
            log.info("user '{}' create a new product '{}'", user.getUsername(), product.getProductId());
            return product.getProductId();
        }
    }

    @Override
    public Boolean deleteProduct(String token, Long productId) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token");
            return false;
        } else {
            Optional<ProductEntity> productOpt = productRepo.findById(productId);
            if (productOpt.isPresent()) {
                ProductEntity product = productOpt.get();
                if (product.getSeller().getUserId().equals(user.getUserId())) {
                    if (productSwitchableToEdit(product)) {
                        productRepo.delete(product);
                        log.info("user '{}' delete product '{}'", user.getUsername(), productId);
                        return true;
                    } else {
                        log.warn("user '{}' want to delete product '{}' while it's '{}'",
                                user.getUsername(), productId, product.getProductStatus());
                        return false;
                    }
                } else {
                    log.warn("user '{}' is not the seller of product '{}'", user.getUsername(), productId);
                    return false;
                }
            } else {
                log.warn("user '{}' want to delete not exist product '{}'", user.getUsername(), productId);
                return false;
            }
        }
    }

    @Override
    public Boolean editProduct(String token, Long productId) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token");
            return false;
        } else {
            Optional<ProductEntity> productOpt = productRepo.findById(productId);
            if (productOpt.isPresent()) {
                ProductEntity product = productOpt.get();
                if (product.getSeller().getUserId().equals(user.getUserId())) {
                    if (productSwitchableToEdit(product)) {
                        product.setProductStatus(ProductService.PRODUCT_EDITING);
                        productRepo.save(product);
                        log.info("user '{}' edit product '{}'", user.getUsername(), productId);
                        return true;
                    } else {
                        log.warn("user '{}' want to edit product '{}' while it's '{}'",
                                user.getUsername(), productId, product.getProductStatus());
                        return false;
                    }
                } else {
                    log.warn("user '{}' is not the seller of product '{}'", user.getUsername(), productId);
                    return false;
                }
            } else {
                log.warn("user '{}' want to edit not exist product '{}'", user.getUsername(), productId);
                return false;
            }
        }
    }

    @Override
    public Boolean editFinish(String token, Long productId) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token");
            return false;
        } else {
            Optional<ProductEntity> productOpt = productRepo.findById(productId);
            if (productOpt.isPresent()) {
                ProductEntity product = productOpt.get();
                if (product.getSeller().getUserId().equals(user.getUserId())) {
                    if (product.getProductStatus().equals(ProductService.PRODUCT_EDITING)) {
                        product.setProductStatus(ProductService.PRODUCT_CENSORING);
                        productRepo.save(product);
                        log.info("user '{}' finish edit on product '{}'", user.getUsername(), productId);
                        return true;
                    } else {
                        log.warn("user '{}' want to finish edit product '{}' while it's '{}'",
                                user.getUsername(), productId, product.getProductStatus());
                        return false;
                    }
                } else {
                    log.warn("user '{}' is not the seller of product '{}'", user.getUsername(), productId);
                    return false;
                }
            } else {
                log.warn("user '{}' want to finish edit on not exist product '{}'", user.getUsername(), productId);
                return false;
            }
        }
    }

    @Override
    public Boolean editExpectedPrice(String token, Long productId, Double price) {
        List<Object> list = productEditable(token, productId);
        if (list == null) {
            log.warn("edit price failed");
            return false;
        } else {
            UserEntity user = (UserEntity)list.get(0);
            ProductEntity product = (ProductEntity)list.get(1);

            product.setExpectedPrice(price);
            productRepo.save(product);
            log.info("user '{}' edit expected price to '{}' on product '{}'", user.getUsername(), price, productId);
            return true;
        }
    }

    @Override
    public Boolean editProductName(String token, Long productId, String productName) {
        List<Object> list = productEditable(token, productId);
        if (list == null) {
            log.warn("edit product name failed");
            return false;
        } else {
            UserEntity user = (UserEntity)list.get(0);
            ProductEntity product = (ProductEntity)list.get(1);

            product.setProductName(productName);
            productRepo.save(product);
            log.info("user '{}' edit product name to '{}' on product '{}'", user.getUsername(), productName, productId);
            return true;
        }
    }

    @Override
    public Boolean editProductDetail(String token, Long productId, String productDetail) {
        List<Object> list = productEditable(token, productId);
        if (list == null) {
            log.warn("edit product detail failed");
            return false;
        } else {
            UserEntity user = (UserEntity)list.get(0);
            ProductEntity product = (ProductEntity)list.get(1);

            product.setProductDetail(productDetail);
            productRepo.save(product);
            log.info("user '{}' edit product detail to '{}' on product '{}'",
                    user.getUsername(), productDetail, productId);
            return true;
        }
    }

    @Override
    @Nullable
    @Transactional()
    public Long editAddPic(String token, Long productId, String picUrl) {
        List<Object> list = productEditable(token, productId);
        if (list == null) {
            log.warn("edit add product picture failed");
            return null;
        } else {
            UserEntity user = (UserEntity)list.get(0);
            ProductEntity product = (ProductEntity)list.get(1);

            ProductPicEntity productPic = new ProductPicEntity();
            productPic.setProduct(product);
            productPic.setProductPicUrl(picUrl);

            productPic = productPicRepo.save(productPic);
            product.getProductPicList().add(productPic);
            productRepo.save(product);
            log.info("user '{}' add product picture '{}' on product '{}'", user.getUsername(), picUrl, productId);
            return productPic.getProductPicId();
        }
    }

    @Override
    public Boolean editDeletePic(String token, Long productId, Long picId) {
        List<Object> list = productEditable(token, productId);
        if (list == null) {
            log.warn("edit delete product picture failed");
            return false;
        } else {
            UserEntity user = (UserEntity)list.get(0);
            ProductEntity product = (ProductEntity)list.get(1);

            Optional<ProductPicEntity> productPicOpt = productPicRepo.findById(picId);
            if (productPicOpt.isPresent()) {
                if (productPicOpt.get().getProduct().getProductId().equals(product.getProductId())) {
                    productPicRepo.delete(productPicOpt.get());
                    log.info("user '{}' delete product picture '{}' on product '{}'",
                            user.getUsername(), picId, productId);
                    return true;
                } else {
                    log.warn("user '{}' delete product picture '{}' not belong to product '{}'",
                            user.getUsername(), picId, productId);
                    return false;
                }
            } else {
                log.warn("user '{}' delete not exist product picture '{}' on product '{}'",
                        user.getUsername(), picId, productId);
                return false;
            }
        }
    }

    // @Override
    // public Boolean modifyProduct(String token, ProductDetail product) {
    //     Optional<ProductEntity> productEntityOpt = productRepo.findById(product.getProductId());
    //     UserEntity user = systemService.token2User(token);
    //     if (user == null) {
    //         log.warn("invalid or expired token '{}'", token);
    //         return false;
    //     } else {
    //         if (productEntityOpt.isPresent()) {
    //             ProductEntity productEntity = productEntityOpt.get();
    //             if (product.getSeller().getUsername().equals(productEntity.getSeller().getUsername())) {
    //                 BeanUtils.copyProperties(product, productEntity);

    //                 List<ProductPic> productPicList = product.getPics();
    //                 // List<ProductPicEntity> productPicEntityList = new ArrayList<>(productPicList.size());
    //                 for (ProductPic productPic : productPicList) {
    //                     ProductPicEntity picEntity = new ProductPicEntity();
    //                     picEntity.setProduct(productEntity);
    //                     picEntity.setProductPicUrl(productPic.getProductPicUrl());
    //                     productPicRepo.save(picEntity);
    //                     // productPicEntityList.set(i, new)
    //                 }
    //                 // productEntity.setProductPicList(productPicEntityList);
    //                 productRepo.save(productEntity);

    //                 log.info("user '{}' modified product('{}')", user.getUsername(), productEntity.getProductId());
    //                 return true;
    //             } else {
    //                 log.warn("product('{}')'s seller is not '{}'", product.getProductId(), user.getUsername());
    //                 return false;
    //             }
    //         } else {
    //             log.warn("product id had been changed or no such product whose id is '{}'", product.getProductId());
    //             return false;
    //         }
    //     }
    // }

    @Override
    public Boolean orderProduct(String token, Long productId) {
        UserEntity user = systemService.token2User(token);
        Optional<ProductEntity> productOpt = productRepo.findById(productId);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return false;
        } else {
            if (productOpt.isPresent()) {
                ProductEntity product = productOpt.get();
                if (product.getProductStatus().equals(ProductService.PRODUCT_SELLING)) {
                    product.setBuyer(user);
                    product.setProductStatus(ProductService.PRODUCT_ORDERED);
                    productRepo.save(product);
                    log.info("user '{}' order product '{}'", user.getUsername(), productId);
                    return true;
                } else {
                    log.warn("user '{}' want to order product '{}' which are not selling",
                            user.getUsername(), productId);
                    return false;
                }
            } else {
                log.warn("invalid productId");
                return false;
            }
        }
    }

    @Override
    public Boolean cancelOrder(String token, Long productId) {
        UserEntity user = systemService.token2User(token);
        Optional<ProductEntity> productOpt = productRepo.findById(productId);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return false;
        } else {
            if (productOpt.isPresent()) {
                ProductEntity product = productOpt.get();
                if (product.getProductStatus().equals(ProductService.PRODUCT_ORDERED)
                        || product.getProductStatus().equals(ProductService.PRODUCT_CONFIRM_SELLER)
                        || product.getProductStatus().equals(ProductService.PRODUCT_CONFIRM_BUYER)) {
                    if (product.getBuyer().getUserId().equals(user.getUserId())) {
                        product.setProductStatus(ProductService.PRODUCT_SELLING);
                        product.setBuyer(null);
                        productRepo.save(product);

                        log.info("buyer '{}' canceled order on product '{}'", user.getUsername(), productId);
                        return true;
                    }  else if (product.getSeller().getUserId().equals(user.getUserId())) {
                        product.setProductStatus(ProductService.PRODUCT_SELLING);
                        product.setBuyer(null);
                        productRepo.save(product);

                        log.info("seller '{}' canceled order on product '{}'", user.getUsername(), productId);
                        return true;
                    } else {
                        log.warn("user '{}' cancel order on product '{}' which not related to him(she)",
                                user.getUsername(), productId);
                        return false;
                    }
                } else {
                    log.warn("user '{}' cancel order on product '{}' which is not ordered",
                            user.getUsername(), productId);
                    return false;
                }
            } else {
                log.warn("invalid productId");
                return false;
            }
        }
    }

    @Override
    public Boolean confirmOrder(String token, Long productId) {
        UserEntity user = systemService.token2User(token);
        Optional<ProductEntity> productOpt = productRepo.findById(productId);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return false;
        } else {
            if (productOpt.isPresent()) {
                ProductEntity product = productOpt.get();
                switch (product.getProductStatus()) {
                    case ProductService.PRODUCT_ORDERED:
                        if (product.getSeller().getUserId().equals(user.getUserId())) {
                            product.setProductStatus(ProductService.PRODUCT_CONFIRM_SELLER);
                            productRepo.save(product);
                            return true;
                        } else if (product.getBuyer().getUserId().equals(user.getUserId())) {
                            product.setProductStatus(ProductService.PRODUCT_CONFIRM_BUYER);
                            productRepo.save(product);
                            return true;
                        } else {
                            log.warn("user '{}' want to confirm product '{}' which has not relation to him(she)",
                                    user.getUsername(), productId);
                            return false;
                        }
                    case ProductService.PRODUCT_CONFIRM_SELLER:
                        if (product.getBuyer().getUserId().equals(user.getUserId())) {
                            product.setProductStatus(ProductService.PRODUCT_CLINCH);
                            productRepo.save(product);
                            return true;
                        } else {
                            log.warn("user '{}' want to confirm product '{}' which has not relation to him(she)",
                                    user.getUsername(), productId);
                            return false;
                        }
                    case ProductService.PRODUCT_CONFIRM_BUYER:
                        if (product.getSeller().getUserId().equals(user.getUserId())) {
                            product.setProductStatus(ProductService.PRODUCT_CLINCH);
                            productRepo.save(product);
                            return true;
                        } else {
                            log.warn("user '{}' want to confirm product '{}' which has not relation to him(she)",
                                    user.getUsername(), productId);
                            return false;
                        }
                    default:
                        log.warn("user '{}' want to confirm product '{}' which cannot be confirmed",
                                user.getUsername(), productId);
                        return false;
                }
            } else {
                log.warn("invalid productId");
                return false;
            }
        }
    }

    @Override
    public Boolean comment(String token, Long productId, String content) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return false;
        } else {
            Optional<ProductEntity> productOpt = productRepo.findById(productId);
            if (productOpt.isPresent()) {
                ProductEntity product = productOpt.get();
                ProductCommentEntity commentEntity = new ProductCommentEntity();
                commentEntity.setUser(user);
                commentEntity.setProduct(product);
                commentEntity.setContent(content);
                commentEntity.setCommentTime(new Date());
                productCommentRepo.save(commentEntity);

                log.info("user '{}' comment on product '{}': '{}'",
                        user.getUsername(), productId, content);
                return true;
            } else {
                log.warn("user '{}' comment on not exist product '{}'", user.getUsername(), productId);
                return false;
            }
        }
    }

    @Override
    @Nullable
    public List<ProductComment> getComments(Long productId) {
        Optional<ProductEntity> productOpt = productRepo.findById(productId);
        if (productOpt.isPresent()) {
            List<ProductCommentEntity> commentEntityList =
                    productCommentRepo.findByProductEquals(productOpt.get());
            List<ProductComment> commentList = new ArrayList<>(commentEntityList.size());
            for (ProductCommentEntity productCommentEntity : commentEntityList) {
                commentList.add(new ProductComment(productCommentEntity));
            }
            log.info("somebody get '{}' comment(s) on product '{}'", commentList.size(), productId);
            return commentList;
        } else {
            log.warn("somebody get product comment on not exist product '{}'", productId);
            return null;
        }
    }
}
