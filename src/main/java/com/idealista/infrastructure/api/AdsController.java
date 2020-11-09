package com.idealista.infrastructure.api;

import java.util.List;


import com.idealista.infrastructure.services.ICalculateScoreService;
import com.idealista.infrastructure.services.IPublicAdService;
import com.idealista.infrastructure.services.IQualityAdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    Logger logger = LoggerFactory.getLogger(AdsController.class);


    @PreAuthorize("hasRole('ADMIN_QUALITY')")
    @RequestMapping(method = RequestMethod.GET, value = "/quality", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity qualityListing() {
        try {
            List<QualityAd> qualityAds = qualityAdService.getQualityAds();
            Integer qualityAvg = qualityAdService.calculateQualityScoreAvg();
            logger.info("[GET_QUALITY_AD] Getting quality ad's list");
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Quality ads average: " + qualityAvg, qualityAds));
        } catch (Exception e) {
            logger.info("[GET_PUBLIC_AD] Error getting quality list : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity publicListing() throws Exception{
        try {
            List<PublicAd> publicAds = publicAdService.getPublicAds();
            logger.info("[GET_PUBLIC_AD] Getting public ad's list");
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Public ads:", publicAds));
        } catch (Exception e) {
            logger.info("[GET_PUBLIC_AD] Error getting public list : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN_QUALITY')")
    @RequestMapping(method = RequestMethod.PUT, value = "/calculateScores", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity calculateScore() {
        try {
            calculateScoreService.calculateAdScore();
            logger.info("[CALCULATE_SCORES] Calculating ad's scores");
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),"All scores has been calculated", calculateScoreService.calculateAdScore()));
        } catch (Exception e) {
            logger.info("[CALCULATE_SCORES] Error calculating ad's scores");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
}
