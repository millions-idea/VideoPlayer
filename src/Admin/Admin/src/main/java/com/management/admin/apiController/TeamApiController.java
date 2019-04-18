/***
 * @pName Admin
 * @name TeamApiController
 * @user HongWei
 * @date 2019/3/8
 * @desc
 */
package com.management.admin.apiController;

import com.management.admin.biz.IUserService;
import com.management.admin.entity.resp.MemberView;
import com.management.admin.entity.resp.TeamView;
import com.management.admin.entity.template.JsonArrayResult;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.entity.template.SessionModel;
import com.management.admin.utils.web.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/team")
public class TeamApiController {
    @Autowired
    private IUserService userService;

    @GetMapping("/getTeamPreview")
    public JsonResult<TeamView> getTeamPreview(HttpServletRequest request){
        SessionModel session = SessionUtil.getSession(request);
        Long userId = session.getUserId();
        TeamView teamView = userService.getTeamPreview(userId);
        if(teamView != null) return new JsonResult<>().successful(teamView);
        return JsonResult.failing();
    }

    @GetMapping("/getTeamMembers")
    public JsonArrayResult<List<MemberView>> getTeamMembers(HttpServletRequest request, @RequestParam(required = false) Integer page, @RequestParam(required = false) String phone){
        SessionModel session = SessionUtil.getSession(request);
        List<MemberView> members = userService.getPromoteMembersLimit(session.getUserId(), page, phone);
        Integer maxPage = userService.getPromoteMembersTotalPage(session.getUserId());
        List<List<MemberView>> membersWrapper = new ArrayList<>();
        membersWrapper.add(members);
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, membersWrapper);
        jsonArrayResult.setCount(maxPage == null ? 1000 : maxPage);
        return jsonArrayResult;
    }
}
