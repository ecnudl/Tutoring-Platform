package com.roncoo.education.user.service.auth.biz;

import com.roncoo.education.common.base.ThreadContext;
import com.roncoo.education.common.base.page.Page;
import com.roncoo.education.common.base.page.PageUtil;
import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.common.core.enums.ReadEnum;
import com.roncoo.education.common.core.enums.StatusIdEnum;
import com.roncoo.education.common.tools.BeanUtil;
import com.roncoo.education.common.base.BaseBiz;
import com.roncoo.education.user.dao.MsgDao;
import com.roncoo.education.user.dao.MsgUserDao;
import com.roncoo.education.user.dao.impl.mapper.entity.Msg;
import com.roncoo.education.user.dao.impl.mapper.entity.MsgUser;
import com.roncoo.education.user.dao.impl.mapper.entity.MsgUserExample;
import com.roncoo.education.user.service.auth.req.AuthMsgUserGetReq;
import com.roncoo.education.user.service.auth.req.AuthMsgUserPageReq;
import com.roncoo.education.user.service.auth.resp.AuthMsgResp;
import com.roncoo.education.user.service.auth.resp.AuthMsgUserResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthMsgUserBiz extends BaseBiz {

    @NotNull
    private final MsgUserDao dao;
    @NotNull
    private final MsgDao msgDao;

    public Result<Page<AuthMsgUserResp>> list(AuthMsgUserPageReq req) {
        MsgUserExample example = new MsgUserExample();
        MsgUserExample.Criteria c = example.createCriteria();
        c.andUserIdEqualTo(ThreadContext.userId());
        c.andStatusIdEqualTo(StatusIdEnum.YES.getCode());
        if (req.getIsRead() != null) {
            c.andIsReadEqualTo(req.getIsRead());
        }
        example.setOrderByClause("id desc");
        Page<MsgUser> page = dao.page(req.getPageCurrent(), req.getPageSize(), example);

        List<AuthMsgUserResp> items = new ArrayList<>();
        if (page.getList() != null) {
            for (MsgUser mu : page.getList()) {
                AuthMsgUserResp resp = BeanUtil.copyProperties(mu, AuthMsgUserResp.class);
                Msg msg = msgDao.getById(mu.getMsgId());
                if (msg != null) {
                    resp.setMsgTitle(msg.getMsgTitle());
                    resp.setMsgText(msg.getMsgText());
                    resp.setMsgType(msg.getMsgType());
                }
                items.add(resp);
            }
        }
        Page<AuthMsgUserResp> out = new Page<>(page.getTotalCount(), page.getTotalPage(),
                page.getPageCurrent(), page.getPageSize(), items);
        return Result.success(out);
    }

    public Result<AuthMsgResp> readMsg(AuthMsgUserGetReq req) {
        if (req.getId() == null) {
            return Result.error("id不能为空");
        }
        MsgUser record = dao.getById(req.getId());
        if (record == null) {
            return Result.error("找不到站内信息");
        }
        if (!record.getUserId().equals(ThreadContext.userId())) {
            return Result.error("没权限读该信息");
        }

        if (ReadEnum.NO.getCode().equals(record.getIsRead())) {
            record.setIsRead(ReadEnum.READ.getCode());
            dao.updateById(record);
        }

        Msg msg = msgDao.getById(record.getMsgId());
        if (msg == null) {
            return Result.error("查询msg有误");
        }
        return Result.success(BeanUtil.copyProperties(msg, AuthMsgResp.class));
    }
}
