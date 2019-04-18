/***
 * @pName Admin
 * @name BootstrapApiController
 * @user HongWei
 * @date 2019/3/7
 * @desc
 */
package com.management.admin.apiController;

import com.google.common.collect.ImmutableMap;
import com.management.admin.biz.ISmsService;
import com.management.admin.biz.IUserService;
import com.management.admin.entity.db.Dictionary;
import com.management.admin.entity.db.User;
import com.management.admin.entity.param.UserParam;
import com.management.admin.entity.resp.Banner;
import com.management.admin.entity.template.*;
import com.management.admin.exception.InfoException;
import com.management.admin.facade.IUserFacadeService;
import com.management.admin.utils.*;
import com.management.admin.utils.http.TencentCosUtil;
import com.management.admin.utils.poster.PosterUtils;
import com.management.admin.utils.web.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bootstrap")
public class BootstrapApiController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserFacadeService userFacadeService;
    @Autowired
    private ISmsService smsService;

    @PostMapping("/login")
    public JsonResult login(HttpServletRequest req, HttpServletResponse resp, @RequestBody UserParam userParam){
        String userIp = RequestUtil.getIp(req);

        User user = userFacadeService.login(userParam.getUsername(), userParam.getPassword(), userIp);
        if(user == null) {
            user = userFacadeService.loginWithPhone(userParam.getUsername(), userParam.getUsername(), userParam.getPassword(), userIp);
            if(user == null) return new JsonResult().failingAsString("账号或密码错误");
        }
        if(user.getPhone() == null) {
            Map<String, String> fields
                    = ImmutableMap.of("account", userParam.getUsername(), "userId", user.getUserId() + "");
            String token = TokenUtil.create(fields);
            return new JsonResult().failingAsString("未绑定手机号:" + token);
        }
        if(user.getIsDelete() != null &&  user.getIsDelete().equals(1)) throw new InfoException("您的账号已被冻结, 如有疑问请联系客服");

        Map<String, String> fields
                = ImmutableMap.of("account", userParam.getUsername(), "userId", user.getUserId() + "");
        String token = TokenUtil.create(fields);

        return new JsonResult().successful(token);
    }

    @PostMapping("/register")
    public JsonResult register(HttpServletRequest request, @RequestBody UserParam userParam){
        if(userParam.getUsername().isEmpty() || userParam.getUsername().length() < 5 || userParam.getUsername().length() > 11) return new JsonResult().failingAsString("用户名格式有误");
        if(userParam.getPassword().isEmpty() || userParam.getPassword().length() < 6 || userParam.getPassword().length() > 128) return new JsonResult().failingAsString("密码格式有误");
        //if(userParam.getSmsCode().isEmpty() || userParam.getSmsCode().length() != 6) return new JsonResult().failingAsString("请输入正确的验证码");
        //if(userParam.getPromotionCode().isEmpty() || userParam.getPromotionCode().length() != 8) return new JsonResult().failingAsString("请输入正确的推广码");

        String userIp = RequestUtil.getIp(request);

        User parentUser = userService.getUserByPromotionCode(userParam.getPromotionCode());
        if((userParam.getPromotionCode() != null  && userParam.getPromotionCode().length() > 0) && parentUser == null) return new JsonResult().normalExceptionAsString("请输入有效的推广码");

        User user = new User();
        user.setAccount(userParam.getUsername());
        user.setPassword(userParam.getPassword());
        user.setIp(userIp);
        user.setAddDate(new Date());
        user.setPromotionCode(RandomUtils.generateUpperString(8));
       if(parentUser != null){
           user.setParentId(parentUser.getUserId());
           user.setPath(parentUser.getUserId() + "," + parentUser.getPath());
       }
        user.setNickName(user.getAccount());
        user.setPhoto(Constant.UserPhoto);
        userFacadeService.registerWithPassword(user, parentUser, userParam.getSmsCode());

        Map<String, String> fields
                = ImmutableMap.of("account", user.getAccount(), "userId", user.getUserId() + "");
        String token = TokenUtil.create(fields);
        return new JsonResult().successful(token);
    }

    @PostMapping("resetPassword")
    public JsonResult resetPassword(HttpServletRequest request,@RequestBody UserParam userParam){
        // 1.参数验证
        if(userParam.getPhone().isEmpty() || userParam.getPhone().length() < 11 || userParam.getPhone().length() > 11) return new JsonResult().failingAsString("手机号码格式有误");
        if(userParam.getPassword().isEmpty() || userParam.getPassword().length() < 6 || userParam.getPassword().length() > 128) return new JsonResult().failingAsString("密码格式有误");
        if(userParam.getSmsCode().isEmpty() || userParam.getSmsCode().length() != 6) return new JsonResult().failingAsString("请输入正确的验证码");

        // 2.更新数据库
        SessionModel session = SessionUtil.getSession(request);
        User profile = userService.getUserById(session.getUserId());
        Boolean result = userService.resetPassword(profile.getAccount(), userParam.getPhone(), userParam.getPassword(), userParam.getSmsCode());

        // 3.返回结果
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    /**
     * 发送验证码 DF 2018年11月27日00:45:07
     * @param phone 手机号码 15000000000 必填
     * @return 6位纯数字
     */
    @GetMapping("sendSmsCode")
    public JsonResult sendSmsCode(String phone){
        boolean result = smsService.sendRegCode(phone);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    /**
     * 发送找回密码短信验证码 DF 2018年12月7日02:05:25
     * @param phone 手机号码 15000000000 必填
     * @return 6位纯数字
     */
    @GetMapping("sendResetPwdSmsCode")
    public JsonResult sendResetPwdSmsCode(String phone){
        boolean result = smsService.sendResetPwdSmsCode(phone);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }


    @PostMapping("/bind")
    public JsonResult bind(HttpServletRequest request, @RequestBody UserParam userParam){
        if(userParam.getPhone().isEmpty() || userParam.getPhone().length() < 11 || userParam.getPhone().length() > 11) return new JsonResult().failingAsString("手机号码格式有误");
        if(userParam.getSmsCode().isEmpty() || userParam.getSmsCode().length() != 6) return new JsonResult().failingAsString("请输入正确的验证码");

        SessionModel session = SessionUtil.getSession(request);

        User user = new User();
        user.setUserId(session.getUserId());
        user.setPhone(userParam.getPhone());
        user.setEditDate(new Date());
        user.setPromotionCode(RandomUtils.generateUpperString(8));
        userFacadeService.bind(user, null, userParam.getSmsCode());

        User profile = userService.getUserById(session.getUserId());

        Map<String, String> fields
                = ImmutableMap.of("account", profile.getAccount(), "userId", profile.getUserId() + "");
        String token = TokenUtil.create(fields);

        return new JsonResult().successful(token);
    }

    private void createPromotionPoster(User user) {
        try {
            String qrcode =  QRCodeUtil.encodeBase64(Constant.WebUrl + "pages/user/bootstrap/register?pcode=" + user.getPromotionCode(), null);
            String url = TencentCosUtil.upload(qrcode);

            String backUrl =  DataDictionary.Map.get("poster.background.url").getValue();
            String nickName =  user.getNickName();
            String posterBase64 = PosterUtils.createPromotionPoster(backUrl, url , nickName);
            url = TencentCosUtil.upload(posterBase64);
            userService.changePoster(user.getUserId(), url);
        } catch (Exception e) {

        }
    }


    @GetMapping("/getBannerList")
    public JsonArrayResult<Banner> getBannerList(){
        List<Banner> bannerList = new ArrayList<>();
        bannerList.add(new Banner(DataDictionary.Map.get("link.img.homeBanner1").getValue(), DataDictionary.Map.get("link.text.homeBanner1").getValue()));
        bannerList.add(new Banner(DataDictionary.Map.get("link.img.homeBanner2").getValue(), DataDictionary.Map.get("link.text.homeBanner2").getValue()));
        bannerList.add(new Banner(DataDictionary.Map.get("link.img.homeBanner3").getValue(), DataDictionary.Map.get("link.text.homeBanner3").getValue()));
        return new JsonArrayResult<>(bannerList);
    }
}
