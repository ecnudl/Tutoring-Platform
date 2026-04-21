package com.roncoo.education.common.pay.impl.manual;

import com.roncoo.education.common.pay.PayFace;
import com.roncoo.education.common.pay.req.*;
import com.roncoo.education.common.pay.resp.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 人工支付占位实现（未对接微信/支付宝前使用）。
 *
 * <p>所有接口均返回 success=false + 提示文案，引导用户联系客服。
 * 接通真实支付后，在 sys_config 选择对应 payPlatform 即可路由到 alipay/wx 实现。
 */
@Slf4j
@Component(value = "manualPay")
public class ManualPayImpl implements PayFace {

    private static final String PENDING_MSG = "在线支付暂未接入，请联系客服完成支付";

    @Override
    public TradeOrderResp tradeOrder(TradeOrderReq req) {
        log.info("[ManualPay] tradeOrder 占位响应: {}", req);
        return new TradeOrderResp().setSuccess(false).setMsg(PENDING_MSG);
    }

    @Override
    public TradeQueryResp tradeQuery(TradeQueryReq req) {
        return new TradeQueryResp().setSuccess(false).setMsg(PENDING_MSG);
    }

    @Override
    public Boolean tradeClose(TradeCloseReq req) {
        log.info("[ManualPay] tradeClose 占位响应: {}", req);
        return true;
    }

    @Override
    public TradeNotifyResp tradeNotify(TradeNotifyReq req) {
        return new TradeNotifyResp().setSuccess(false).setMsg(PENDING_MSG);
    }

    @Override
    public RefundResp refund(RefundReq req) {
        return new RefundResp().setSuccess(false).setMsg(PENDING_MSG);
    }

    @Override
    public RefundQueryResp refundQuery(RefundQueryReq req) {
        return new RefundQueryResp().setSuccess(false).setMsg(PENDING_MSG);
    }
}
