package com.lagou.edu.ad.client.remote;

import com.lagou.edu.ad.client.dto.PromotionAdDTO;
import com.lagou.edu.ad.client.dto.PromotionSpaceDTO;
import com.lagou.edu.common.response.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdRemoteService
 *
 * @author xianhongle
 * @data 2022/1/9 7:11 下午
 **/
@FeignClient(name = "${remote.feign.edu-ad-boot.name:edu-ad-boot}", path = "/ad")
public interface AdRemoteService {

    @GetMapping("/getAllAds")
    List<PromotionAdDTO> getAllAds();

    @GetMapping("/getAdBySpaceKey")
    List<PromotionSpaceDTO> getAdBySpaceKey(@RequestParam("spaceKey") String[] spaceKey);

    @PostMapping("/saveOrUpdateAd")
    ResponseDTO saveOrUpdateAd(@RequestBody PromotionAdDTO promotionAdDTO);

    @GetMapping("/getAdById")
    ResponseDTO<PromotionAdDTO> getAdById(@RequestParam("id") Integer id);

    @RequestMapping("/updateStatus")
    ResponseDTO updateStatus(@RequestParam("id") Integer id,
                             @RequestParam("status") Integer status);

    @GetMapping("/space/getAllSpaces")
    List<PromotionSpaceDTO> getAllSpaces();

    @PostMapping("/space/saveOrUpdateSpace")
    ResponseDTO saveOrUpdateSpace(@RequestBody PromotionSpaceDTO spaceDTO);

    @GetMapping("/space/getSpaceById")
    PromotionSpaceDTO getSpaceById(@RequestParam("id") Integer id);

}
