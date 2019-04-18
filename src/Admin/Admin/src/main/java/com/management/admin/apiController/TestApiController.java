/***
 * @pName Admin
 * @name TestApiController
 * @user HongWei
 * @date 2019/3/19
 * @desc
 */
package com.management.admin.apiController;

import com.management.admin.entity.db.Product;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.repository.ProductMapper;
import com.management.admin.utils.IdWorker;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/test")
public class TestApiController {
    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/importProducts")
    public JsonResult importProducts() {
        List<Product> list= new ArrayList<>();
        String prefix = "https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/aishi/";
        String[] names = new String[]{"测试标题12131231231231231231231231231","测试测测试测试测测试测手册是测试测测试测","啊啊撒大声地稍等去驱蚊器翁群翁全"};
        Double[] amounts = new Double[]{3D,4.99,99D};
        String[] suffix = new String[]{"stream3.png","stream2.png","stream3.png"};
        Integer[] shows = new Integer[]{1331,122,5};
        for (int i = 0; i < 100; i++) {
            Product product = new Product();
            try {
                product.setProductId(IdWorker.getFlowIdWorkerInstance().nextId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            int random = RandomUtils.nextInt(0, 2);
            product.setImage(prefix + suffix[random]);
            product.setProductName(names[random]);
            product.setAmount(amounts[random]);
            product.setShowCount(shows[random]);
            product.setCategoryId(1);
            product.setStatus(1);
            product.setIsShow(0);
            product.setAddDate(new Date());
            list.add(product);
        }
        productMapper.insertList(list);
        return JsonResult.successful();
    }

}
