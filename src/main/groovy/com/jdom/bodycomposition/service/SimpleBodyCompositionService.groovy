package com.jdom.bodycomposition.service
import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.DailyTrend
import com.jdom.bodycomposition.domain.TrendMetrics
import com.jdom.util.TimeUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

import javax.transaction.Transactional
/**
 * Created by djohnson on 11/14/14.
 */
@Service
@Transactional
class SimpleBodyCompositionService implements BodyCompositionService {

    @Autowired DailyEntryDao dailyEntryDao
    @Autowired DailyTrendDao dailyTrendDao
    @Autowired TrendService trendService

    @Override
    List<DailyEntry> getLastSevenEntries() {
        Pageable lastSeven = new PageRequest(0, 7, Sort.Direction.DESC, 'date')
        Page page = dailyEntryDao.findAll(lastSeven)

        return page.getContent().reverse()
    }

    @Override
    DailyEntry getNewestEntry() {
        Pageable latest = new PageRequest(0, 1, Sort.Direction.DESC, 'date')
        Page page = dailyEntryDao.findAll(latest)

        return page.getContent()[0]
    }

    @Override
    void saveEntry(DailyEntry entry) {
        Calendar cal = TimeUtil.newCalendar()
        cal.setTime(entry.getDate())
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        entry.setDate(cal.getTime())

        dailyEntryDao.save(entry)

        DailyTrend previous = null
        List<DailyTrend> content = dailyTrendDao.findAll(new PageRequest(0, 1, Sort.Direction.DESC, 'date')).getContent()
        if (!content.isEmpty()) {
            previous = content[0]
        }

        DailyTrend trend = trendService.calculateDailyTrend(previous, entry)
        dailyTrendDao.save(trend)
    }

    @Override
    DailyEntry findByDate(Date date) {
        return dailyEntryDao.findByDate(date)
    }

    @Override
    DailyTrend findTrendByDate(Date date) {
        return dailyTrendDao.findByDate(date)
    }

    @Override
    List<TrendMetrics> getTrendMetricsToDisplay() {
        List<TrendMetrics> results = []
        DailyEntry newest = getNewestEntry();
        DailyTrend newestTrend = dailyTrendDao.findByDate(newest.date)

        Calendar cal = Calendar.getInstance()

        for (i in [7, 14, 30, 60, 90, 180, 365]) {
            cal.setTime(newestTrend.date)
            cal.add(Calendar.DAY_OF_YEAR, -i)

            Date date = cal.getTime()

            DailyTrend trend = dailyTrendDao.findByDate(date)

            if (trend != null) {
                results += trendService.calculateTrendMetrics(trend, newestTrend)
            }
        }

        return results
    }

    @Override
    List<DailyEntry> getSimilarDays() {
        DailyEntry newest = getNewestEntry();

        Pageable recentEleven = new PageRequest(0, 11);

        def entries = dailyEntryDao.findByWaterPercentageOrderByDateDesc(newest.waterPercentage, recentEleven)
        return entries.findAll{ it.id != newest.id }.reverse()
    }
}
