package org.hstack.vmeta.extraction.scene;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hstack.vmeta.extraction.scene.narrative.Narrative;
import org.hstack.vmeta.extraction.scene.presentation.Presentation;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SceneDTO {

    private Narrative narrative;

    private Presentation presentation;

}
