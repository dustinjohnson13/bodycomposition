package com.jdom.bodycomposition.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by djohnson on 11/14/14.
 */
@Configuration
class BodyCompositionContext {

    private static final Logger LOG = LoggerFactory.getLogger(BodyCompositionContext.class)

    public BodyCompositionContext() {
        LOG.info('Context initialized.')
    }

    @Bean
    BodyCompositionService getBodyCompositionService() {
        return new MockBodyCompositionService()
    }
}
