package com.idealista.config;

public class ScoreValuesConfiguration {

	public ScoreValuesConfiguration() { }

	public enum Keywords {
		LUMINOSO,
		NUEVO,
		CÉNTRICO,
		REFORMADO,
		ÁTICO
	}

	public enum Typology {
		CHALET,
		FLAT,
		GARAGE
	}

	public enum PhotoQuality {
		SD,
		HD,
	}

	public static final Integer MIN_AD_SCORE = 0;
	public static final Integer MAX_AD_SCORE = 100;
	public static final Integer IRRELEVANT_AD_SCORE = 40;

	public static final Integer KEYWORDS_SCORE = 5;

	public static final Integer NO_PHOTO_SCORE = -10;
	public static final Integer PHOTO_HD_SCORE = 20;
	public static final Integer PHOTO_SD_SCORE = 10;

	public static final Integer PISO_DESCRIPTION_BETWEEN_SCORE = 10;
	public static final Integer PISO_DESCRIPTION_BIGGER_THAN_SCORE = 30;
	public static final Integer CHALET_DESCRIPTION_BIGGER_THAN_SCORE = 20;

	public static final Integer PISO_DESCRIPTION_BIGGER_THAN = 20;
	public static final Integer PISO_DESCRIPTION_LESS_THAN = 49;
	public static final Integer CHALET_DESCRIPTION_BIGGER_THAN = 49;

	public static final Integer FULL_AD_PROPERTIES_SCORE = 40;
	public static final Integer FULL_AD_PROPERTIES_GARAGE_SCORE = 40;
}
