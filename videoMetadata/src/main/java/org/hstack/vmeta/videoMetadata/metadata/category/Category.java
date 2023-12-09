package org.hstack.vmeta.videoMetadata.metadata.category;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@Entity
@Table(name="category")
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private CategoryType categoryType;

    @Column
    private float perc;

    @Override
    public String toString() {
        return categoryType + ":" + perc;
    }


    /*
     * << categoryType Matching >>
     * - 한국 표준 십진분류법에 맞게 ETRI TTA 표준 태그셋 매칭
     */
    public final static Map<String, CategoryType> categoryTypeMap = new HashMap<String, CategoryType>() {
        {
            put("TMI_HW", CategoryType.IT);
            put("TMI_SW", CategoryType.IT);
            put("TMI_SERVICE", CategoryType.IT);

            put("TM_CLIMATE", CategoryType.GEOGRAPHY);
            put("AF_ROAD", CategoryType.GEOGRAPHY);
            put("LC_OTHERS", CategoryType.GEOGRAPHY);
            put("LCP_COUNTRY", CategoryType.GEOGRAPHY);
            put("LCP_PROVINCE", CategoryType.GEOGRAPHY);
            put("LCP_COUNTRY", CategoryType.GEOGRAPHY);
            put("LCP_CITY", CategoryType.GEOGRAPHY);
            put("LCP_CAPITALCITY", CategoryType.GEOGRAPHY);
            put("LCG_RIVER", CategoryType.GEOGRAPHY);
            put("LCG_OCEAN", CategoryType.GEOGRAPHY);
            put("LCG_BAY", CategoryType.GEOGRAPHY);
            put("LCG_MOUNTAIN", CategoryType.GEOGRAPHY);
            put("LCG_ISLAND", CategoryType.GEOGRAPHY);
            put("LCG_CONTINENT", CategoryType.GEOGRAPHY);
            put("LC_TOUR", CategoryType.GEOGRAPHY);
            put("LC_SPACE", CategoryType.GEOGRAPHY);
            put("QT_ZIPCODE", CategoryType.GEOGRAPHY);

            put("TM_CELL_TISSUE", CategoryType.MEDICINE);
            put("TMM_DISEASE", CategoryType.MEDICINE);
            put("TMM_DRUG", CategoryType.MEDICINE);
            put("OGG_MEDICINE", CategoryType.MEDICINE);
            put("FD_MEDICINE", CategoryType.MEDICINE);
            put("TR_MEDICINE", CategoryType.MEDICINE);

            put("TM_SPORTS", CategoryType.SPORTS);
            put("OGG_SPORTS", CategoryType.SPORTS);
            put("CV_SPORTS", CategoryType.SPORTS);
            put("CV_SPORTS_INST", CategoryType.SPORTS);
            put("EV_SPORTS", CategoryType.SPORTS);
            put("QT_SPORTS", CategoryType.SPORTS);

            put("OGG_ECONOMY", CategoryType.ECONOMY);
            put("CV_FUNDS", CategoryType.ECONOMY);
            put("CV_CURRENCY", CategoryType.ECONOMY);

            put("OGG_EDUCATION", CategoryType.EDUCATION);

            put("OGG_MILITARY", CategoryType.MILITARY);
            put("AF_WEAPON", CategoryType.MILITARY);


            put("OGG_SCIENCE", CategoryType.SCIENCE);
            put("FD_SCIENCE", CategoryType.SCIENCE);
            put("TR_SCIENCE", CategoryType.SCIENCE);
            put("MT_ELEMENT", CategoryType.SCIENCE);
            put("MT_METAL", CategoryType.SCIENCE);
            put("MT_ROCK", CategoryType.SCIENCE);
            put("MT_CHEMICAL", CategoryType.SCIENCE);

            put("OGG_LAW", CategoryType.LAW);
            put("CV_LAW", CategoryType.LAW);

            put("OGG_FOOD", CategoryType.FOOD);
            put("CV_FOOD", CategoryType.FOOD);
            put("CV_DRINK", CategoryType.FOOD);
            put("CV_FOOD_STYLE", CategoryType.FOOD);

            put("OGG_MEDIA", CategoryType.MEDIA);
            put("AFW_VIDEO", CategoryType.MEDIA);

            put("OGG_ART", CategoryType.ART);
            put("AF_WORKS", CategoryType.ART);
            put("AFW_PERFORMANCE", CategoryType.ART);
            put("AFW_ART_CRAFT", CategoryType.ART);
            put("FD_ART", CategoryType.ART);
            put("TR_ART", CategoryType.ART);
            put("CV_CLOTHING", CategoryType.ART);

            put("OGG_POLITICS", CategoryType.POLITICS);
            put("CV_POLICY", CategoryType.POLITICS);
            put("CV_TAX", CategoryType.POLITICS);
            put("FD_SOCIAL_SCIENCE", CategoryType.POLITICS);
            put("TR_SOCIAL_SCIENCE", CategoryType.POLITICS);

            put("OGG_HOTEL", CategoryType.TRAVEL);


            put("AF_MUSICAL_INSTRUMENT", CategoryType.MUSIC);
            put("AFW_MUSIC", CategoryType.MUSIC);
            put("AFW_DOCUMENT", CategoryType.MUSIC);

            put("AF_TRANSPORT", CategoryType.TRANSPORT);

            put("AF_CULTURAL_ASSET", CategoryType.CULTURE);
            put("CV_NAME", CategoryType.CULTURE);
            put("CV_TRIBE", CategoryType.CULTURE);

            put("CV_LANGUAGE", CategoryType.LINGUISTICS);

            put("CV_BUILDING_TYPE", CategoryType.ARCHITECTURE);


            put("AM_OTHERS", CategoryType.NATURE);
            put("AM_INSECT", CategoryType.NATURE);
            put("AM_BIRD", CategoryType.NATURE);
            put("AM_FISH", CategoryType.NATURE);
            put("AM_MAMMALIA", CategoryType.NATURE);
            put("AM_AMPHIBIA", CategoryType.NATURE);
            put("AM_REPTILIA", CategoryType.NATURE);
            put("AM_TYPE", CategoryType.NATURE);
            put("AM_PART", CategoryType.NATURE);

            put("PT_OTHERS", CategoryType.NATURE);
            put("PT_FRUIT", CategoryType.NATURE);
            put("PT_FLOWER", CategoryType.NATURE);
            put("PT_TREE", CategoryType.NATURE);
            put("PT_GRASS", CategoryType.NATURE);
            put("PT_TYPE", CategoryType.NATURE);
            put("PT_PART", CategoryType.NATURE);

            put("FD_PHILOSOPHY", CategoryType.PHILOSOPHY);
            put("TR_PHILOSOPHY", CategoryType.PHILOSOPHY);
            put("OGG_RELIGION", CategoryType.PHILOSOPHY);

            put("EV_ACTIVITY", CategoryType.HISTORY);
            put("EV_WAR_REVOLUTION", CategoryType.HISTORY);

            put("PS_NAME", CategoryType.PEOPLE);
        }
    };

    // 사실 여기서 쓸건 아닌데 나중에 다른 모듈 할때 복붙 편하라고...
    public final static Map<CategoryType, String> categoryMap = new HashMap<CategoryType, String>() {
        {
            put(CategoryType.POLITICS, "행정");
            put(CategoryType.ECONOMY, "경제");
            put(CategoryType.EDUCATION, "교육");
            put(CategoryType.MEDIA, "미디어");
            put(CategoryType.GEOGRAPHY, "지리");
            put(CategoryType.SPORTS, "스포츠");
            put(CategoryType.SCIENCE, "과학");
            put(CategoryType.LAW, "법학");
            put(CategoryType.MEDICINE, "의학");
            put(CategoryType.FOOD, "요리");
            put(CategoryType.MUSIC, "음악");
            put(CategoryType.CULTURE, "문화");
            put(CategoryType.TRANSPORT, "교통");
            put(CategoryType.ART, "예술");
            put(CategoryType.LINGUISTICS, "언어");
            put(CategoryType.PHILOSOPHY, "철학");
            put(CategoryType.HISTORY, "역사");
            put(CategoryType.NATURE, "자연");
            put(CategoryType.MILITARY, "군사");
            put(CategoryType.TRAVEL, "여행");
            put(CategoryType.PEOPLE, "인물");
            put(CategoryType.IT, "IT");
        }
    };
}