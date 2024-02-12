package org.hstack.vmeta.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hstack.vmeta.extraction.MetadataDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExtractionCompletedProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void produce(Object data) {
        try {
            if (data.getClass() == MetadataDTO.class) {
                MetadataDTO metadataDTO = (MetadataDTO) data;
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonStr = objectMapper.writeValueAsString(metadataDTO);
                kafkaTemplate.send("ExtractionCompleted", jsonStr);
            } else {
                kafkaTemplate.send("ExtractionCompleted", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
