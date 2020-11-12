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
		this.pictureVO = new PictureVO(1, "http://www.idealista.com/pictures/1", "SD");
		this.adVO = new AdVO(3, "CHALET", "Nuevo chalet CÉNTRICO y recién reformado. No deje pasar la oportunidad y adquiera este chalet de lujo, es un piso increible de 300 metros cuadrados en donde podrá disfrutar de su familia, amigos, además, tiene un jardin de \n" +
			"20 metros cuadrados donde podrá hacer barbacoas (ya que la casa ya viene totalmente amueblada y trae una barbacoa moderna)",Arrays.asList(this.pictureVO.getId()), 300, 20, 0, null);
	}

	@Test
	public void getPicturesByIdTest() throws Exception {
		Mockito.when(this.inMemoryPersistence.getPicturesById(1)).thenReturn(this.pictureVO);
		final AdVO test = this.calculateScoreService.calculatePhotoScore(this.adVO);
		assertEquals(test.getScore().intValue(), 10);
	}

	@Test
	public void calculateDescriptionScoreTest() throws Exception {
		final AdVO test = this.calculateScoreService.calculateDescriptionScore(this.adVO);
		assertEquals(test.getScore().intValue(), 5);

	}
	@Test
	public void calculateDescriptionSizeScoreTest() throws Exception {
		final AdVO test = this.calculateScoreService.calculateDescriptionSizeScore(this.adVO);
		assertEquals(test.getScore().intValue(), 20);

	}
	@Test
	public void calculateDescriptionKeywordsScoreCaseInsensitiveTest() throws Exception {
		final AdVO test = this.calculateScoreService.calculateDescriptionKeywordsScoreCaseInsensitive(this.adVO);
		assertEquals(test.getScore().intValue(), 15);
	}
	@Test
	public void calculateFullAddTest() throws Exception {
		final AdVO test = this.calculateScoreService.calculateFullAdd(this.adVO);
		assertEquals(test.getScore().intValue(), 40);
	}
}