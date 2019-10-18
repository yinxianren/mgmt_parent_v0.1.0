package com.rxh.service;

import com.rxh.pojo.merchant.MerchantPrivileges;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.pojo.merchant.MerchantUser;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/7/3
 * Time: 14:19
 * Project: Management
 * Package: com.rxh.service
 */
public interface MerchantUserService {
    Boolean isUserExist(MerchantUser merchantUser);

    Boolean addUser(MerchantUser merchantUser, String userName);

    Boolean deleteUserByUserNameAndBelongTo(String userName, String belongTo);

    Boolean updateUserById(MerchantUser merchantUser, String userName);

    Boolean updateUserByUserNameAndBelongTo(MerchantUser merchantUser, String userName);

    List<MerchantUser> getUserByMerchantId(String merchantId);

    List<MerchantRole> getRoleByMerchantId(String merchantId);

    MerchantUser selectUserById(Long userId);

    MerchantUser createUser(MerchantUser user, String userName);

    MerchantUser getUserAndRoleAndPrivilege(String username, String merchantId);

    Boolean updateUserByPrimaryKey(MerchantUser user, String userName);

    Boolean isRoleExist(MerchantRole merchantRole);

    Boolean addRole(MerchantRole role, String userName, MerchantUser merchantUser);

    Boolean addAdminRole(String merchantId, String userName, MerchantUser merchantUser);

    Boolean updateRoleByRoleNameAndBelongTo(MerchantRole merchantRole, String userName);

    Boolean deleteRoleByRoleNameAndBelongTo(String roleName, String belongTo);

    Boolean deleteUserByIdList(List<Long> idList);

    List<MerchantPrivileges> selectAllPrivileges();

    List<MerchantPrivileges> getMenu(String userName, String merchantId);

    String getUserIdByMerchantId(String merchantId);

    Boolean checkUserIp(String ip,String customerId);
}
