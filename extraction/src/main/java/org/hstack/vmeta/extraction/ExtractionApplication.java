package org.hstack.vmeta.extraction;

import org.hstack.vmeta.extraction.audio.AudioExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExtractionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExtractionApplication.class, args);
	}

}
