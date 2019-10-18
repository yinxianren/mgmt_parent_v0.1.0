package com.rxh.service.impl;

import com.rxh.mapper.merchant.AgmentPrivilegesMapper;
import com.rxh.mapper.merchant.AgmentSquareRoleMapper;
import com.rxh.mapper.merchant.AgmentUserMapper;
import com.rxh.mapper.square.UserLoginIpMapper;
import com.rxh.pojo.merchant.MerchantPrivileges;
import com.rxh.pojo.merchant.MerchantRole;
import com.rxh.pojo.merchant.MerchantUser;
import com.rxh.service.AgmentUserService;
import com.rxh.utils.StringUtils;
import com.rxh.utils.SystemConstant;
import com.rxh.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/7/3
 * Time: 14:19
 * Project: Management
 * Package: com.rxh.service.impl
 */
@Service
public class AgmentUserServiceImp implements AgmentUserService {

    @Resource
    private AgmentUserMapper agmentUserMapper;

    @Resource
    private AgmentSquareRoleMapper agmentSquareRoleMapper;

    @Resource
    private AgmentPrivilegesMapper agmentPrivilegesMapper;

    @Resource
    private UserLoginIpMapper userLoginIpMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Boolean isUserExist(MerchantUser merchantUser) {
        return agmentUserMapper.selectByMerchantUser(merchantUser).size() > 0;
    }

    @Override
    public Boolean addUser(MerchantUser merchantUser, String userName) {
        merchantUser.setId(UUID.createShortKey("merchant_user"));
        merchantUser.setCreator(userName);
        merchantUser.setCreateTime(new Date());
        merchantUser.setAvailable(true);
        return agmentUserMapper.insertSelective(merchantUser) == 1;
    }

    @Override
    public Boolean deleteUserByUserNameAndBelongTo(String userName, String belongTo) {
        return agmentUserMapper.deleteByUserNameAndBelongTo(userName, belongTo) > 0;
    }

    @Override
    public Boolean updateUserById(MerchantUser merchantUser, String userName) {
        merchantUser.setModifier(userName);
        merchantUser.setModifyTime(new Date());
        return agmentUserMapper.updateByPrimaryKeySelective(merchantUser) == 1;
    }

    @Override
    public Boolean updateUserByUserNameAndBelongTo(MerchantUser merchantUser, String userName) {
        merchantUser.setModifier(userName);
        merchantUser.setModifyTime(new Date());
        return agmentUserMapper.updateMerchantUserByUserNameAndBelongTo(merchantUser) == 1;
    }

    @Override
    public List<MerchantUser> getUserByMerchantId(String merchantId) {
        return agmentUserMapper.selectMerchantUserByMerchantId(merchantId);
    }

    @Override
    public List<MerchantRole> getRoleByMerchantId(String merchantId) {
        return agmentSquareRoleMapper.selectByMerchantId(merchantId);
    }

    @Override
    public MerchantUser selectUserById(Long userId) {
        return agmentUserMapper.selectByPrimaryKey(userId);
    }

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 用户信息
     */
    @Override
    public MerchantUser createUser(MerchantUser user, String userName) {
        if (agmentUserMapper.selectByMerchantUser(user).size() == 0) {
            user.setId(UUID.createShortKey("merchant_user"));
            user.setCreator(userName);
            user.setCreateTime(new Date());
            user.setAvailable(true);
            agmentUserMapper.insertSelective(user);
            return user;
        }
        return null;
    }

