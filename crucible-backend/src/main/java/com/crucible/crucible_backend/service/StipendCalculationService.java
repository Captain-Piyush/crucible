package com.crucible.crucible_backend.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class StipendCalculationService {

    // Financial Model Constants
    private static final BigDecimal PLATFORM_FEE_PERCENTAGE = new BigDecimal("0.20"); // 20% total fee
    private static final BigDecimal STIPEND_POOL_PERCENTAGE = new BigDecimal("0.50"); // 50% of the platform fee goes to the pool

    // Rank-weighted split ratios based on the capped shortlist
    private static final BigDecimal[] SPLIT_1_RUNNER_UP = { new BigDecimal("1.00") };
    private static final BigDecimal[] SPLIT_2_RUNNERS_UP = { new BigDecimal("0.60"), new BigDecimal("0.40") };
    private static final BigDecimal[] SPLIT_3_RUNNERS_UP = { new BigDecimal("0.50"), new BigDecimal("0.30"), new BigDecimal("0.20") };

    /**
     * An immutable Java 21 Record holding the exact financial distribution for a Gig.
     */
    public record FinancialLedger(
            BigDecimal winnerPayout,
            BigDecimal platformRevenue,
            BigDecimal totalStipendPool,
            List<BigDecimal> runnerUpStipends
    ) {}

    /**
     * Calculates the deterministic payout split for a Gig based on the total budget and number of runners-up.
     */
    public FinancialLedger calculatePayouts(BigDecimal totalGigPayment, int numberOfRunnersUp) {

        // 1. Calculate the total platform fee (e.g., $20 out of a $100 payment)
        BigDecimal totalPlatformFee = totalGigPayment.multiply(PLATFORM_FEE_PERCENTAGE);

        // 2. The winner gets the payment minus the total fee (e.g., $80)
        BigDecimal winnerPayout = totalGigPayment.subtract(totalPlatformFee);

        // 3. Carve the stipend pool out of the platform fee (e.g., $10)
        BigDecimal stipendPool = totalPlatformFee.multiply(STIPEND_POOL_PERCENTAGE);

        // 4. The remainder of the fee is actual platform revenue (e.g., $10)
        BigDecimal platformRevenue = totalPlatformFee.subtract(stipendPool);

        // 5. Calculate exact stipends for the non-winning finalists based on rank
        List<BigDecimal> runnerUpStipends = new ArrayList<>();
        if (numberOfRunnersUp > 0) {
            BigDecimal[] splitRatios = getSplitRatios(numberOfRunnersUp);

            for (int i = 0; i < numberOfRunnersUp && i < splitRatios.length; i++) {
                // HALF_EVEN is the banking standard for rounding currency
                BigDecimal exactStipend = stipendPool.multiply(splitRatios[i]).setScale(2, RoundingMode.HALF_EVEN);
                runnerUpStipends.add(exactStipend);
            }
        }

        return new FinancialLedger(winnerPayout, platformRevenue, stipendPool, runnerUpStipends);
    }

    private BigDecimal[] getSplitRatios(int numberOfRunnersUp) {
        if (numberOfRunnersUp == 1) return SPLIT_1_RUNNER_UP;
        if (numberOfRunnersUp == 2) return SPLIT_2_RUNNERS_UP;
        return SPLIT_3_RUNNERS_UP;
    }
}