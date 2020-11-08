package com.idealista.infrastructure.services.impl;

import com.idealista.config.ScoreValuesConfiguration;
import com.idealista.infrastructure.persistence.AdVO;
import com.idealista.infrastructure.persistence.InMemoryPersistence;
import com.idealista.infrastructure.persistence.PictureVO;
import com.idealista.infrastructure.services.ICalculateScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CalculateScoreServiceImpl implements ICalculateScoreService {

	@Autowired private InMemoryPersistence inMemoryPersistence;


	public List<AdVO> calculateAdScore() {
		List<AdVO> adVOList = inMemoryPersistence.getAds();
		adVOList.stream().forEach(
			adVO -> {
				initScore(adVO);
				calculatePhotoScore(adVO);
				calculateDescriptionScore(adVO);
				calculateDescriptionSizeScore(adVO);
				calculateDescriptionKeywordsScore(adVO);
				calculateFullAdd(adVO);
				updateScore(adVO);
				inMemoryPersistence.updateAds(adVOList);
			}
		);
		return adVOList;
	}


	private AdVO calculatePhotoScore(AdVO adVO) {
		if(adVO.getPictures()!=null && adVO.getPictures().isEmpty()) {
			adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.NO_PHOTO_SCORE);
			return adVO;
		} else {
			List<Integer> adVoPicturesId = adVO.getPictures();
			adVoPicturesId.stream().forEach(
				picturesId -> {
					PictureVO pictureVO = inMemoryPersistence.getPicturesById(picturesId);
					if(pictureVO!=null && pictureVO.getQuality().equals(ScoreValuesConfiguration.PhotoQuality.SD)) {
						adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.PHOTO_SD_SCORE);
					} else {
						if(pictureVO!=null && pictureVO.getQuality().equals(ScoreValuesConfiguration.PhotoQuality.HD))
						adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.PHOTO_HD_SCORE);
					}
				}
			);
			return adVO;
		}
	}

	private AdVO calculateDescriptionScore(AdVO adVO) {
		if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()) {
			adVO.setScore(adVO.getScore()+5);
		}
		return adVO;
	}

	private AdVO calculateDescriptionSizeScore(AdVO adVO) {
		if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()){
			if(adVO.getTypology().equals(ScoreValuesConfiguration.Typology.Piso.toString())) {
				if (adVO.getDescription().length() >= ScoreValuesConfiguration.PISO_DESCRIPTION_BIGGER_THAN && adVO.getDescription().length() <= ScoreValuesConfiguration.PISO_DESCRIPTION_LESS_THAN) {
					adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.PISO_DESCRIPTION_BETWEEN_SCORE);
				} else {
					if (adVO.getDescription().length() > ScoreValuesConfiguration.CHALET_DESCRIPTION_BIGGER_THAN) {
						adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.PISO_DESCRIPTION_BIGGER_THAN_SCORE);
					}
				}
			} else {
				if(adVO.getTypology().equals(ScoreValuesConfiguration.Typology.Chalet.toString()) && adVO.getDescription().length() > 49) {
					adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.CHALET_DESCRIPTION_BIGGER_THAN_SCORE);
				}
			}
		}
		return adVO;
	}

	private AdVO calculateDescriptionKeywordsScore(AdVO adVO) {
		if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()) {
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.Luminoso.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.Nuevo.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.Céntrico.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.Reformado.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.Ático.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
		}
		return adVO;
	}


	/**
	 * 	TODO: Comprobar si esto es viable y sería buen toque de calidad, case Insensitive
	 */


	private AdVO calculateDescriptionKeywordsScoreWithoutCaseSensitive(AdVO adVO) {
		if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()) {
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.Luminoso.toString())) {
				adVO.setScore(adVO.getScore() + 5);
			}
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.Nuevo.toString())) {
				adVO.setScore(adVO.getScore() + 5);
			}
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.Céntrico.toString())) {
				adVO.setScore(adVO.getScore() + 5);
			}
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.Reformado.toString())) {
				adVO.setScore(adVO.getScore() + 5);
			}
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.Ático.toString())) {
				adVO.setScore(adVO.getScore() + 5);
			}
		}
		return adVO;
	}

	private Boolean containsWithoutCaseSensitive(String contains, String string) {
		return Pattern.compile(Pattern.quote(contains), Pattern.CASE_INSENSITIVE).matcher(string).find();
	}


	private AdVO calculateFullAdd(AdVO adVO) {
		if(!adVO.getPictures().isEmpty()) {
			if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()) {
				if (adVO.getTypology().equals(ScoreValuesConfiguration.Typology.Piso.toString())) {
					if (adVO.getHouseSize() != null) {
						adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.FULL_AD_PROPERTIES_SCORE);
					}
					if (adVO.getTypology().equals(ScoreValuesConfiguration.Typology.Chalet.toString())) {
						if (adVO.getHouseSize() != null) {
							if (adVO.getGardenSize() != null) {
								adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.FULL_AD_PROPERTIES_SCORE);
							}
						}
					}
				}
			} else {
				if (adVO.getTypology().equals(ScoreValuesConfiguration.Typology.Garaje.toString())) {
					if (adVO.getHouseSize() != null) {
						adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.FULL_AD_PROPERTIES_GARAGE_SCORE);
					}
				}
			}
		}
		return adVO;
	}

	private void updateScore(AdVO adVO) {
		if(adVO.getScore()<ScoreValuesConfiguration.MIN_AD_SCORE) {
			adVO.setScore(ScoreValuesConfiguration.MIN_AD_SCORE);
		}
		if(adVO.getScore()>ScoreValuesConfiguration.MAX_AD_SCORE) {
			adVO.setScore(ScoreValuesConfiguration.MAX_AD_SCORE);
		}
		if(adVO.getScore()>=ScoreValuesConfiguration.IRRELEVANT_AD_SCORE) {
			adVO.setIrrelevantSince(null);
		} else {
			adVO.setIrrelevantSince(new Date());
		}
	}

	private void initScore(AdVO adVO) {
		if(adVO.getScore()==null){
			adVO.setScore(ScoreValuesConfiguration.MIN_AD_SCORE);
		}
	}
}
