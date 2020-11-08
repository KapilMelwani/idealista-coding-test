package com.idealista.infrastructure.api;

import java.util.List;

import com.idealista.infrastructure.persistence.AdVO;
import com.idealista.infrastructure.services.ICalculateScoreService;
import com.idealista.infrastructure.services.IPublicAdService;
import com.idealista.infrastructure.services.IQualityAdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ads/")
public class AdsController {

    @Autowired
    ICalculateScoreService calculateScoreService;
    @Autowired
    IPublicAdService publicAdService;
    @Autowired
    IQualityAdService qualityAdService;


    @RequestMapping(method = RequestMethod.GET, value = "/quality")
    public ResponseEntity<ApiResponse> qualityListing() {
        List<QualityAd> qualityAds = qualityAdService.getQualityAds();
        Integer qualityAvg = qualityAdService.calculateQualityScoreAvg();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),"Quality ads average: " + qualityAvg, qualityAds));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/public")
    public ResponseEntity<ApiResponse> publicListing() {
        List<PublicAd> publicAds = publicAdService.getPublicAds();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),"Public ads:", publicAds));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/calculateScores")
    public ResponseEntity<ApiResponse> calculateScore() {
        calculateScoreService.calculateAdScore();
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),"All scores has been calculated", calculateScoreService.calculateAdScore()));
    }
}
