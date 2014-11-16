package com.jdom.bodycomposition.service

import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.DailyTrend

/**
 * Created by djohnson on 11/16/14.
 */
interface TrendService {

    DailyTrend calculateDailyTrend(DailyTrend previous, DailyEntry newEntry)

}