package org.hstack.vmeta.extraction.category;

import com.google.gson.Gson;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryCalculatorTest {

    static class NameEntity {
        final String text;
        final String type;
        Integer count;
        public NameEntity (String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
    }

    @Value("${etri.keys}")
    private String[] ETRI_API_KEY;

    @Value("${etri.url}")
    private String ETRI_STT_API_URL;

    private static final String ETRI_STT_API_SUB_URL= "/WiseNLU";

    private static final String ANALYSIS_CODE = "ner";     // 언어 분석 코드

    private static final String sampleText = "윤동주(尹東柱, 1917년 12월 30일 ~ 1945년 2월 16일)는 한국의 독립운동가, 시인, 작가이다.";

    private static final String sampleRes = "{\"result\":0,\"return_object\":{\"doc_id\":\"\",\"DCT\":\"\",\"category\":\"\",\"category_weight\":0,\"title\":{\"text\":\"\",\"NE\":\"\"},\"metaInfo\":{},\"paragraphInfo\":[],\"sentence\":[{\"id\":0,\"reserve_str\":\"\",\"text\":\"윤동주(尹東柱, 1917년 12월 30일 ~ 1945년 2월 16일)는 한국의 독립운동가, 시인, 작가이다.\",\"morp\":[{\"id\":0,\"lemma\":\"윤동주\",\"type\":\"NNP\",\"position\":0,\"weight\":0.0847864},{\"id\":1,\"lemma\":\"(\",\"type\":\"SS\",\"position\":9,\"weight\":1},{\"id\":2,\"lemma\":\"尹東柱\",\"type\":\"SH\",\"position\":10,\"weight\":1},{\"id\":3,\"lemma\":\",\",\"type\":\"SP\",\"position\":19,\"weight\":1},{\"id\":4,\"lemma\":\"1917\",\"type\":\"SN\",\"position\":21,\"weight\":1},{\"id\":5,\"lemma\":\"년\",\"type\":\"NNB\",\"position\":25,\"weight\":0.13405},{\"id\":6,\"lemma\":\"12\",\"type\":\"SN\",\"position\":29,\"weight\":1},{\"id\":7,\"lemma\":\"월\",\"type\":\"NNB\",\"position\":31,\"weight\":0.130006},{\"id\":8,\"lemma\":\"30\",\"type\":\"SN\",\"position\":35,\"weight\":1},{\"id\":9,\"lemma\":\"일\",\"type\":\"NNB\",\"position\":37,\"weight\":0.1015},{\"id\":10,\"lemma\":\"~\",\"type\":\"SO\",\"position\":41,\"weight\":1},{\"id\":11,\"lemma\":\"1945\",\"type\":\"SN\",\"position\":43,\"weight\":1},{\"id\":12,\"lemma\":\"년\",\"type\":\"NNB\",\"position\":47,\"weight\":0.123236},{\"id\":13,\"lemma\":\"2\",\"type\":\"SN\",\"position\":51,\"weight\":1},{\"id\":14,\"lemma\":\"월\",\"type\":\"NNB\",\"position\":52,\"weight\":0.148254},{\"id\":15,\"lemma\":\"16\",\"type\":\"SN\",\"position\":56,\"weight\":1},{\"id\":16,\"lemma\":\"일\",\"type\":\"NNB\",\"position\":58,\"weight\":0.100599},{\"id\":17,\"lemma\":\")\",\"type\":\"SS\",\"position\":61,\"weight\":1},{\"id\":18,\"lemma\":\"는\",\"type\":\"JX\",\"position\":62,\"weight\":0.141142},{\"id\":19,\"lemma\":\"한국\",\"type\":\"NNP\",\"position\":66,\"weight\":0.343648},{\"id\":20,\"lemma\":\"의\",\"type\":\"JKG\",\"position\":72,\"weight\":0.141972},{\"id\":21,\"lemma\":\"독립\",\"type\":\"NNG\",\"position\":76,\"weight\":0.241722},{\"id\":22,\"lemma\":\"운동\",\"type\":\"NNG\",\"position\":82,\"weight\":0.241722},{\"id\":23,\"lemma\":\"가\",\"type\":\"XSN\",\"position\":88,\"weight\":0.241722},{\"id\":24,\"lemma\":\",\",\"type\":\"SP\",\"position\":91,\"weight\":1},{\"id\":25,\"lemma\":\"시인\",\"type\":\"NNG\",\"position\":93,\"weight\":0.11476},{\"id\":26,\"lemma\":\",\",\"type\":\"SP\",\"position\":99,\"weight\":1},{\"id\":27,\"lemma\":\"작가\",\"type\":\"NNG\",\"position\":101,\"weight\":0.0783636},{\"id\":28,\"lemma\":\"이\",\"type\":\"VCP\",\"position\":107,\"weight\":0.0899771},{\"id\":29,\"lemma\":\"다\",\"type\":\"EF\",\"position\":110,\"weight\":0.136043},{\"id\":30,\"lemma\":\".\",\"type\":\"SF\",\"position\":113,\"weight\":1}],\"morp_eval\":[{\"id\":0,\"result\":\"윤동주/NNG+(/SS+尹東柱/SH+,/SP\",\"target\":\"윤동주(尹東柱,\",\"word_id\":0,\"m_begin\":0,\"m_end\":3},{\"id\":1,\"result\":\"1917/SN+년/NNB\",\"target\":\"1917년\",\"word_id\":1,\"m_begin\":4,\"m_end\":5},{\"id\":2,\"result\":\"12/SN+월/NNB\",\"target\":\"12월\",\"word_id\":2,\"m_begin\":6,\"m_end\":7},{\"id\":3,\"result\":\"30/SN+일/NNB\",\"target\":\"30일\",\"word_id\":3,\"m_begin\":8,\"m_end\":9},{\"id\":4,\"result\":\"~/SO\",\"target\":\"~\",\"word_id\":4,\"m_begin\":10,\"m_end\":10},{\"id\":5,\"result\":\"1945/SN+년/NNB\",\"target\":\"1945년\",\"word_id\":5,\"m_begin\":11,\"m_end\":12},{\"id\":6,\"result\":\"2/SN+월/NNB\",\"target\":\"2월\",\"word_id\":6,\"m_begin\":13,\"m_end\":14},{\"id\":7,\"result\":\"16/SN+일/NNB+)/SS+는/JX\",\"target\":\"16일)는\",\"word_id\":7,\"m_begin\":15,\"m_end\":18},{\"id\":8,\"result\":\"한국/NNG+의/JKG\",\"target\":\"한국의\",\"word_id\":8,\"m_begin\":19,\"m_end\":20},{\"id\":9,\"result\":\"독립운동가/NNG+,/SP\",\"target\":\"독립운동가,\",\"word_id\":9,\"m_begin\":21,\"m_end\":24},{\"id\":10,\"result\":\"시인/NNG+,/SP\",\"target\":\"시인,\",\"word_id\":10,\"m_begin\":25,\"m_end\":26},{\"id\":11,\"result\":\"작가/NNG+이/VCP+다/EF+./SF\",\"target\":\"작가이다.\",\"word_id\":11,\"m_begin\":27,\"m_end\":30}],\"WSD\":[{\"id\":0,\"text\":\"윤동주\",\"type\":\"NNP\",\"scode\":\"00\",\"weight\":20,\"position\":0,\"begin\":0,\"end\":0},{\"id\":1,\"text\":\"(\",\"type\":\"SS\",\"scode\":\"00\",\"weight\":1,\"position\":9,\"begin\":1,\"end\":1},{\"id\":2,\"text\":\"尹東柱\",\"type\":\"SH\",\"scode\":\"00\",\"weight\":1,\"position\":10,\"begin\":2,\"end\":2},{\"id\":3,\"text\":\",\",\"type\":\"SP\",\"scode\":\"00\",\"weight\":1,\"position\":19,\"begin\":3,\"end\":3},{\"id\":4,\"text\":\"1917\",\"type\":\"SN\",\"scode\":\"00\",\"weight\":1,\"position\":21,\"begin\":4,\"end\":4},{\"id\":5,\"text\":\"년\",\"type\":\"NNB\",\"scode\":\"02\",\"weight\":3.3,\"position\":25,\"begin\":5,\"end\":5},{\"id\":6,\"text\":\"12\",\"type\":\"SN\",\"scode\":\"00\",\"weight\":1,\"position\":29,\"begin\":6,\"end\":6},{\"id\":7,\"text\":\"월\",\"type\":\"NNB\",\"scode\":\"02\",\"weight\":1,\"position\":31,\"begin\":7,\"end\":7},{\"id\":8,\"text\":\"30\",\"type\":\"SN\",\"scode\":\"00\",\"weight\":1,\"position\":35,\"begin\":8,\"end\":8},{\"id\":9,\"text\":\"일\",\"type\":\"NNB\",\"scode\":\"07\",\"weight\":1,\"position\":37,\"begin\":9,\"end\":9},{\"id\":10,\"text\":\"~\",\"type\":\"SO\",\"scode\":\"00\",\"weight\":1,\"position\":41,\"begin\":10,\"end\":10},{\"id\":11,\"text\":\"1945\",\"type\":\"SN\",\"scode\":\"00\",\"weight\":1,\"position\":43,\"begin\":11,\"end\":11},{\"id\":12,\"text\":\"년\",\"type\":\"NNB\",\"scode\":\"02\",\"weight\":1,\"position\":47,\"begin\":12,\"end\":12},{\"id\":13,\"text\":\"2\",\"type\":\"SN\",\"scode\":\"00\",\"weight\":1,\"position\":51,\"begin\":13,\"end\":13},{\"id\":14,\"text\":\"월\",\"type\":\"NNB\",\"scode\":\"02\",\"weight\":3.3,\"position\":52,\"begin\":14,\"end\":14},{\"id\":15,\"text\":\"16\",\"type\":\"SN\",\"scode\":\"00\",\"weight\":1,\"position\":56,\"begin\":15,\"end\":15},{\"id\":16,\"text\":\"일\",\"type\":\"NNB\",\"scode\":\"07\",\"weight\":6.5,\"position\":58,\"begin\":16,\"end\":16},{\"id\":17,\"text\":\")\",\"type\":\"SS\",\"scode\":\"00\",\"weight\":1,\"position\":61,\"begin\":17,\"end\":17},{\"id\":18,\"text\":\"는\",\"type\":\"JX\",\"scode\":\"00\",\"weight\":1,\"position\":62,\"begin\":18,\"end\":18},{\"id\":19,\"text\":\"한국\",\"type\":\"NNP\",\"scode\":\"05\",\"weight\":6.6,\"position\":66,\"begin\":19,\"end\":19},{\"id\":20,\"text\":\"의\",\"type\":\"JKG\",\"scode\":\"00\",\"weight\":1,\"position\":72,\"begin\":20,\"end\":20},{\"id\":21,\"text\":\"독립\",\"type\":\"NNG\",\"scode\":\"00\",\"weight\":0,\"position\":76,\"begin\":21,\"end\":21},{\"id\":22,\"text\":\"운동가\",\"type\":\"NNG\",\"scode\":\"01\",\"weight\":6.7,\"position\":82,\"begin\":22,\"end\":23},{\"id\":23,\"text\":\",\",\"type\":\"SP\",\"scode\":\"00\",\"weight\":1,\"position\":91,\"begin\":24,\"end\":24},{\"id\":24,\"text\":\"시인\",\"type\":\"NNG\",\"scode\":\"10\",\"weight\":7.53333,\"position\":93,\"begin\":25,\"end\":25},{\"id\":25,\"text\":\",\",\"type\":\"SP\",\"scode\":\"00\",\"weight\":1,\"position\":99,\"begin\":26,\"end\":26},{\"id\":26,\"text\":\"작가\",\"type\":\"NNG\",\"scode\":\"01\",\"weight\":3.2,\"position\":101,\"begin\":27,\"end\":27},{\"id\":27,\"text\":\"이\",\"type\":\"VCP\",\"scode\":\"01\",\"weight\":1,\"position\":107,\"begin\":28,\"end\":28},{\"id\":28,\"text\":\"다\",\"type\":\"EF\",\"scode\":\"00\",\"weight\":1,\"position\":110,\"begin\":29,\"end\":29},{\"id\":29,\"text\":\".\",\"type\":\"SF\",\"scode\":\"00\",\"weight\":1,\"position\":113,\"begin\":30,\"end\":30}],\"word\":[{\"id\":0,\"text\":\"윤동주(尹東柱,\",\"type\":\"\",\"begin\":0,\"end\":3},{\"id\":1,\"text\":\"1917년\",\"type\":\"\",\"begin\":4,\"end\":5},{\"id\":2,\"text\":\"12월\",\"type\":\"\",\"begin\":6,\"end\":7},{\"id\":3,\"text\":\"30일\",\"type\":\"\",\"begin\":8,\"end\":9},{\"id\":4,\"text\":\"~\",\"type\":\"\",\"begin\":10,\"end\":10},{\"id\":5,\"text\":\"1945년\",\"type\":\"\",\"begin\":11,\"end\":12},{\"id\":6,\"text\":\"2월\",\"type\":\"\",\"begin\":13,\"end\":14},{\"id\":7,\"text\":\"16일)는\",\"type\":\"\",\"begin\":15,\"end\":18},{\"id\":8,\"text\":\"한국의\",\"type\":\"\",\"begin\":19,\"end\":20},{\"id\":9,\"text\":\"독립운동가,\",\"type\":\"\",\"begin\":21,\"end\":24},{\"id\":10,\"text\":\"시인,\",\"type\":\"\",\"begin\":25,\"end\":26},{\"id\":11,\"text\":\"작가이다.\",\"type\":\"\",\"begin\":27,\"end\":30}],\"NE\":[{\"id\":0,\"text\":\"윤동주\",\"type\":\"PS_NAME\",\"begin\":0,\"end\":0,\"weight\":0.378635,\"common_noun\":0},{\"id\":1,\"text\":\"尹東柱\",\"type\":\"PS_NAME\",\"begin\":2,\"end\":2,\"weight\":1,\"common_noun\":0},{\"id\":2,\"text\":\"1917년 12월 30일 ~ 1945년 2월 16일\",\"type\":\"DT_DURATION\",\"begin\":4,\"end\":16,\"weight\":1,\"common_noun\":0},{\"id\":3,\"text\":\"한국\",\"type\":\"LCP_COUNTRY\",\"begin\":19,\"end\":19,\"weight\":0.580454,\"common_noun\":0},{\"id\":4,\"text\":\"독립운동가\",\"type\":\"CV_OCCUPATION\",\"begin\":21,\"end\":23,\"weight\":0.581843,\"common_noun\":0},{\"id\":5,\"text\":\"시인\",\"type\":\"CV_OCCUPATION\",\"begin\":25,\"end\":25,\"weight\":0.659815,\"common_noun\":0},{\"id\":6,\"text\":\"작가\",\"type\":\"CV_OCCUPATION\",\"begin\":27,\"end\":27,\"weight\":0.447266,\"common_noun\":0}],\"NE_Link\":[],\"chunk\":[],\"dependency\":[],\"phrase_dependency\":[],\"SRL\":[],\"relation\":[],\"SA\":[],\"ZA\":[]}],\"entity\":[]}}\n";

    @Test
    @DisplayName("단일 STT 실행")
    void executeSTT() {

        // return val
        String res = null;

        // set parameters for STT request
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        argument.put("text", sampleText);
        argument.put("analysis_code", ANALYSIS_CODE);
        request.put("argument", argument);

        // send req to STT Server
        try {
            // repeat for ETRI Key length
            for (int i = 0; i < 1; i++) {

                // set connection
                URL url = new URL(ETRI_STT_API_URL + ETRI_STT_API_SUB_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("Authorization", ETRI_API_KEY[i]);

                // set response type -> json
                Gson gson = new Gson();
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.write(gson.toJson(request).getBytes("UTF-8"));
                dataOutputStream.flush();
                dataOutputStream.close();

                // get response
                int responseCode = connection.getResponseCode();
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();

                String responseBodyJson = new String(sb);
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                responseBodyJson = sb.toString();

                if (responseCode != 200) {
                    continue;
                    // throw new RuntimeException();
                }

                System.out.println(responseBodyJson);
            }


        } catch (MalformedURLException e) {
            // TODO : logging
            e.printStackTrace();
            fail();
        } catch (IOException e) {
            // TODO : logging
            e.printStackTrace();
            fail();
        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
            fail();
        }

    } // end of SttThread.excuteSTT(filePath)

    @Test
    @DisplayName("결과 JSON Parsing")
    void parseResJson() {

        Map<String, Object> responseBody = new Gson().fromJson(sampleRes, Map.class);
        Map<String, Object> returnObject = (Map<String, Object>) responseBody.get("return_object");
        List<Map> sentences = (List<Map>) returnObject.get("sentence");

        Float totalSum = 0F;
        Map<String, Float> morphemesMap = new HashMap<>(); // 결과 저장용 HashMap

        for ( Map<String, Object> sentence : sentences ) {
            // 개체명 분석 결과 수집 및 정렬
            List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
            for( Map<String, Object> nameEntityInfo : nameEntityRecognitionResult ) {

                String type = (String) nameEntityInfo.get("type");
                Float weight = Float.valueOf(((Double) nameEntityInfo.get("weight"))+"");

                if (morphemesMap.containsKey(type)) {
                    Float newWeight = morphemesMap.get(type) + weight;
                    morphemesMap.put(type, newWeight);
                } else {
                    morphemesMap.put(type, weight);
                }

                totalSum += weight;
            }
        }

        // 후처리 : perc 조절
        for (String type : morphemesMap.keySet()) {
            Float newWeight = morphemesMap.get(type) / totalSum;
            morphemesMap.put(type, newWeight);
        }

        for ( String key : morphemesMap.keySet() ) {
            System.out.println(key + " : " + morphemesMap.get(key));
        }
    }
}