package com.idealista.infrastructure.services;

import com.idealista.infrastructure.api.QualityAd;

import java.util.List;

public interface IQualityAdService {
	List<QualityAd> getQualityAds();
	Integer calculateQualityScoreAvg();
}