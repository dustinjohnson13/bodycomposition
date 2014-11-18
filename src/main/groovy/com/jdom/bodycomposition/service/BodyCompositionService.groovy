package com.jdom.bodycomposition.service

import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.DailyTrend
import com.jdom.bodycomposition.domain.TrendMetrics

/**
 * Created by djohnson on 11/14/14.
 */
interface BodyCompositionService {

    List<DailyEntry> getLastSevenEntries()

    DailyEntry getNewestEntry()

    void saveEntry(DailyEntry entry)

    DailyEntry findByDate(Date date)

    DailyTrend findTrendByDate(Date date)

    List<TrendMetrics> getTrendMetricsToDisplay()

    List<DailyEntry> getSimilarDays()
}