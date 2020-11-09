package com.idealista.infrastructure.services.impl;

import com.idealista.infrastructure.api.QualityAd;
import com.idealista.infrastructure.persistence.AdVO;
import com.idealista.infrastructure.persistence.InMemoryPersistence;
import com.idealista.infrastructure.services.IQualityAdService;
import com.idealista.mapper.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualityAdServiceImpl implements IQualityAdService {

	@Autowired
	private InMemoryPersistence inMemoryPersistence;

	@Override
	public List<QualityAd> getQualityAds() throws Exception{
		List<AdVO> adVOList = inMemoryPersistence.findAll();
		if(!verifyIfScoreCalculated(adVOList)) {
			List<QualityAd> qualityAds = ObjectMapper.mapAll(adVOList,QualityAd.class);
			return qualityAds;
		} else {
			throw new Exception("No se han calculado los score de los anuncios.");
		}
	}

	@Override
	public Integer calculateQualityScoreAvg(){
		Integer avg = null;
		if (inMemoryPersistence.adsAverage()!=null) {
			avg = inMemoryPersistence.adsAverage();
		}
		return avg;
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
