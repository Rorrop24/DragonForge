
package com.rng_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class RollResultDTO {
    private int total;
    private List<Integer> individualRolls;
    private int modifier;

    // Opcionales para saber si sacaste 20 natural o pifia (1)
    private boolean criticalSuccess;
    private boolean criticalFail;
}