package com.loans.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Calculator {
    INSTANCE;

    public static BigDecimal calculateAmount(BigDecimal amount, BigDecimal percentage) {
        BigDecimal percentageAmount = BigDecimal.valueOf(amount.doubleValue() * (percentage.doubleValue() / 100));

        return amount.add(percentageAmount)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
