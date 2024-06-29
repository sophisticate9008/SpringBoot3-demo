package com.wzy.demo.realm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzy.demo.common.ActiverUser;
import com.wzy.demo.common.Constast;
import com.wzy.demo.entity.Permission;
import com.wzy.demo.entity.Role;
import com.wzy.demo.entity.RolePermission;
import com.wzy.demo.entity.User;
import com.wzy.demo.entity.UserRole;
import com.wzy.demo.service.PermissionService;
import com.wzy.demo.service.RolePermissionService;
import com.wzy.demo.service.RoleService;
import com.wzy.demo.service.UserRoleService;
import com.wzy.demo.service.UserService;

@Component
public class UserRealm extends AuthorizingRealm {


    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public String getName(){
        return this.getClass().getSimpleName();
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",authenticationToken.getPrincipal().toString());
        //通过用户名从数据库中查询出该用户
        User user = userService.getOne(queryWrapper);
        SecurityUtils.getSubject().getSession().setAttribute("user", user);
        if (null!=user){
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(user);
            
            List<String> roles = new ArrayList<>();
            List<String> permissions = new ArrayList<>();

            List<Integer> roleIds = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", user.getId()))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
        // 查询角色对应的权限信息并转换为权限ID列表
            if(roleIds.size() > 0) {
                roles = roleService.list(new QueryWrapper<Role>().in("id", roleIds))
                    .stream().map(Role::getRoleName).collect(Collectors.toList());
                List<Integer> permissionIds = rolePermissionService.list(new QueryWrapper<RolePermission>().in("role_id", roleIds))
                    .stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
                if(permissionIds.size() > 0) {
                    permissions = permissionService.list(new QueryWrapper<Permission>().in("id", permissionIds))
                        .stream().map(Permission::getPermissionName).collect(Collectors.toList());                    
                }
            }
            activerUser.setRoles(roles);
            activerUser.setPermissions(permissions);
            //生成盐
            
            ByteSource credentialsSalt=ByteSource.Util.bytes(user.getSalt());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser,user.getPassword(),credentialsSalt,this.getName());
            return info;
        }
        return null;
    }

    // 用户授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        ActiverUser activerUser = (ActiverUser) principalCollection.getPrimaryPrincipal();
        User user = activerUser.getUser();
        authorizationInfo.addRoles(activerUser.getRoles());
        List<String> superPermission = new ArrayList<>();
        superPermission.add("*:*");
        List<String> permissions = activerUser.getPermissions();
        if (user.getTheType().equals(Constast.USER_TYPE_SUPER)){
            authorizationInfo.addStringPermissions(superPermission);

        }else {
            if (null!=permissions&&permissions.size()>0){
                authorizationInfo.addStringPermissions(permissions);
            }
        }

        return authorizationInfo;
    }
    @Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		// 构建凭证匹配对象
		HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher();
		// 设置加密算法  Matcher
		cMatcher.setHashAlgorithmName(Constast.AlgorithmName);
		// 设置加密次数
		cMatcher.setHashIterations(Constast.HASHITERATIONS);
		super.setCredentialsMatcher(cMatcher);
	}



}
