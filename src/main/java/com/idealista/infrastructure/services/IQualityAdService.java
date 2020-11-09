package com.idealista.infrastructure.services;

import com.idealista.infrastructure.api.QualityAd;

import java.util.List;

public interface IQualityAdService {

	/**
	 * Service layer method to get all ads mapped to QualityAd claass
	 * @return List<QualityAd>
	 * @throws Exception
	 */
	List<QualityAd> getQualityAds() throws Exception;

	/**
	 * Service layer method to calculate average score
	 * @return Integer
	 * @throws Exception
	 */
	Integer calculateQualityScoreAvg();
}