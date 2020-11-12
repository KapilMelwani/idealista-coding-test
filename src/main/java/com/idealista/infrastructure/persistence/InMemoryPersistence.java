package com.idealista.infrastructure.persistence;

import com.idealista.config.ScoreValuesConfiguration;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryPersistence {

    private List<AdVO> ads;
    private List<PictureVO> pictures;

    public InMemoryPersistence() {
        this.ads = new ArrayList<AdVO>();
        this.ads.add(new AdVO(1, "CHALET", "Este piso es una ganga, compra, compra, COMPRA!!!!!", Collections.<Integer>emptyList(), 300, null, null, null));
        this.ads.add(new AdVO(2, "FLAT", "Nuevo ático céntrico recién reformado. No deje pasar la oportunidad y adquiera este ático de lujo", Arrays.asList(4), 300, null, null, null));
        this.ads.add(new AdVO(3, "CHALET", "", Arrays.asList(2), 300, null, null, null));
        this.ads.add(new AdVO(4, "FLAT", "Ático céntrico muy luminoso y recién reformado, parece nuevo", Arrays.asList(5), 300, null, null, null));
        this.ads.add(new AdVO(5, "FLAT", "Pisazo,", Arrays.asList(3, 8), 300, null, null, null));
        this.ads.add(new AdVO(6, "GARAGE", "", Arrays.asList(6), 300, null, null, null));
        this.ads.add(new AdVO(7, "GARAGE", "Garaje en el centro de Albacete", Collections.<Integer>emptyList(), 300, null, null, null));
        this.ads.add(new AdVO(8, "CHALET", "Maravilloso chalet situado en lAs afueras de un pequeño pueblo rural. El entorno es espectacular, las vistas magníficas. ¡Cómprelo ahora!", Arrays.asList(1, 7), 300, null, null, null));

        this.pictures = new ArrayList<PictureVO>();
        this.pictures.add(new PictureVO(1, "http://www.idealista.com/pictures/1", "SD"));
        this.pictures.add(new PictureVO(2, "http://www.idealista.com/pictures/2", "HD"));
        this.pictures.add(new PictureVO(3, "http://www.idealista.com/pictures/3", "SD"));
        this.pictures.add(new PictureVO(4, "http://www.idealista.com/pictures/4", "HD"));
        this.pictures.add(new PictureVO(5, "http://www.idealista.com/pictures/5", "SD"));
        this.pictures.add(new PictureVO(6, "http://www.idealista.com/pictures/6", "SD"));
        this.pictures.add(new PictureVO(7, "http://www.idealista.com/pictures/7", "SD"));
        this.pictures.add(new PictureVO(8, "http://www.idealista.com/pictures/8", "HD"));
    }

    /**
     * Get all ads
     * @return List<AdVO>
     */
    public List<AdVO> getAds() {
        return this.ads;
    }

    /**
     * Get picture by ad picture id
     * @param pictureId
     * @return PictureVO
     */
    public PictureVO getPicturesById(final Integer pictureId) {
        for(final PictureVO pictureVO : this.pictures) {
            if(pictureVO.getId().equals(pictureId)) {
                return  pictureVO;
            }
        }
        return null;
    }

    /**
     * Method to update ads of List<AdVo>
     * @param adVOList
     */
    public void updateAds(final List<AdVO> adVOList) {
        for(int i = 0; i< this.ads.size(); i++){
            for(final AdVO adVO : adVOList) {
               if(this.ads.get(i).getId().equals(adVO.getId())){
                   this.ads.set(i,adVO);
               }
            }
        }
    }

    /**
     * Method to filter ads with score greather than IRRELEVANT_SCORE
     * to return it to public users
     * @return List<AdVO>
     */
    public List<AdVO> filterPublicAds() {
        final List<AdVO> publicAdVOList = this.ads.stream().filter(
            adVO -> adVO.getScore()>= ScoreValuesConfiguration.IRRELEVANT_AD_SCORE
        ).sorted(Comparator.comparing(AdVO::getScore)).collect(Collectors.toList());
        return publicAdVOList;
    }

    /**
     * Calculate ads avg
     * @return
     */
    public Integer adsAverage() {
        Integer countScore = 0;
        for(final AdVO ad : this.ads) {
            countScore += ad.getScore();
        }
        final Integer avg = countScore/ this.ads.size();
        return avg;
    }

    public void setAds(final List<AdVO> ads) {
        this.ads = ads;
    }

    public List<PictureVO> getPictures() {
        return this.pictures;
    }

    public void setPictures(final List<PictureVO> pictures) {
        this.pictures = pictures;
    }
}
