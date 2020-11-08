package com.idealista.infrastructure.services.impl;

import com.idealista.infrastructure.api.PublicAd;
import com.idealista.infrastructure.persistence.AdVO;
import com.idealista.infrastructure.persistence.InMemoryPersistence;
import com.idealista.infrastructure.services.IPublicAdService;
import com.idealista.mapper.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicAdServiceImpl implements IPublicAdService {

	@Autowired
	private InMemoryPersistence inMemoryPersistence;

	@Override
	public List<PublicAd> getPublicAds(){
		List<AdVO> adVOList = inMemoryPersistence.filterPublicAds();
		List<PublicAd> qualityAds = ObjectMapper.mapAll(adVOList,PublicAd.class);
		return qualityAds;
 	}
}
