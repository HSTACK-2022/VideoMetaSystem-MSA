package org.hstack.vmeta.extraction.category;

import com.google.gson.Gson;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.hstack.vmeta.extraction.keyword.KeywordDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class CategoryCalculator {

    @Value("${etri.keys}")
    private String[] ETRI_API_KEY;

    @Value("${etri.url}")
    private String ETRI_STT_API_URL;

    private static final String ETRI_STT_API_SUB_URL= "/WiseNLU";

    private static final String ANALYSIS_CODE = "ner";     // 언어 분석 코드


    /*
     * [executeSTT]
     *  > 형태소 분석 STT 호출
     * @param
     * - text : 추출 대상 단어 모음
     * @returnVal
     * - Map<CategoryType, perc>
     */
    String[] executeSTT(String text) {

        // return val
        String[] res = new String[ETRI_API_KEY.length];

        // set parameters for STT request
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        argument.put("analysis_code", ANALYSIS_CODE);
        argument.put("text", text);

        request.put("argument", argument);

        // send req to STT Server
        try {
            // repeat for ETRI Key length
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

                // response storing
                if (responseCode == 200) {
                    res[i] = responseBodyJson;
                } else {
                    res[i] = null;
                }
            }

            return res;

        } catch (MalformedURLException e) {
            // TODO : logging
            e.printStackTrace();
        } catch (IOException e) {       // 429, 504 : API Call Error
            // TODO : logging
            e.printStackTrace();
        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
        } finally {
            return res;
        }

    } // end of SttThread.excuteSTT(text)

    /*
     * [parseRes2MorphMap]
     *  > STT 결과를 바탕으로 Map<개체명, 확률> 생성
     * @param
     * - res : executeSTT로 얻은 결과 String[]
     * @returnVal
     * - Map<CategoryTypeStr, perc>
     */
    Map<String, Float> parseRes2MorphMap(String[] res) {

        float totalSum = 0F;
        Map<String, Float> morphemesMap = new HashMap<>(); // 결과 저장용 HashMap

        for (int i = 0; i < res.length; i++) {
            Map<String, Object> responseBody = new Gson().fromJson(res[i], Map.class);
            Map<String, Object> returnObject = (Map<String, Object>) responseBody.get("return_object");
            List<Map> sentences = (List<Map>) returnObject.get("sentence");

            // 개체명 분석 결과 수집 및 정렬
            for (Map<String, Object> sentence : sentences) {
                List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
                for (Map<String, Object> nameEntityInfo : nameEntityRecognitionResult ) {
                    String type = (String) nameEntityInfo.get("type");
                    Float weight = Float.valueOf(((Double) nameEntityInfo.get("weight"))+"");

                    if (morphemesMap.containsKey(type)) {
                        Float newWeight = morphemesMap.get(type) + weight;
                        morphemesMap.put(type, newWeight);
                    } else {
                        morphemesMap.put(type, weight);
                    }

                    totalSum += weight;
                } // End of for(nameEntityInfo)
            } // End of for(sentence)
        } // End of for(res)

        // 후처리 : perc 조절
        for (String type : morphemesMap.keySet()) {
            Float newWeight = morphemesMap.get(type) / totalSum;
            morphemesMap.put(type, newWeight);
        }
        
        return morphemesMap;
        
    } // End of parseRes2MorphMap(res)

    /*
     * [categoryMap2List]
     *  > categoryMap을 List로 변환
     *
     * @param
     * - categoryMap : API로 받은 결과값 (변환 완료 값)
     * @returnVal
     * - categoryList : 변환된 List<Category>
     */
    List<CategoryDTO.Category> categoryMap2List(Map<String, Float> categoryMap) {
        List<CategoryDTO.Category> categoryList = new ArrayList<>();
        for (String type : categoryMap.keySet()) {
            categoryList.add(CategoryDTO.Category
                    .builder()
                    .categoryType(type)
                    .perc(categoryMap.get(type))
                    .build());
        }
        return categoryList;
    }
}
