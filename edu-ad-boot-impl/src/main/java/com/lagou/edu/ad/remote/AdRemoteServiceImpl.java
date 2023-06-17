package com.lagou.edu.ad.remote;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.ad.client.dto.PromotionAdDTO;
import com.lagou.edu.ad.client.dto.PromotionSpaceDTO;
import com.lagou.edu.ad.client.remote.AdRemoteService;
import com.lagou.edu.ad.entity.PromotionAd;
import com.lagou.edu.ad.entity.PromotionSpace;
import com.lagou.edu.ad.service.IPromotionAdService;
import com.lagou.edu.ad.service.IPromotionSpaceService;
import com.lagou.edu.common.response.ResponseDTO;
import com.lagou.edu.common.utils.CoverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * AdRemoteServiceImpl
 *
 * @author xianhongle
 * @data 2022/1/9 7:21 下午
 **/
@RestController
@RequestMapping("/ad")
public class AdRemoteServiceImpl implements AdRemoteService {

    @Autowired
    private IPromotionSpaceService promotionSpaceService;

    @Autowired
    private IPromotionAdService promotionAdService;

    //广告位的新增或者修改
    @RequestMapping(value = "/space/saveOrUpdate",method = RequestMethod.POST)
    public ResponseDTO saveOrUpdate(PromotionSpaceDTO promotionSpaceDTO){
        PromotionSpace entity = new PromotionSpace();
        CoverUtil.cover(promotionSpaceDTO,entity);
        ResponseDTO responseDTO = null;


        //保存或者更新
        try {
            entity.setUpdateTime(new Date());
            if(entity.getId() != null){
                promotionSpaceService.updateById(entity);
            }else {
                entity.setIsDel(0);
                entity.setCreateTime(new Date());
                promotionSpaceService.save(entity);
            }
            responseDTO = ResponseDTO.success();
        }catch (Exception e){
            responseDTO = ResponseDTO.ofError(e.getMessage());
            throw e;
        }
        return responseDTO;
    }

    @Override
    @GetMapping("/space/getAllSpaces")
    public List<PromotionSpaceDTO> getAllSpaces() {
        List<PromotionSpace> list = promotionSpaceService.list();
        return CoverUtil.coverList(list,PromotionSpaceDTO.class);
    }

    @Override
    @GetMapping("/getAdBySpaceKey")
    public List<PromotionSpaceDTO> getAdBySpaceKey(String[] spaceKey) {

        List<PromotionSpaceDTO> rs = new ArrayList<>();
        for (String key:spaceKey) {

            // 广告位
            QueryWrapper<PromotionSpace> spaceQueryWrapper = new QueryWrapper<>();
            spaceQueryWrapper.eq("spaceKey",key);
            PromotionSpace promotionSpace = promotionSpaceService.getOne(spaceQueryWrapper);


            if(promotionSpace != null){
                // 广告
                QueryWrapper<PromotionAd> adQueryWrapper = new QueryWrapper<>();
                adQueryWrapper.eq("spaceId",promotionSpace.getId());
                // 状态为有效
                adQueryWrapper.eq("status",1);
                // 且广告有效起始时间在当前时间内
                Date now = new Date();
                adQueryWrapper.lt("startTime",now);
                adQueryWrapper.gt("endTime",now);
                List<PromotionAd> promotionAdList = promotionAdService.list(adQueryWrapper);
                List<PromotionAdDTO> promotionAdDTOS = CoverUtil.coverList(promotionAdList, PromotionAdDTO.class);


                // 转换实体
                PromotionSpaceDTO spaceDTO = CoverUtil.cover(promotionSpace, PromotionSpaceDTO.class);
                spaceDTO.setAdDTOList(promotionAdDTOS);
                rs.add(spaceDTO);
            }
        }

        return rs;
    }

    @Override
    @PostMapping("/space/saveOrUpdateSpace")
    public ResponseDTO saveOrUpdateSpace(@RequestBody PromotionSpaceDTO spaceDTO) {

        // 保存或更新广告位信息
        PromotionSpace space = CoverUtil.cover(spaceDTO, PromotionSpace.class);
        if(space.getId() == null){
            space.setCreateTime(new Date());
            space.setUpdateTime(new Date());
            space.setIsDel(0);
        }else{
            space.setUpdateTime(new Date());
        }

        ResponseDTO rs = null;
        try {
            promotionSpaceService.saveOrUpdate(space);
            rs = ResponseDTO.success();
        }catch (Exception e){
            rs = ResponseDTO.ofError(e.getMessage());
        }

        return rs;
    }

    @Override
    @GetMapping("/space/getSpaceById")
    public PromotionSpaceDTO getSpaceById(@RequestParam("id") Integer id) {
        PromotionSpace promotionSpace = promotionSpaceService.getById(id);
        return CoverUtil.cover(promotionSpace,PromotionSpaceDTO.class);
    }

    @Override
    @GetMapping("/getAllAds")
    public List<PromotionAdDTO> getAllAds() {
        List<PromotionAd> promotionAds = promotionAdService.list();
        return CoverUtil.coverList(promotionAds,PromotionAdDTO.class);
    }

    @Override
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    public ResponseDTO saveOrUpdateAd(PromotionAdDTO promotionAdDTO) {

        PromotionAd ad = CoverUtil.cover(promotionAdDTO, PromotionAd.class);
        if(ad.getId() == null){
            ad.setCreateTime(new Date());
            ad.setUpdateTime(new Date());
            ad.setStatus(1);
        }else{
            ad.setUpdateTime(new Date());
        }

        ResponseDTO rs = null;
        try{
            promotionAdService.saveOrUpdate(ad);
            rs = ResponseDTO.success();
        }catch(Exception e){
            rs = ResponseDTO.ofError(e.getMessage());
        }

        return rs;
    }

    @Override
    @RequestMapping("/getAdById")
    public ResponseDTO<PromotionAdDTO> getAdById(Integer id) {
        PromotionAd ad = promotionAdService.getById(id);
        return ResponseDTO.success(CoverUtil.cover(ad,PromotionAdDTO.class));
    }

    @Override
    @RequestMapping("/updateStatus")
    public ResponseDTO updateStatus(Integer id, Integer status) {

        ResponseDTO responseDTO = null;
        try {
            if(status == 0 || status == 1){
                PromotionAd promotionAd = new PromotionAd();
                promotionAd.setId(id);
                promotionAd.setStatus(status);
                promotionAdService.updateById(promotionAd);
                responseDTO = ResponseDTO.success();
            }
        }catch (Exception e){
            responseDTO = ResponseDTO.ofError(e.getMessage());
            throw  e;
        }
        return responseDTO;
    }
}
