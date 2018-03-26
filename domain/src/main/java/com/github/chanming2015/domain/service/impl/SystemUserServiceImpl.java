package com.github.chanming2015.domain.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.github.chanming2015.common.util.result.PageObject;
import com.github.chanming2015.common.util.result.Pager;
import com.github.chanming2015.common.util.result.Result;
import com.github.chanming2015.domain.entity.SystemUser;
import com.github.chanming2015.domain.entity.SystemUserRole;
import com.github.chanming2015.domain.repository.SystemUserRepository;
import com.github.chanming2015.domain.repository.SystemUserRoleRepository;
import com.github.chanming2015.domain.service.PasswordService;
import com.github.chanming2015.domain.service.QueuingService;
import com.github.chanming2015.domain.service.SystemUserService;
import com.github.chanming2015.domain.sql.SpecParam;
import com.github.chanming2015.domain.sql.SpecUtil;

@Service("systemUserService")
public class SystemUserServiceImpl implements SystemUserService
{

    private static final String QUEUING_MAKE_SRC = "01";

    @Autowired
    private SystemUserRepository systemUserRepository;
    @Autowired
    private SystemUserRoleRepository systemUserRoleRepository;
    @Autowired
    private QueuingService queuingService;
    @Autowired
    private PasswordService passwordService;

    @Override
    public Result<Pager<SystemUser>> pageSystemUsers(SpecParam<SystemUser> specs,
            Pager<SystemUser> pager)
    {
        Result<Pager<SystemUser>> result;
        // 参数校验
        if (specs == null || pager == null)
        {
            result = Result.newFailure("CMS1291", "");
            return result;
        }
        if (pager.getPageSize() <= 0)
        {
            result = Result.newFailure("CMS1292", "");
            return result;
        }
        // 准备分页参数
        Pageable pageable = pager.getDirection() == null || pager.getProperties() == null ? new PageRequest(
                pager.getCurrentPage() - 1, pager.getPageSize()) : new PageRequest(
                pager.getCurrentPage() - 1, pager.getPageSize(), Direction.valueOf(pager
                        .getDirection()), pager.getProperties().split(","));
        // 准备查询参数
        specs.eq("deleted", false); // 未删除
        // 执行查询
        Page<SystemUser> resultSet = null;
        try
        {
            resultSet = systemUserRepository.findAll(SpecUtil.spec(specs), pageable);
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0151", "");
            return result;
        }

        // 返回结果
        PageObject<SystemUser> pageObject = new PageObject<SystemUser>(
                (int) resultSet.getTotalElements(), resultSet.getContent());
        pager.setElements(pageObject.getList());
        pager.init(pageObject.getTotal());
        result = Result.newSuccess(pager);
        return result;
    }

    @Override
    public Result<SystemUser> getSystemUser(Long id)
    {
        Result<SystemUser> result;
        // 参数校验
        if (id == null)
        {
            result = Result.newFailure("CMS1041", "");
            return result;
        }

        // 执行查询
        SystemUser systemUser = null;
        try
        {
            systemUser = systemUserRepository.findOne(id);
            if (systemUser != null)
            {
                setUserRoleIds(systemUser);
            }
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0021", "");
            return result;
        }

        // 返回结果
        result = Result.newSuccess(systemUser);
        return result;
    }

    @Override
    public Result<SystemUser> getSystemUser(String loginName)
    {
        Result<SystemUser> result;
        // 参数校验
        if (loginName == null)
        {
            result = Result.newFailure("CMS1041", "");
            return result;
        }

        // 执行查询
        SystemUser systemUser = null;
        try
        {
            systemUser = systemUserRepository.findByLoginNameAndDeleted(loginName, false);
            if (systemUser != null)
            {
                setUserRoleIds(systemUser);
            }
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0021", "");
            return result;
        }

        // 返回结果
        result = Result.newSuccess(systemUser);
        return result;
    }

    /**
     * @author XuMaoSen
     * Create Time:2015年12月8日 下午7:45:46
     * Description
     * @param systemUser
     */
    private void setUserRoleIds(SystemUser systemUser)
    {
        List<SystemUserRole> relations = systemUserRoleRepository.findByUserId(systemUser.getId());
        if (relations != null)
        {
            Set<Long> roleIds = new HashSet<Long>();
            for (SystemUserRole relation : relations)
            {
                roleIds.add(relation.getRoleId());
            }
            systemUser.setRoleIds(roleIds);
        }
    }

