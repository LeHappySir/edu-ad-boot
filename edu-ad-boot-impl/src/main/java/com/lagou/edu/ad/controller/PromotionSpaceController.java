package com.lagou.edu.ad.controller;


import com.lagou.edu.ad.entity.PromotionSpace;
import com.lagou.edu.ad.service.IPromotionSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author felix
 * @since 2022-01-09
 */
// @RestController
// @RequestMapping("/ad/space")
public class PromotionSpaceController {

    @Autowired
    private IPromotionSpaceService spaceService;

    @GetMapping("/getAllSpaces")
    public List<PromotionSpace> getAllSpaces(){
        return spaceService.list();
    }

}
