
package com.DragonForge.rng_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class RollResultDTO {
    private int total;
    private List<Integer> individualRolls;
    private int modifier;
    private boolean criticalSuccess;
    private boolean criticalFail;
}