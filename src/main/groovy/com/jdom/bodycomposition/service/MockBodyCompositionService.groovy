package com.jdom.bodycomposition.service

import com.jdom.bodycomposition.domain.BodyComposition

/**
 * Created by djohnson on 11/14/14.
 */
class MockBodyCompositionService implements BodyCompositionService {

    @Override
    List<BodyComposition> findInRange(final Date start, final Date end) {

        def results = []

        Calendar cal = Calendar.getInstance()
        cal.setTime(start)

        Calendar endCal = Calendar.getInstance()
        endCal.setTime(end)

        double weight = 136.8
        double bodyFat = 13.5
        double waterPercentage = 62.3

        Random random = new Random()
        while (cal.get(Calendar.DAY_OF_YEAR) <= endCal.get(Calendar.DAY_OF_YEAR)) {
            BodyComposition bodyComposition = new BodyComposition(date: cal.getTime(),
                weight: weight, bodyFat: bodyFat, waterPercentage: waterPercentage)

            results += bodyComposition
            cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 1)
        }

        return results
    }
}
