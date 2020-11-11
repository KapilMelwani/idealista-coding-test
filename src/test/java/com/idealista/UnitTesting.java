package com.idealista;

import com.idealista.infrastructure.persistence.AdVO;
import com.idealista.infrastructure.persistence.InMemoryPersistence;
import com.idealista.infrastructure.persistence.PictureVO;
import com.idealista.infrastructure.services.impl.CalculateScoreServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
import java.util.Arrays;


@ExtendWith(MockitoExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UnitTesting {

	@InjectMocks CalculateScoreServiceImpl calculateScoreService;

	@Mock
	InMemoryPersistence inMemoryPersistence;

	private PictureVO pictureVO;
	private AdVO adVO;

	@Before
	public void setup() {
		pictureVO = new PictureVO(1, "http://www.idealista.com/pictures/1", "SD");
		adVO = new AdVO(3, "CHALET", "Nuevo chalet CÉNTRICO y recién reformado. No deje pasar la oportunidad y adquiera este chalet de lujo",Arrays.asList(pictureVO.getId()), 300, 20, 0, null);
	}

	@Test
	public void getPicturesByIdTest() throws Exception {
		Mockito.when(inMemoryPersistence.getPicturesById(1)).thenReturn(pictureVO);
		AdVO test = calculateScoreService.calculatePhotoScore(adVO);
		assertEquals(test.getScore().intValue(), 10);
	}

	@Test
	public void calculateDescriptionScoreTest() throws Exception {
		AdVO test = calculateScoreService.calculateDescriptionScore(adVO);
		assertEquals(test.getScore().intValue(), 5);

	}
	@Test
	public void calculateDescriptionSizeScoreTest() throws Exception {
		AdVO test = calculateScoreService.calculateDescriptionSizeScore(adVO);
		assertEquals(test.getScore().intValue(), 0);

	}
	@Test
	public void calculateDescriptionKeywordsScoreCaseInsensitiveTest() throws Exception {
		AdVO test = calculateScoreService.calculateDescriptionKeywordsScoreCaseInsensitive(adVO);
		assertEquals(test.getScore().intValue(), 15);
	}
	@Test
	public void calculateFullAddTest() throws Exception {
		AdVO test = calculateScoreService.calculateFullAdd(adVO);
		assertEquals(test.getScore().intValue(), 40);
	}
}