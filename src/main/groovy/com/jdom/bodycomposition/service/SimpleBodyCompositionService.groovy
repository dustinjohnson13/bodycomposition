package com.jdom.bodycomposition.service
import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.DailyTrend
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
    void saveEntry(DailyEntry entry) {
        Calendar cal = TimeUtil.newCalendar()
        cal.setTime(entry.getDate())
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        entry.setDate(cal.getTime())

        dailyEntryDao.save(entry)

        List<DailyTrend> all = dailyTrendDao.findAll()
        DailyTrend previous = all.get(all.size() - 1)

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
}
