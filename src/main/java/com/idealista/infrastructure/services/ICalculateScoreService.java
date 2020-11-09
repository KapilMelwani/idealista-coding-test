package com.idealista.infrastructure.services;

import com.idealista.infrastructure.persistence.AdVO;

import java.util.List;

public interface ICalculateScoreService {

	/**
	 * Service layer method to calculate ad's score having count
	 * the score ad rules
	 * @return
	 */
	List<AdVO> calculateAdScore();
}
