package com.idealista.infrastructure.services;

import com.idealista.infrastructure.api.PublicAd;

import java.util.List;

public interface IPublicAdService {
	List<PublicAd> getPublicAds() throws Exception;
}
