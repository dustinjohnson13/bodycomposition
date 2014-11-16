package com.jdom.bodycomposition.service

import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.DailyTrend
import com.jdom.bodycomposition.domain.TrendMetrics
import com.jdom.util.TimeUtil
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by djohnson on 11/16/14.
 */
class SimpleTrendServiceSpec extends Specification {

    SimpleTrendService service = new SimpleTrendService()

    @Unroll
    def 'should calculate daily trend correctly'() {

        when: 'the trend is calculated'
        DailyTrend trend = service.calculateDailyTrend(previousTrend, newEntry)

        then: 'the trend is correct'
        trend == expectedResult

        where:
        previousTrend                                                       | newEntry                                                                                                             | expectedResult
        null                                                                | new DailyEntry(date: TimeUtil.newDate(), weight: 135.5, bodyFat: 13.5, waterPercentage: 62.3)                        | new DailyTrend(date: TimeUtil.newDate(), weight: 135.5, bodyFat: 13.5, waterPercentage: 62.3)
        new DailyTrend(weight: 138.2, bodyFat: 13.3, waterPercentage: 62.2) | new DailyEntry(date: TimeUtil.dateFromDashString('2014-11-11'), weight: 140, bodyFat: 13.7, waterPercentage: 61.8)   | new DailyTrend(date: TimeUtil.dateFromDashString('2014-11-11'), weight: 138.5, bodyFat: 13.4, waterPercentage: 62.1)
        new DailyTrend(weight: 138.5, bodyFat: 13.4, waterPercentage: 62.1) | new DailyEntry(date: TimeUtil.dateFromDashString('2014-11-12'), weight: 141.2, bodyFat: 14.2, waterPercentage: 61.2) | new DailyTrend(date: TimeUtil.dateFromDashString('2014-11-12'), weight: 139, bodyFat: 13.5, waterPercentage: 62)
        new DailyTrend(weight: 139, bodyFat: 13.5, waterPercentage: 62)     | new DailyEntry(date: TimeUtil.dateFromDashString('2014-11-13'), weight: 136.8, bodyFat: 13.6, waterPercentage: 61.6) | new DailyTrend(date: TimeUtil.dateFromDashString('2014-11-13'), weight: 138.6, bodyFat: 13.5, waterPercentage: 61.9)
    }

    def 'should calculate trend metrics correctly'() {

        def start = new DailyTrend(date: TimeUtil.dateFromDashString('2014-11-02'), weight: 138.2, bodyFat: 13.3, waterPercentage: 62.2)
        def end = new DailyTrend(date: TimeUtil.dateFromDashString('2014-11-09'), weight: 139.3, bodyFat: 12.7, waterPercentage: 61.4)
        def expectedMetrics = new TrendMetrics(start: TimeUtil.dateFromDashString('2014-11-02'), end: TimeUtil.dateFromDashString('2014-11-09'), weightDifference: 1.1, bodyFatDifference: -0.6, waterPercentageDifference: -0.8)

        when: 'the trend metrics are calculated'
        def actual = service.calculateTrendMetrics(start, end)

        then: 'the metrics are correct'
        actual == expectedMetrics

        and: 'the number of days returns the correct value'
        actual.periodInDays == 7
    }

}
