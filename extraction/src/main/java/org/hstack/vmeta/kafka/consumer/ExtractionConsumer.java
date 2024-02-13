package org.hstack.vmeta.kafka.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hstack.vmeta.extraction.ExtractionService;
import org.hstack.vmeta.extraction.MetadataDTO;
import org.hstack.vmeta.kafka.producer.ExtractionCompletedProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExtractionConsumer {

    @Autowired
    private ExtractionService extractionService;

    @Autowired
    private ExtractionCompletedProducer extractionCompletedProducer;

    @KafkaListener(topics="Extraction", groupId="Group")
    public void listener(String data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> dataMap = objectMapper.readValue(data, new TypeReference<Map<String, String>>() {});

            Long id = Long.parseLong(dataMap.get("id"));
            String title = dataMap.get("title");
            String filePath = dataMap.get("filePath");
            MetadataDTO metadataDTO = extractionService.extractMetadataDTO(id, title, filePath);
            extractionCompletedProducer.produce(metadataDTO);

        } catch (Exception e) {
            e.printStackTrace();
            extractionCompletedProducer.produce(null);
        }
    }
}
