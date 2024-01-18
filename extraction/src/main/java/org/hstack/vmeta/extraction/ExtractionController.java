package org.hstack.vmeta.extraction;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/extraction")
public class ExtractionController {

    @PostMapping
    public void extractMetadata(
            @RequestParam Long id,
            @RequestParam String filePath
    ) {
    }

}
