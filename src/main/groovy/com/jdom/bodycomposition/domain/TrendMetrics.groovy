package com.jdom.bodycomposition.domain

import com.jdom.util.TimeUtil
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
/**
 * Created by djohnson on 11/14/14.
 */
@EqualsAndHashCode
@ToString
class TrendMetrics implements Serializable {
    Date start
    Date end
    double weightDifference
    double bodyFatDifference
    double waterPercentageDifference

    int getPeriodInDays() {
        return (end.getTime() - start.getTime()) / TimeUtil.MILLIS_PER_DAY
    }
}