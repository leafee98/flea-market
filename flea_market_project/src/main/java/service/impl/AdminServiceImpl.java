package service.impl;

import lombok.extern.slf4j.Slf4j;
import model.bean.ProductSummary;
import model.dao.entity.BulletinMsgEntity;
import model.dao.entity.ProductEntity;
import model.dao.entity.UserEntity;
import model.dao.repo.BulletinMsgRepo;
import model.dao.repo.ProductRepo;
import model.dao.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import service.AdminService;
import service.SystemService;

import java.util.*;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    private SystemService systemService;
    private UserRepo userRepo;
    private ProductRepo productRepo;
    private BulletinMsgRepo bulletinMsgRepo;

    @Autowired
    public AdminServiceImpl(SystemService systemService, UserRepo userRepo,
                            ProductRepo productRepo, BulletinMsgRepo bulletinMsgRepo) {
        this.systemService = systemService;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.bulletinMsgRepo = bulletinMsgRepo;
    }

    @Nullable
    private UserEntity validateAdmin(String token) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalided token {}", token);
            return null;
        } else {
            if (user.getAdmin()) {
                log.info("user {} admin validate pass", user.getUsername());
                return user;
            } else {
                log.warn("user {} not admin", user.getUsername());
                return null;
            }
        }
    }


    @Override
    public Boolean grantUser(String token, String username) {
        UserEntity admin = validateAdmin(token);
        if (admin == null) {
            log.warn("grant failed, invalid token or user is not admin");
            return false;
        } else {
            UserEntity user = systemService.findUserByUsername(username);
            if (user == null) {
                log.warn("no such user: {}", username);
                return false;
            } else {
                if (user.getAdmin()) {
                    log.warn("user {} is admin already", username);
                    return false;
                } else {
                    user.setAdmin(true);
                    userRepo.save(user);
                    log.info("user {} grant admin to user {}", admin.getUsername(), username);
                    return true;
                }
            }
        }
    }

    @Override
    public Boolean banUser(String token, String username, Integer day) {
        UserEntity admin = validateAdmin(token);
        if (admin == null) {
            log.warn("ban failed, invalid token or user is not admin");
            return false;
        } else {
            UserEntity user = systemService.findUserByUsername(username);
            if (user == null) {
                log.warn("no such user {}", username);
                return false;
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DATE, day);

                user.setBanUntil(calendar.getTime());
                userRepo.save(user);

                log.info("admin {} banned user {} for {} days.", admin.getUsername(), username, day);
                return true;
            }
        }
    }

    @Nullable
    @Override
    public List<ProductSummary> getCensoringProducts(String token) {
        UserEntity admin = validateAdmin(token);
        if (admin == null) {
            log.warn("get failed, invalid token or user is not admin");
            return null;
        } else {
            List<ProductEntity> productEntityList =
                    productRepo.findByProductStatusEquals(productRepo.PRODUCT_CENSORING, productRepo.sortByReleaseDesc);
            List<ProductSummary> productSummaryList = new ArrayList<>(productEntityList.size());
            for (int i = 0; i < productEntityList.size(); ++i) {
                productSummaryList.set(i, new ProductSummary(productEntityList.get(i)));
            }
            log.info("admin {} get {} censoring products", admin.getUsername(), productSummaryList.size());
            return productSummaryList;
        }
    }

    @Override
    public Boolean CensorProduct(String token, Long productId, Boolean pass) {
        UserEntity admin = validateAdmin(token);
        if (admin == null) {
            log.warn("censor failed, invalid token or user is not admin");
            return false;
        } else {
            Optional<ProductEntity> productOpt = productRepo.findById(productId);
            if (productOpt.isPresent()) {
                ProductEntity product = productOpt.get();
                if (pass) {
                    product.setProductStatus(productRepo.PRODUCT_SELLING);
                } else {
                    product.setProductStatus(productRepo.PRODUCT_NOT_APPROVED);
                }
                productRepo.save(product);
                log.info("admin {} censor product {} pass as {}", admin.getUsername(), productId, pass);
                return true;
            } else {
                log.warn("no such product {}", productId);
                return false;
            }
        }
    }

    @Override
    public Boolean publishBulletin(String token, String bulletinContent) {
        UserEntity admin = validateAdmin(token);
        if (admin == null) {
            log.warn("publish bulletin failed, invalid token or user is not admin");
            return false;
        } else {
            BulletinMsgEntity bulletinMsg = new BulletinMsgEntity();
            bulletinMsg.setContent(bulletinContent);
            bulletinMsg.setPublishTime(new Date());
            bulletinMsg.setUser(admin);
            bulletinMsgRepo.save(bulletinMsg);
            log.info("admin {} publish bulletin: {}", admin.getUsername(), bulletinContent);
            return true;
        }
    }
}
