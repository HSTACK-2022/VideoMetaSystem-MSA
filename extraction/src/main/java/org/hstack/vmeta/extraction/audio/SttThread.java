package org.hstack.vmeta.extraction.audio;

import com.google.gson.Gson;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class SttThread extends Thread {

    private String apiKey;

    private static final String ETRI_STT_API_URL = "http://aiopen.etri.re.kr:8000/WiseASR/Recognition";

    private static final String LANGUAGE_CODE = "korean";     // 언어 코드


    private int keyNum;     // API Key num
    private Stack<String> filePathStack;    // filePaths that have to extract by STT Server
    private Map<Integer, String> scriptMap; // result Map


    /* constructor
     *
     * @param
     * - keyNum : STT Key num
     * @returnVal
     * - script str
     */
    public SttThread(String apiKey) {
        this.apiKey = apiKey;
        scriptMap = new TreeMap<>();
        filePathStack = new Stack<>();
    }

    /*
     * getter
     */
    public Map<Integer, String> getThreadResult() {
        return scriptMap;
    }

    /*
     * filePathStack에 filePath를 추가한다.
     */
    public void pushFilePath(String filePath) {
        filePathStack.push(filePath);
    }

    /*
     * filePathStack에 저장된 filePath에 대해 STT Server를 호출한다.
     */
    @Override
    public void run() {
        scriptMap = new TreeMap<>();
        while(!filePathStack.isEmpty()) {
            String currFilePath = filePathStack.pop();
            String res = executeSTT(currFilePath);

            // calculate time by file name
            String fileName = new File(currFilePath).getName().split("\\.")[0];

            int timeSec = Integer.valueOf(fileName);
            scriptMap.put(timeSec, res);
        }
    } // end of SttThread.run()

    /*
     * filePath에 해당하는 오디오 파일에 대해 STT 작업을 수행한다.
     * @param
     * - filePath : 음성 파일 경로
     * @returnVal
     * - script str
     */
    private String executeSTT(String filePath) {

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
            // random wait - for avoid 429 Error
            sleep((long) (100L*filePathStack.size()*Math.random()));

            // set connection
            URL url = new URL(ETRI_STT_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", apiKey);

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
            } else {
                filePathStack.push(filePath);
            }


        } catch (MalformedURLException e) {
            // TODO : logging
            e.printStackTrace();
        } catch (IOException e) {       // 429, 504 : API Call Error
            // TODO : logging
            e.printStackTrace();
            filePathStack.push(filePath);
        } catch (Exception e) {
            // TODO : logging
            e.printStackTrace();
        } finally {
            return res;
        }

    } // end of SttThread.excuteSTT(filePath)

} // end of class SttThread