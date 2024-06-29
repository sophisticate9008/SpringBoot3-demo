package com.wzy.demo.realm;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzy.demo.common.ActiverUser;

import com.wzy.demo.common.JwtToken;
import com.wzy.demo.common.JwtUtils;
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
public class JwtRealm extends AuthorizingRealm {

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
    public boolean supports(AuthenticationToken token) {
        // 仅支持 JwtToken 类型的 Token
        return token instanceof JwtToken;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = (String) jwtToken.getPrincipal();
        // 解析 JWT Token，验证有效性
        Jws<Claims> claimsJws = JwtUtils.parseClaim(token);
        Claims payLoad = claimsJws.getPayload();
        String account = (String)payLoad.get("account"); // 获取用户名
        // 根据用户名查询用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        // 构建 ActiverUser 对象
        ActiverUser activerUser = new ActiverUser();
        activerUser.setUser(user);

        List<String> roles = new ArrayList<>();
        List<String> permissions = new ArrayList<>();

        List<Integer> roleIds = userRoleService.list(new QueryWrapper<UserRole>().eq("user_id", user.getId()))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());

        if (roleIds.size() > 0) {
            roles = roleService.list(new QueryWrapper<Role>().in("id", roleIds))
                    .stream().map(Role::getRoleName).collect(Collectors.toList());

            List<Integer> permissionIds = rolePermissionService
                    .list(new QueryWrapper<RolePermission>().in("role_id", roleIds))
                    .stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

            if (permissionIds.size() > 0) {
                permissions = permissionService.list(new QueryWrapper<Permission>().in("id", permissionIds))
                        .stream().map(Permission::getPermissionName).collect(Collectors.toList());
            }
        }

        activerUser.setRoles(roles);
        activerUser.setPermissions(permissions);

        return new SimpleAuthenticationInfo(activerUser, token, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        ActiverUser activerUser = (ActiverUser) principalCollection.getPrimaryPrincipal();
        authorizationInfo.addRoles(activerUser.getRoles());
        authorizationInfo.addStringPermissions(activerUser.getPermissions());
        return authorizationInfo;
    }
}
