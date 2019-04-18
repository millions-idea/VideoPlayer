/***
 * @pName Admin
 * @name MessageApiController
 * @user HongWei
 * @date 2019/3/11
 * @desc
 */
package com.management.admin.apiController;

import com.management.admin.biz.IPushMessageService;
import com.management.admin.entity.db.PushMessage;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.entity.template.SessionModel;
import com.management.admin.utils.web.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageApiController {
    @Autowired
    private IPushMessageService pushMessageService;

    @GetMapping("/getMessage")
    public JsonArrayResult<PushMessage> getMessage(HttpServletRequest request){
        SessionModel session = null;
        try{
            session = SessionUtil.getSession(request);
        }catch (Exception e){

        }
        if(session != null){
            List<PushMessage> messages = pushMessageService.getMessages(session.getUserId());
            if (messages.size() > 0){
                pushMessageService.signalList(messages,session.getUserId());
            }
            return new JsonArrayResult<PushMessage>(messages);
        }
        return JsonArrayResult.failing();
    }


}
