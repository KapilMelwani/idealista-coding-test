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
	public List<PublicAd> getPublicAds() throws Exception {
		List<AdVO> adVOList = inMemoryPersistence.findAll();
		if(!verifyIfScoreCalculated(adVOList)) {
			List<AdVO> adVOListFiltered = inMemoryPersistence.filterPublicAds();
			List<PublicAd> qualityAds = ObjectMapper.mapAll(adVOListFiltered,PublicAd.class);
			return qualityAds;
		} else {
			throw new Exception("No se han calculado los score de los anuncios.");
		}
 	}

 	private Boolean verifyIfScoreCalculated(List<AdVO> adVOList) {
		for(AdVO adVO : adVOList) {
			if(adVO.getScore()==null) {
				return true;
			}
		}
		return false;
    }
}
