/***
 * @pName Admin
 * @name UserApiController
 * @user HongWei
 * @date 2019/3/8
 * @desc
 */
package com.management.admin.apiController;

import com.google.common.collect.ImmutableMap;
import com.management.admin.biz.*;
import com.management.admin.entity.db.Order;
import com.management.admin.entity.db.User;
import com.management.admin.entity.dbExt.CollectDetailInfo;
import com.management.admin.entity.param.UserParam;
import com.management.admin.entity.resp.*;
import com.management.admin.entity.template.*;
import com.management.admin.exception.InfoException;
import com.management.admin.utils.Base64Util;
import com.management.admin.utils.PropertyUtil;
import com.management.admin.utils.QRCodeUtil;
import com.management.admin.utils.TokenUtil;
import com.management.admin.utils.http.TencentCosUtil;
import com.management.admin.utils.poster.PosterUtils;
import com.management.admin.utils.web.SessionUtil;
import com.management.admin.utils.yabo.YaBoLoginResp;
import com.management.admin.utils.yabo.YaBoProfileResp;
import com.management.admin.utils.yabo.YaBoResp;
import com.management.admin.utils.yabo.YaBoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICollectService collectService;


    @PostMapping("/getProfile")
    public JsonResult<UserProfileView> getProfile(HttpServletRequest request){
        SessionModel session = SessionUtil.getSession(request);
        Long userId = session.getUserId();
        User user = userService.getUserById(userId);
        if(user == null) return new JsonResult<>().failingAsString("获取用户资料失败");
        if(user.getIsDelete() != null && user.getIsDelete().equals(1)) throw new InfoException("您的账号已被冻结, 如有疑问请联系客服");
        UserProfileView userProfileView = new UserProfileView();
        PropertyUtil.clone(user, userProfileView);
        userProfileView.setAvatar(user.getPhoto());
        userProfileView.setIsVip(user.getLevel() > 0);
        userProfileView.setExitsPayment(user.getPaymentPassword() != null && user.getPaymentPassword().length() > 1);
        User parentUser = userService.getUserById(user.getParentId());
        if(parentUser != null) {
            UserProfileView parentUserProfileView = new UserProfileView();
            parentUserProfileView.setAvatar(parentUser.getPhoto());
            parentUserProfileView.setPhone(parentUser.getPhone());
            parentUserProfileView.setNickName(parentUser.getNickName());
            userProfileView.setParentUser(parentUserProfileView);
        }
        //同步亚博信息
        //查询亚博信息
        /*if(user.getYaboToken() != null){
            YaBoProfileResp profile = YaBoUtil.getProfile(user.getYaboToken(), user.getPhone());
            userProfileView.setYabo(profile);
            userProfileView.setAvatar(profile.getData().getUserprofile());
        }else{
            YaBoProfileResp profile = YaBoUtil.getProfile(user.getYaboToken(), user.getPhone());
            if(profile == null || !profile.getStatus().equals("success")){
                YaBoLoginResp login = YaBoUtil.login(user.getPhone(), user.getYaboPassword());
                if(login != null && login.getStatus().equals("success")){
                    userProfileView.setYabo(profile);
                    userProfileView.setAvatar(profile.getData().getUserprofile());
                    userService.changeYaBoToken(userId, login.getData().getToken());
                }
            }
        }*/
        userProfileView.setYaboUrl(DataDictionary.Map.get("link.yabo").getValue());
        userProfileView.setYaboAppUrl(DataDictionary.Map.get("link.app.yabo").getValue());
        return new JsonResult<>().successful(userProfileView);
    }

    @PostMapping("/uploadAvatar")
    public JsonResult uploadAvatar(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url= TencentCosUtil.upload(file);
        SessionModel session = SessionUtil.getSession(request);
        Long userId = session.getUserId();
        if(!userService.changeAvatar(userId, url)) return new JsonResult().failingAsString("上传头像失败");
        return new JsonResult().successful(url);
    }

    @PostMapping("/editNickName")
    public JsonResult editNickName(HttpServletRequest request, @RequestBody UserParam userParam){
        SessionModel session = SessionUtil.getSession(request);
        User user = userService.getUserById(session.getUserId());
        if(!userService.changeNickName(session.getUserId(), userParam.getNickName())) return JsonResult.failing();
        /*try {
            String qrcode =  QRCodeUtil.encodeBase64(Constant.WebUrl + "pages/user/bootstrap/register?pcode=" + user.getPromotionCode(), null);
            String url = TencentCosUtil.upload(qrcode);

            String backUrl =  DataDictionary.Map.get("poster.background.url").getValue();
            String nickName =  userParam.getNickName();
            String posterBase64 = PosterUtils.createPromotionPoster(backUrl, url , nickName);
            url = TencentCosUtil.upload(posterBase64);
            userService.changePoster(user.getUserId(), url);
        } catch (Exception e) {

        }*/
        return JsonResult.successful();
    }

    @GetMapping("/sendChangePhoneSmsCode")
    public JsonResult sendChangePhoneSmsCode(String phone){
        boolean result = smsService.sendChangePhoneSmsCode(phone);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    @GetMapping("/sendPaymentSmsCode")
    public JsonResult sendPaymentSmsCode(String phone){
        boolean result = smsService.sendPaymentSmsCode(phone);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    @GetMapping("/sendPasswordSmsCode")
    public JsonResult sendPasswordSmsCode(String phone){
        boolean result = smsService.sendPasswordSmsCode(phone);
        if(result) return JsonResult.successful();
        return JsonResult.failing();
    }

    @PostMapping("/changePhone")
    public JsonResult<UserProfileView> changePhone(HttpServletRequest request, @RequestBody UserParam userParam){
        if(userParam.getUsername().isEmpty() || userParam.getUsername().length() < 11 || userParam.getUsername().length() > 11) return new JsonResult().failingAsString("手机号码格式有误");
        if(userParam.getSmsCode().isEmpty() || userParam.getSmsCode().length() != 6) return new JsonResult().failingAsString("请输入正确的验证码");

        SessionModel session = SessionUtil.getSession(request);

        if(!userService.changePhone(session.getUserId(), userParam.getUsername(), userParam.getSmsCode())) return JsonResult.failing();

        UserProfileView userProfileView = new UserProfileView();
        userProfileView.setAccount(userParam.getUsername());

        Map<String, String> fields
                = ImmutableMap.of("account", userProfileView.getAccount(), "userId", session.getUserId() + "");
        String token = TokenUtil.create(fields);

        userProfileView.setToken(token);

        return new JsonResult<>().successful(userProfileView);
    }

    @PostMapping("/changePaymentPassword")
    public JsonResult changePaymentPassword(HttpServletRequest request, @RequestBody UserParam userParam){
        if(userParam.getPassword().isEmpty() || userParam.getPassword().length() != 6) return new JsonResult().failingAsString("请输入正确格式的交易密码");

        SessionModel session = SessionUtil.getSession(request);

        if(!userService.changePaymentPassword(session.getAccount(), session.getUserId(), userParam.getPassword(), userParam.getSmsCode())) return JsonResult.failing();

        return JsonResult.successful();
    }

    @GetMapping("/getAssetSample")
    public JsonResult<UserAssetView> getAssetSample(HttpServletRequest request){
        SessionModel session = SessionUtil.getSession(request);
        Long userId = session.getUserId();
        UserAssetView userAssetView = userService.getAssets(userId);
        return new JsonResult<>().successful(userAssetView);
    }

    @GetMapping("/verifyPaymentPassword")
    public JsonResult verifyPaymentPassword(HttpServletRequest request, String password){
        SessionModel session = SessionUtil.getSession(request);
        boolean result = userService.verifyPaymentPassword(session.getAccount(), session.getUserId(), password);
        if(result) return JsonResult.successful();
        return new JsonResult().failingAsString("支付密码输入错误");
    }

    @GetMapping("/getVipOrderInfo")
    public JsonResult<VipOrder> getVipOrderInfo(){
        VipOrder vipOrder = new VipOrder();
        vipOrder.setTitle(DataDictionary.Map.get("platform.vip.title").getValue());
        vipOrder.setAmount(Double.valueOf(DataDictionary.Map.get("platform.vip.amount").getValue()));
        vipOrder.setId(DataDictionary.Map.get("platform.vip.pid").getValue());
        return new JsonResult<>().successful(vipOrder);
    }

    @GetMapping("/getPromotionPoster")
    public String getPromotionPoster(HttpServletRequest request){
        SessionModel session = SessionUtil.getSession(request);
        User user = userService.getUserById(session.getUserId());
        List<Order> orderList = orderService.getOrderList(user.getUserId());
        if(orderList == null || orderList.size() == 0){
            throw new InfoException("消费后即可生成您的专属推广码");
        }
        //生成推广海报
        if(user.getPromotionPoster() == null){
            try {
                String qrcode =  QRCodeUtil.encodeBase64(Constant.WebUrl + "pages/user/bootstrap/register?pcode=" + user.getPromotionCode(), null);
                String url = TencentCosUtil.upload(qrcode);
                String backUrl =  DataDictionary.Map.get("poster.background.url").getValue();
                String nickName =  user.getNickName();
                String posterBase64 = PosterUtils.createPromotionPoster(backUrl, url , nickName);
                url = TencentCosUtil.upload(posterBase64);
                if(userService.changePoster(session.getUserId(), url)){
                    return "data:image/jpeg;base64," + Base64Util.encodeUrl(url);
                }
            } catch (Exception e) {

            }
        }else{
            return "data:image/jpeg;base64," + Base64Util.encodeUrl(user.getPromotionPoster());
        }
        return null;
    }

    @PostMapping("/changePassword")
    public JsonResult changePassword(HttpServletRequest request, @RequestBody UserParam userParam){
        if(userParam.getPassword().isEmpty() || userParam.getPassword().length() < 6 || userParam.getPassword().length() > 128) return new JsonResult().failingAsString("密码格式有误");

        SessionModel session = SessionUtil.getSession(request);

        if(!userService.changePassword(session.getAccount(), session.getUserId(), userParam.getPassword(), userParam.getSmsCode())) return JsonResult.failing();

        return JsonResult.successful();
    }

    @GetMapping("/getPlayHistory")
    public JsonArrayResult<ProductView> getPlayHistory(HttpServletRequest request,Integer page){
        SessionModel session = SessionUtil.getSession(request);
        List<ProductView> list = productService.getPlayHistory(session.getUserId(), page);
        Integer maxPage = productService.getPlayHistoryTotalPage(session.getUserId());
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        jsonArrayResult.setCode(200);
        jsonArrayResult.setMsg("success");
        jsonArrayResult.setCount(maxPage == null ? 1000 : maxPage);
        return jsonArrayResult;
    }

    @GetMapping("/getCollects")
    public JsonArrayResult<CollectView> getCollects(HttpServletRequest request, Integer page){
        SessionModel session = SessionUtil.getSession(request);
        List<CollectView> list = collectService.getLimit(session.getUserId(), page);
        Integer maxPage = collectService.getTotalPage(session.getUserId());
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        jsonArrayResult.setCode(200);
        jsonArrayResult.setMsg("success");
        jsonArrayResult.setCount(maxPage == null ? 1000 : maxPage);
        return jsonArrayResult;
    }


    @GetMapping("/getShopping")
    public JsonArrayResult<OrderView> getShopping(HttpServletRequest request, Integer page){
        SessionModel session = SessionUtil.getSession(request);
        List<OrderView> list = orderService.getLimit(session.getUserId(), page);
        Integer maxPage = orderService.getTotalPage(session.getUserId());
        JsonArrayResult jsonArrayResult = new JsonArrayResult(0, list);
        jsonArrayResult.setCode(200);
        jsonArrayResult.setMsg("success");
        jsonArrayResult.setCount(maxPage == null ? 1000 : maxPage);
        return jsonArrayResult;
    }
}
