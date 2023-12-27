package com.rhymthwave.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountStatusADS {

    private  Long  total_pending;
    private  Long  total_reject;
}
