package com.jdom.bodycomposition.service

import com.jdom.bodycomposition.domain.DailyEntry
import com.jdom.bodycomposition.web.WicketApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Created by djohnson on 11/14/14.
 */
@ComponentScan(basePackageClasses = [DailyEntry.class, DbContext.class, WicketApplication.class])
@Configuration
class BodyCompositionContext {

    private static final Logger LOG = LoggerFactory.getLogger(BodyCompositionContext.class)

    public BodyCompositionContext() {
        LOG.info('Context initialized.')
    }
}
