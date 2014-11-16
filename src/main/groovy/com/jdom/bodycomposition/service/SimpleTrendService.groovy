package com.jdom.bodycomposition.service
import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.DailyTrend
import com.jdom.bodycomposition.domain.TrendMetrics
import org.springframework.stereotype.Service
/**
 * Created by djohnson on 11/16/14.
 */
@Service
class SimpleTrendService implements TrendService {

    static final double TREND_SMOOTHING_FACTOR = 6.0

    @Override
    DailyTrend calculateDailyTrend(DailyTrend previous, DailyEntry newEntry) {
        if (previous == null) {
            return new DailyTrend(date: newEntry.date, weight: newEntry.weight, bodyFat: newEntry.bodyFat, waterPercentage: newEntry.waterPercentage)
        }

        def weight = calculateTrend(previous.weight, newEntry.weight)
        def bodyFat = calculateTrend(previous.bodyFat, newEntry.bodyFat)
        def waterPercentage = calculateTrend(previous.waterPercentage, newEntry.waterPercentage)

        return new DailyTrend(date: newEntry.date, weight: weight, bodyFat: bodyFat, waterPercentage: waterPercentage)
    }

    @Override
    TrendMetrics calculateTrendMetrics(DailyTrend start, DailyTrend end) {
        TrendMetrics metrics = new TrendMetrics(start: start.date, end: end.date,
            weightDifference: roundToOneDecimal(end.weight - start.weight), bodyFatDifference: roundToOneDecimal(end.bodyFat - start.bodyFat),
            waterPercentageDifference: roundToOneDecimal(end.waterPercentage - start.waterPercentage))

        return metrics
    }

    private double calculateTrend(double previousTrend, double newValue) {
        return roundToOneDecimal(((newValue - previousTrend) / TREND_SMOOTHING_FACTOR) + previousTrend)
    }

    private double roundToOneDecimal(double value) {
        return Math.round(value * 10) / 10
    }
}
