package com.jdom.bodycomposition.service

import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.DailyTrend
import com.jdom.bodycomposition.domain.TrendMetrics
import com.jdom.util.TimeUtil
import org.springframework.beans.factory.annotation.Autowired
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
    List<DailyEntry> getWeeksWorthOfEntries() {

        Calendar cal = TimeUtil.newCalendar()
        Date end = cal.getTime();

        cal.add(Calendar.DAY_OF_YEAR, -6);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        Date start = cal.getTime();

        return dailyEntryDao.findByDateBetween(start, end)
    }

    @Override
    DailyEntry getNewestEntry() {
        def list = getWeeksWorthOfEntries();

        return list.get(list.size() - 1);
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
        List<DailyTrend> all = dailyTrendDao.findAll()
        if (!all.isEmpty()) {
            previous = all.get(all.size() - 1)
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

        for (i in [7, 14, 30, 60, 90]) {
            cal.setTime(newestTrend.date)
            cal.add(Calendar.DAY_OF_YEAR, -i)

            Date date = cal.getTime()

            DailyTrend trend = dailyTrendDao.findByDate(date)

            results += trendService.calculateTrendMetrics(trend, newestTrend)
        }

        return results
    }

    @Override
    List<DailyEntry> getSimilarDays() {
        DailyEntry newest = getNewestEntry();
        
        def entries = dailyEntryDao.findByWaterPercentage(newest.waterPercentage)
        if (entries.size() > 10) {
            entries = entries.subList(entries.size() - 11, entries.size())
        }
        return entries
    }
}
