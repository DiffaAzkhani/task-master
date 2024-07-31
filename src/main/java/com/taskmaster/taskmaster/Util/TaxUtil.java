package com.taskmaster.taskmaster.Util;

import com.taskmaster.taskmaster.entity.Study;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TaxUtil {

    public static int countPPN(Study study) {
        int ppnRate = 11;

        return (study.getPrice() * ppnRate) / 100;
    }

}
