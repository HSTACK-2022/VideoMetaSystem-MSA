package org.hstack.vmeta.extraction.audio;


import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

class STTCalculator {

    @Value("${etri.keys}")
    private static String[] ETRI_API_KEY;

    private static final String ETRI_STT_API_URL = "http://aiopen.etri.re.kr:8000/WiseASR/Recognition";

    private static final String LANGUAGE_CODE = "korean";     // 언어 코드

    /*
     * filePath에 해당하는 오디오 파일에 대해 STT 작업을 수행한다.
     * @param
     * - filePath : 음성 파일 경로
     * @returnVal
     * - script str
     */
    public String executeSTT(String filePath, int keyNum) {

        // return val
        String res = null;

        // audioFile to Base64
        String audioContents = null;

        try {
            Path path = Paths.get(filePath);
            byte[] audioBytes = Files.readAllBytes(path);
            audioContents = Base64.getEncoder().encodeToString(audioBytes);
        } catch (IOException e) {
            // TODO : logging
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // set parameters for STT request
        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();

        argument.put("language_code", LANGUAGE_CODE);
        argument.put("audio", audioContents);
        request.put("argument", argument);

        // send req to STT Server
        try {
            // set connection
            URL url = new URL(ETRI_STT_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", ETRI_API_KEY[keyNum]);

            // set response type -> json
            Gson gson = new Gson();
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(gson.toJson(request).getBytes("UTF-8"));
            dataOutputStream.flush();
            dataOutputStream.close();

            // get response
            int responseCode = connection.getResponseCode();
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[inputStream.available()];
            int byteRead = inputStream.read(buffer);
            String responseBody = new String(buffer);

            // TODO : logging
            System.out.println("["+responseCode+"] " + responseBody);

            if (responseCode == 200) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObjParent =
                        (JSONObject) jsonParser.parse(responseBody);
                JSONObject jsonObjChild =
                        (JSONObject) jsonParser.parse(jsonObjParent.get("return_object").toString());
                res = jsonObjChild.get("recognized").toString();
            }

        } catch (MalformedURLException e) {
            // TODO : logging
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            // TODO : logging
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return res;

    }
}
