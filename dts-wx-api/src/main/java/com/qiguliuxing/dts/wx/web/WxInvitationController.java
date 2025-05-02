package com.qiguliuxing.dts.wx.web;

import com.qiguliuxing.dts.core.util.ResponseUtil;
import com.qiguliuxing.dts.db.service.DtsUserService;
import com.qiguliuxing.dts.vo.InvitationRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/invitation")
@Validated
public class WxInvitationController {

    @Autowired
    DtsUserService userService;

    @GetMapping("/getInvitationRecords")
    public Object getInvitationRecords(String userId) {
        List<InvitationRecordVO> invitationRecords = userService.getInvitationRecords(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("items", invitationRecords);
        return ResponseUtil.ok(result);
    }


}
