package com.jdom.bodycomposition.service
import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.DailyTrend
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

    private double calculateTrend(double previousTrend, double newValue) {
        return Math.round((((newValue - previousTrend) / TREND_SMOOTHING_FACTOR) + previousTrend) * 10) / 10
    }
}
