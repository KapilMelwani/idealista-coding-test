package com.idealista.infrastructure.services;

import com.idealista.infrastructure.api.PublicAd;

import java.util.List;

public interface IPublicAdService {

	/**
	 * Service layer method to get all public users add
	 * @return List<PublicAd>
	 * @throws Exception
	 */
	List<PublicAd> getPublicAds() throws Exception;
}
