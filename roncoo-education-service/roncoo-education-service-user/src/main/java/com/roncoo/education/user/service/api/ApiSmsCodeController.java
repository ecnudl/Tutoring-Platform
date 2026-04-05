package com.roncoo.education.user.service.api;

import com.roncoo.education.common.core.base.Result;
import com.roncoo.education.user.service.api.biz.ApiSmsCodeBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/api/sms")
@RequiredArgsConstructor
public class ApiSmsCodeController {

    private final ApiSmsCodeBiz biz;

    @PostMapping("/send")
    public Result<String> sendRegisterCode(@RequestBody Map<String, String> req) {
        return biz.sendRegisterCode(req.get("mobile"));
    }

    @PostMapping("/send-login")
    public Result<String> sendLoginCode(@RequestBody Map<String, String> req) {
        return biz.sendLoginCode(req.get("mobile"));
    }

    @PostMapping("/send-reset")
    public Result<String> sendResetCode(@RequestBody Map<String, String> req) {
        return biz.sendResetCode(req.get("mobile"));
    }
}