    /**
     * 获取用户对应角色以及角色对应权限
     *
     * @param username 用户名
     * @return 用户对象包含角色、权限
     */
    @Override
    public MerchantUser getUserAndRoleAndPrivilege(String username, String merchantId) {
        MerchantUser user = agmentUserMapper.selectByUserNameAndBelongTo(username, merchantId);
        if (user == null) {
            return null;
        }
        MerchantRole role = agmentSquareRoleMapper.getRole(user.getRoleId());
        if (StringUtils.isNotBlank(role.getPrivilegesId())) {
            List<Long> roleIdList = new ArrayList<>();
            Pattern r = Pattern.compile("\\d+");
            Matcher m = r.matcher(role.getPrivilegesId());
            while (m.find()) {
                roleIdList.add(Long.valueOf(m.group()));
            }
            role.setPrivileges(agmentPrivilegesMapper.selectUserPrivileges(roleIdList));
        }
        user.setRole(role);
        return user;
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 成功/失败
     */
    @Override
    public Boolean updateUserByPrimaryKey(MerchantUser user, String userName) {
        user.setModifier(userName);
        user.setModifyTime(new Date());
        return agmentUserMapper.updateByPrimaryKeySelective(user) == 1;
    }

    @Override
    public Boolean isRoleExist(MerchantRole merchantRole) {
        return agmentSquareRoleMapper.selectByMerchantRole(merchantRole).size() > 0;
    }

    @Override
    public Boolean addRole(MerchantRole role, String userName, MerchantUser merchantUser) {
        Long roleid = UUID.createKey("agment_role");
        Long userId = UUID.createKey("agment_user");
        role.setId(roleid);
        role.setRole(SystemConstant.ROLE_MERCHANT_USER);
        role.setCreator(userName);
        role.setCreateTime(new Date());
        role.setAvailable(true);
        if(merchantUser!=null){
            merchantUser.setId(userId);
            merchantUser.setRoleId(role.getId());
            merchantUser.setCreator(userName);
            merchantUser.setCreateTime(new Date());
            merchantUser.setAvailable(true);
            agmentUserMapper.insertSelective(merchantUser);
        }

        return agmentSquareRoleMapper.insertSelective(role) == 1;
    }

    @Override
    public Boolean addAdminRole(String merchantId, String userName, MerchantUser merchantUser) {
        MerchantRole merchantRole = new MerchantRole();
        merchantRole.setBelongto(merchantId);
        merchantRole.setRoleName("管理员");
        List<Long> privilegesId = selectAllPrivileges()
                .stream()
                .filter(merchantPrivileges -> merchantPrivileges.getParentId() != null)
                .map(MerchantPrivileges::getId)
                .collect(Collectors.toList());
        merchantRole.setPrivilegesId(privilegesId.toString().replaceAll("[^,0-9]", ""));
        return addRole(merchantRole, userName,merchantUser);
    }

    @Override
    public Boolean updateRoleByRoleNameAndBelongTo(MerchantRole merchantRole, String userName) {
        merchantRole.setModifier(userName);
        merchantRole.setModifyTime(new Date());
        return agmentSquareRoleMapper.updateRoleByRoleNameAndBelongTo(merchantRole) == 1;
    }

    @Override
    public Boolean deleteRoleByRoleNameAndBelongTo(String roleName, String belongTo) {
        return agmentSquareRoleMapper.deleteRoleByRoleNameAndBelongTo(roleName, belongTo) > 0;
    }

    @Override
    public Boolean deleteUserByIdList(List<Long> idList) {
        return agmentUserMapper.deleteByIdList(idList) > 0;
    }

    /**
     * 查找所有权限
     *
     * @return 权限信息
     */
    @Override
    public List<MerchantPrivileges> selectAllPrivileges() {
        return agmentPrivilegesMapper.selectAll();
    }

    @Override
    public List<MerchantPrivileges> getMenu(String userName, String merchantId) {
        MerchantUser user = getUserAndRoleAndPrivilege(userName, merchantId);
        List<MerchantPrivileges> privilegesList = user.getRole().getPrivileges();
        if (privilegesList != null) {
            List<MerchantPrivileges> menuList = privilegesList
                    .stream()
                    .filter(sysPrivileges -> sysPrivileges.getParentId() == null)
                    .collect(Collectors.toList());
            List<MerchantPrivileges> submenuList = privilegesList
                    .stream()
                    .filter(sysPrivileges -> sysPrivileges.getParentId() != null)
                    .collect(Collectors.toList());
            Map<Long, List<MerchantPrivileges>> submenuMap = submenuList
                    .stream()
                    .collect(Collectors.groupingBy(MerchantPrivileges::getParentId));
            menuList.forEach(sysPrivileges -> sysPrivileges.setSubmenu(submenuMap.get(sysPrivileges.getId())));
            menuList = menuList
                    .stream()
                    .filter(sysPrivileges -> sysPrivileges.getSubmenu() != null)
                    .collect(Collectors.toList());
            return menuList;
        }
        return null;
    }

    @Override
    public Boolean checkUserIp(String ip, String merchantId) {
        List<String> list = userLoginIpMapper.searchByCustomerId(merchantId);
        for (String s : list) {
            if (s.equals(ip)){
                return true;
            }
        }
        return false;
    }
}
