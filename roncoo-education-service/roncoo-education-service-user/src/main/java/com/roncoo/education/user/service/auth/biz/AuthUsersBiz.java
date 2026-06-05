package com.roncoo.education.user.service.auth.biz;

import cn.hutool.core.util.ObjectUtil;
import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.StatusIdEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.common.base.BaseWxBiz;
import com.roncoo.education.system.feign.interfaces.IFeignSysConfig;
import com.roncoo.education.system.feign.interfaces.vo.LoginConfig;
import com.roncoo.education.user.dao.UsersAccountDao;
import com.roncoo.education.user.dao.UsersDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Users;
import com.roncoo.education.user.dao.impl.mapper.entity.UsersAccount;
import com.roncoo.education.user.service.auth.req.AuthBindingReq;
import com.roncoo.education.user.service.auth.req.AuthUsersReq;
import com.roncoo.education.user.service.auth.resp.AuthUsersResp;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.validation.constraints.NotNull;

/**
 * AUTH-用户信息
 *
 * @author wujing
 */
@Component
@RequiredArgsConstructor
public class AuthUsersBiz extends BaseBiz {

    @NotNull
    private final UsersDao dao;
    @NotNull
    private final UsersAccountDao usersAccountDao;
    @NotNull
    private final IFeignSysConfig feignSysConfig;
    @NotNull
    private final BaseWxBiz baseWxBiz;

    public Result<AuthUsersResp> view() {
        Users users = dao.getById(ThreadContext.userId());
        if (users != null && users.getStatusId().equals(StatusIdEnum.YES.getCode())) {
            AuthUsersResp resp = BeanUtil.copyProperties(users, AuthUsersResp.class);
            UsersAccount usersAccount = usersAccountDao.getByUserId(users.getId());
            if (usersAccount != null) {
                resp.setAvailableAmount(usersAccount.getAvailableAmount());
                resp.setFreezeAmount(usersAccount.getFreezeAmount());
            }
            return Result.success(resp);
        }
        return Result.error("用户不存在或被禁用");
    }

    public Result<String> update(AuthUsersReq req) {
        Users users = BeanUtil.copyProperties(req, Users.class);
        users.setId(ThreadContext.userId());
        int result = dao.updateById(users);
        if (result > 0) {
            return Result.success("操作成功");
        }
        return Result.error("操作失败");
    }

    /** 仅更新当前用户的头像 (userHead 字段), 不触发完整资料校验. */
    public Result<String> updateAvatar(String url) {
        Users update = new Users();
        update.setId(ThreadContext.userId());
        update.setUserHead(url);
        int result = dao.updateById(update);
        return result > 0 ? Result.success("头像已更新") : Result.error("更新失败");
    }

    /**
     * 已登录用户用原密码修改为新密码。
     * 密码为 SHA1(mobileSalt + 明文)，校验原密码后用同一个盐重算新密码（保持盐不变，避免影响依赖盐的找回逻辑）。
     */
    public Result<String> changePassword(java.util.Map<String, Object> req) {
        if (req == null) {
            return Result.error("参数不能为空");
        }
        Object oldP = req.get("oldPassword");
        Object newP = req.get("newPassword");
        String oldPwd = oldP == null ? "" : oldP.toString();
        String newPwd = newP == null ? "" : newP.toString();
        if (!StringUtils.hasText(oldPwd) || !StringUtils.hasText(newPwd)) {
            return Result.error("原密码和新密码不能为空");
        }
        if (newPwd.length() < 6 || newPwd.length() > 20) {
            return Result.error("新密码长度需为 6-20 位");
        }
        Users user = dao.getById(ThreadContext.userId());
        if (user == null || !StatusIdEnum.YES.getCode().equals(user.getStatusId())) {
            return Result.error("用户不存在或被禁用");
        }
        if (!StringUtils.hasText(user.getMobilePsw()) || !StringUtils.hasText(user.getMobileSalt())) {
            return Result.error("您还未设置登录密码，请用短信验证码登录后再设置");
        }
        // 校验原密码 (与登录同一套 SHA1(salt + 明文) 比对)
        if (!cn.hutool.crypto.digest.DigestUtil.sha1Hex(user.getMobileSalt() + oldPwd).equals(user.getMobilePsw())) {
            return Result.error("原密码不正确");
        }
        if (newPwd.equals(oldPwd)) {
            return Result.error("新密码不能与原密码相同");
        }
        Users update = new Users();
        update.setId(user.getId());
        update.setMobilePsw(cn.hutool.crypto.digest.DigestUtil.sha1Hex(user.getMobileSalt() + newPwd));
        int result = dao.updateById(update);
        return result > 0 ? Result.success("密码修改成功") : Result.error("修改失败, 请重试");
    }

    public Result<String> binding(AuthBindingReq req) throws WxErrorException {
        Users users = dao.getById(ThreadContext.userId());
        if (StringUtils.hasText(users.getUnionId()) || StringUtils.hasText(users.getOpenId())) {
            return Result.error("您已绑定微信，请勿重复绑定");
        }

        // 获取微信用户信息
        LoginConfig loginConfig = feignSysConfig.getLogin();
        WxOAuth2UserInfo userInfo = baseWxBiz.getAuthInfo(loginConfig.getWxPcLoginAppId(), loginConfig.getWxPcLoginAppSecret(), req.getCode());
        Users usersRecord = dao.getByUnionIdOrOpenId(userInfo.getUnionId(), userInfo.getOpenid());
        if (ObjectUtil.isNotNull(usersRecord)) {
            return Result.error("该微信已绑定其他账号，请更换微信重新绑定");
        }

        // 更新用户信息
        Users newUser = new Users();
        newUser.setId(users.getId());
        newUser.setUnionId(userInfo.getUnionId());
        newUser.setOpenId(userInfo.getOpenid());
        dao.updateById(newUser);
        return Result.success("操作成功");
    }

    /**
     * 解绑微信
     *
     * @return
     */
    public Result<String> unbind() {
        Users users = dao.getById(ThreadContext.userId());
        Users newUsers = new Users();
        newUsers.setId(users.getId());
        newUsers.setUnionId("");
        newUsers.setOpenId("");
        dao.updateById(newUsers);
        return Result.success("操作成功");
    }
}
