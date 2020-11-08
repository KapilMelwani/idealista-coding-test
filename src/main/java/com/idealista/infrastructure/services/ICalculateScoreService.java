package com.idealista.infrastructure.services;

import com.idealista.infrastructure.persistence.AdVO;

import java.util.List;

public interface ICalculateScoreService {
	List<AdVO> calculateAdScore();
}
