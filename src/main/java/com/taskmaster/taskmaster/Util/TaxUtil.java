package com.taskmaster.taskmaster.Util;

import com.taskmaster.taskmaster.entity.Study;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TaxUtil {

    public static Double countPPN(Study study) {
        Double ppnRate = 11.0;

        return (study.getPrice() * ppnRate) / 100;
    }

}
