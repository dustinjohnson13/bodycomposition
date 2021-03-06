package com.jdom.bodycomposition.service

import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.domain.TrendMetrics
import com.jdom.util.TimeUtil
import com.jdom.util.TimeUtilHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.transaction.TransactionConfiguration
import spock.lang.Specification

import javax.transaction.Transactional
import java.text.SimpleDateFormat

/**
 * Created by djohnson on 11/15/14.
 */
@ActiveProfiles(SpringProfiles.TEST)
@ContextConfiguration(classes = [BodyCompositionContext.class])
@Transactional
@TransactionConfiguration(defaultRollback = true)
class SimpleBodyCompositionServiceSpec extends Specification {

    @Autowired BodyCompositionService service

    def setup() {
        Calendar cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, Calendar.NOVEMBER)
        cal.set(Calendar.DAY_OF_MONTH, 15)
        cal.set(Calendar.YEAR, 2014)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        TimeUtilHelper.freezeTime(cal.getTime())
    }

    def cleanup() {
        TimeUtilHelper.resumeTime()
    }

    def 'should return last seven entries'() {

        def expectedDates = [
              '11/08/2014',
              '11/09/2014',
              '11/10/2014',
              '11/11/2014',
              '11/12/2014',
              '11/13/2014',
              '11/14/2014',
        ]

        when: 'entries for a week are retrieved'
        def results = service.getLastSevenEntries()

        then: 'the correct number of results were returned'
        results.size() == 7

        and: 'they have the correct dates'
        SimpleDateFormat format = new SimpleDateFormat('MM/dd/yyyy')
        def actual = results.collect{ format.format(it.date) }
        actual == expectedDates
    }

    def 'should save entry to the database'() {

        DailyEntry entry = new DailyEntry(date: TimeUtil.newDate(), weight: 135.5, bodyFat: 13.5, waterPercentage: 62.3)

        when: 'an entry is saved'
        service.saveEntry(entry)

        then: 'it can be retrieved'
        def persisted = service.findByDate(entry.date)
        persisted != null

        and: 'the values are correct'
        persisted == entry
    }

    def 'should save trend to the database'() {

        DailyEntry entry = new DailyEntry(date: TimeUtil.newDate(), weight: 143.2, bodyFat: 13.5, waterPercentage: 62.2)

        when: 'an entry is saved'
        service.saveEntry(entry)

        then: 'the daily trend can be retrieved'
        def persisted = service.findTrendByDate(entry.date)
        persisted != null

        and: 'the values are correct'
        persisted.date == entry.date
        persisted.weight == 139.7
        persisted.bodyFat == 13.7
        persisted.waterPercentage == 61.8
    }

    def 'should return newest entry'() {
        when: 'the newest entry is requested'
        def newest = service.getNewestEntry()
        def expected = TimeUtil.dateFromDashString('2014-11-14')

        then: 'the newest entry is returned'
        assert newest.date == expected
    }

    def 'should return metrics for 7,14,30,60,90,180,365 days'() {

        def expectedNumberOfDays = [
              7, 14, 30, 60, 90, 180, 365
        ]

        when: 'trend metrics are requested'
        List<TrendMetrics> metrics = service.getTrendMetricsToDisplay()

        then: 'the correct number of metrics were returned'
        metrics.size() == 7

        def actual = metrics.collect{ it.periodInDays }
        and: 'they had the correct periods'
        actual == expectedNumberOfDays
    }

    def 'should limit similar days to last 10'() {

        given: 'the entry for today has a frequent water percentage'
        DailyEntry entry = new DailyEntry(date: TimeUtil.newDate(), weight: 138, bodyFat: 13.5, waterPercentage: 63.9)
        service.saveEntry(entry)

        when: 'similar days are requested'
        def similarDays = service.getSimilarDays()

        then: 'all similar days have the same water percentage'
        similarDays.each {
            assert it.waterPercentage == entry.waterPercentage
        }

        and: 'the results are limited to 10 entries'
        similarDays.size() == 10

        and: 'they are the most recent days'
        similarDays.get(0).date == TimeUtil.dateFromDashString('2014-01-03')
        similarDays.get(9).date == TimeUtil.dateFromDashString('2014-07-29')
    }
}
