package com.jdom.bodycomposition.service

import com.jdom.bodycomposition.domain.BodyComposition

/**
 * Created by djohnson on 11/14/14.
 */
interface BodyCompositionService {

    List<BodyComposition> findInRange(Date start, Date end)

}