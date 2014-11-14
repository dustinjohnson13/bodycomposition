package com.jdom.bodycomposition.web
import org.apache.wicket.util.tester.WicketTester
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(locations = ["classpath:/applicationContext.xml"])
class HomePageSpec extends Specification {

    @Autowired
    WicketApplication application

    WicketTester tester;

    def setup() {
        tester = new WicketTester(application)
    }

    def 'should render the homepage'() {

        when: 'the home page is started'
        tester.startPage(HomePage.class)

        then: 'the home page was rendered'
        tester.assertRenderedPage(HomePage.class)

        and: 'the welcome message is displayed'
        tester.assertLabel("message", "If you see this message wicket is properly configured and running")
    }
}
