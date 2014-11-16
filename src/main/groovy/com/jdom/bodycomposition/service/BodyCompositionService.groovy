package com.jdom.bodycomposition.service

import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.DailyTrend

/**
 * Created by djohnson on 11/14/14.
 */
interface BodyCompositionService {

    List<DailyEntry> getWeeksWorthOfEntries()

    void saveEntry(DailyEntry entry)

    DailyEntry findByDate(Date date)

    DailyTrend findTrendByDate(Date date)
}