    @Override
    public Result<SystemUser> createSystemUser(SystemUser systemUser)
    {
        Result<SystemUser> result;
        // 校验参数
        if (systemUser == null)
        {
            result = Result.newFailure("CMS1311", "");
            return result;
        }
        // 准备序列号
        Result<Long> systemUserQueuing = queuingService.next(QUEUING_MAKE_SRC);
        if (systemUserQueuing.getError() != null)
        {
            return Result.newFailure("CMS0032", "");
        }
        // 准备盐和密码
        systemUser.setSalt(passwordService.generateSalt());
        systemUser.setPassword(passwordService.encryptPassword(systemUser.getPassword(),
                systemUser.getLoginName(), systemUser.getSalt()));
        // 持久对象
        SystemUser systemUserSaved = null;
        try
        {
            systemUser.setId(systemUserQueuing.getObject());
            systemUserSaved = systemUserRepository.save(systemUser);
            if (systemUser.getRoleIds() != null)
            {
                for (Long roleId : systemUser.getRoleIds())
                {
                    SystemUserRole relation = new SystemUserRole();
                    relation.setId(queuingService.next(QUEUING_MAKE_SRC).getObject());
                    relation.setUserId(systemUser.getId());
                    relation.setRoleId(roleId);
                    systemUserRoleRepository.save(relation);
                }
            }
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0171", "");
            return result;
        }
        // 返回结果
        result = Result.newSuccess(systemUserSaved);
        return result;
    }

    @Override
    public Result<SystemUser> updateSystemUser(SystemUser systemUser)
    {
        Result<SystemUser> result;
        // 校验参数
        if (systemUser == null)
        {
            result = Result.newFailure("CMS1081", "");
            return result;
        }
        if (systemUser.getId() == null)
        {
            result = Result.newFailure("CMS1082", "");
            return result;
        }
        SystemUser systemUserFetched;
        try
        {
            // 准备对象
            systemUserFetched = systemUserRepository.findOne(systemUser.getId());
            systemUserFetched.setLoginName(systemUser.getLoginName() == null ? systemUserFetched
                    .getLoginName() : systemUser.getLoginName());
            systemUserFetched.setRealName(systemUser.getRealName() == null ? systemUserFetched
                    .getRealName() : systemUser.getRealName());
            systemUserFetched.setMobile(systemUser.getMobile() == null ? systemUserFetched
                    .getMobile() : systemUser.getMobile());
            systemUserFetched.setSalt(systemUser.getSalt() == null ? systemUserFetched.getSalt()
                    : systemUser.getSalt());
            systemUserFetched.setMemo(systemUser.getMemo() == null ? systemUserFetched.getMemo()
                    : systemUser.getMemo());
            systemUserFetched.setStatus(systemUser.getStatus() == null ? systemUserFetched
                    .getStatus() : systemUser.getStatus());
            if (systemUser.getPassword() != null && !"".equals(systemUser.getPassword()))
            {
                systemUserFetched.setSalt(passwordService.generateSalt());
                systemUserFetched.setPassword(passwordService.encryptPassword(
                        systemUser.getPassword(), systemUserFetched.getLoginName(),
                        systemUserFetched.getSalt()));
            }
            // 持久对象
            systemUserRepository.save(systemUserFetched);

            // 更改角色
            if (systemUser.getRoleIds() != null)
            {
                if (systemUser.getRoleIds().size() > 0)
                {
                    // 删除旧记录
                    List<SystemUserRole> toDeleteList = systemUserRoleRepository
                            .findByUserId(systemUser.getId());
                    if (toDeleteList != null)
                    {
                        for (SystemUserRole relation : toDeleteList)
                        {
                            systemUserRoleRepository.delete(relation);
                        }
                    }
                    // 准备并保存新纪录
                    List<SystemUserRole> toSaveList = new ArrayList<SystemUserRole>();
                    for (Long rid : systemUser.getRoleIds())
                    {
                        SystemUserRole relation = new SystemUserRole();
                        relation.setId(queuingService.next(QUEUING_MAKE_SRC).getObject());
                        relation.setUserId(systemUser.getId());
                        relation.setRoleId(rid);
                        toSaveList.add(relation);
                    }
                    systemUserRoleRepository.save(toSaveList);
                }
            }
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0041", "");
            return result;
        }
        // 返回结果
        result = Result.newSuccess(systemUserFetched);
        return result;
    }

    @Override
    public Result<Boolean> deleteSystemUser(Long id)
    {
        Result<Boolean> result;
        // 参数校验
        if (id == null)
        {
            result = Result.newFailure("CMS1331", "");
            return result;
        }
        // 准备对象
        SystemUser systemUser = systemUserRepository.findOne(id);
        if (systemUser != null)
        {
            systemUser.setDeleted(true);
        }
        // 持久化操作
        try
        {
            systemUserRepository.save(systemUser);
        }
        catch (Exception e)
        {
            result = Result.newFailure("CMS0191", "");
            return result;
        }
        // 返回结果
        result = Result.newSuccess(true);
        return result;
    }
}
