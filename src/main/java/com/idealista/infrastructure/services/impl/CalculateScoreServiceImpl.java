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
import java.util.stream.Collectors;

@Service
public class CalculateScoreServiceImpl implements ICalculateScoreService {

	@Autowired private InMemoryPersistence inMemoryPersistence;

	@Override
	public List<AdVO> calculateAdScore() {
		final List<AdVO> adVOList = this.inMemoryPersistence.getAds();
		final List<AdVO> adVOListAlreadyCalculated = alreadyCalculated(adVOList);
		if(adVOListAlreadyCalculated.isEmpty()) {
			return adVOList;
		} else {
			adVOListAlreadyCalculated.stream().forEach(
				adVO -> {
					initScore(adVO);
					calculatePhotoScore(adVO);
					calculateDescriptionScore(adVO);
					calculateDescriptionSizeScore(adVO);
					calculateDescriptionKeywordsScoreCaseInsensitive(adVO);
					calculateFullAdd(adVO);
					updateScore(adVO);
					this.inMemoryPersistence.updateAds(adVOList);
				}
			);
		}
		return adVOListAlreadyCalculated;
	}


	public AdVO calculatePhotoScore(final AdVO adVO) {
		if(adVO.getPictures()!=null && adVO.getPictures().isEmpty()) {
			adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.NO_PHOTO_SCORE);
			return adVO;
		} else {
			final List<Integer> adVoPicturesId = adVO.getPictures();
			for(final Integer picturesId : adVoPicturesId) {
				final PictureVO pictureVO = this.inMemoryPersistence.getPicturesById(picturesId);
				if (pictureVO != null && pictureVO.getQuality().equals(ScoreValuesConfiguration.PhotoQuality.SD.toString())) {
					adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.PHOTO_SD_SCORE);
				} else {
					if (pictureVO != null && pictureVO.getQuality().equals(ScoreValuesConfiguration.PhotoQuality.HD.toString()))
						adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.PHOTO_HD_SCORE);
				}
			}
			return adVO;
		}
	}

	public AdVO calculateDescriptionScore(final AdVO adVO) {
		if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()) {
			adVO.setScore(adVO.getScore()+5);
		}
		return adVO;
	}

	public AdVO calculateDescriptionSizeScore(final AdVO adVO) {
		if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()){
			final int sentenceWordsLength = splitBySpacesLength(adVO.getDescription());
			if(adVO.getTypology().equals(ScoreValuesConfiguration.Typology.FLAT.toString())) {
				if (sentenceWordsLength >= ScoreValuesConfiguration.PISO_DESCRIPTION_BIGGER_THAN && sentenceWordsLength <= ScoreValuesConfiguration.PISO_DESCRIPTION_LESS_THAN) {
					adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.PISO_DESCRIPTION_BETWEEN_SCORE);
				} else {
					if (sentenceWordsLength > ScoreValuesConfiguration.CHALET_DESCRIPTION_BIGGER_THAN) {
						adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.PISO_DESCRIPTION_BIGGER_THAN_SCORE);
					}
				}
			} else {
				if(adVO.getTypology().equals(ScoreValuesConfiguration.Typology.CHALET.toString()) && sentenceWordsLength > ScoreValuesConfiguration.CHALET_DESCRIPTION_BIGGER_THAN) {
					adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.CHALET_DESCRIPTION_BIGGER_THAN_SCORE);
				}
			}
		}
		return adVO;
	}

	private int splitBySpacesLength(final String sentence) {
		final String[] sentenceSplitted = sentence.split("\\s+");
		return sentenceSplitted.length;
	}

	public AdVO calculateDescriptionKeywordsScore(final AdVO adVO) {
		if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()) {
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.LUMINOSO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.NUEVO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.CÉNTRICO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.REFORMADO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(adVO.getDescription().contains(ScoreValuesConfiguration.Keywords.ÁTICO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
		}
		return adVO;
	}

	public AdVO calculateDescriptionKeywordsScoreCaseInsensitive(final AdVO adVO) {
		if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()) {
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.LUMINOSO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.NUEVO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.CÉNTRICO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.REFORMADO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
			if(containsWithoutCaseSensitive(adVO.getDescription(),ScoreValuesConfiguration.Keywords.ÁTICO.toString())) {
				adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.KEYWORDS_SCORE);
			}
		}
		return adVO;
	}

	private Boolean containsWithoutCaseSensitive(final String sentence, final String keyword) {
		return Pattern.compile(Pattern.quote(keyword.toLowerCase()), Pattern.CASE_INSENSITIVE).matcher(sentence.toLowerCase()).find();
	}


	public AdVO calculateFullAdd(final AdVO adVO) {
		if(!adVO.getPictures().isEmpty()) {
			if(adVO.getDescription()!=null && !adVO.getDescription().isEmpty()) {
				if (adVO.getTypology().equals(ScoreValuesConfiguration.Typology.FLAT.toString())) {
					if (adVO.getHouseSize() != null) {
						adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.FULL_AD_PROPERTIES_SCORE);
					}
				} else {
					if (adVO.getTypology().equals(ScoreValuesConfiguration.Typology.CHALET.toString())) {
						if (adVO.getHouseSize() != null) {
							if (adVO.getGardenSize() != null) {
								adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.FULL_AD_PROPERTIES_SCORE);
							}
						}
					}
				}
			} else {
				if (adVO.getTypology().equals(ScoreValuesConfiguration.Typology.GARAGE.toString())) {
					if (adVO.getHouseSize() != null) {
						adVO.setScore(adVO.getScore() + ScoreValuesConfiguration.FULL_AD_PROPERTIES_GARAGE_SCORE);
					}
				}
			}
		}
		return adVO;
	}

	private void updateScore(final AdVO adVO) {
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

	private void initScore(final AdVO adVO) {
		if(adVO.getScore()==null){
			adVO.setScore(ScoreValuesConfiguration.MIN_AD_SCORE);
		}
	}

	private List<AdVO> alreadyCalculated(final List<AdVO> adVOs) {
		return adVOs.stream().filter(
			adVO -> adVO.getScore()==null
		).collect(Collectors.toList());
	}
}
