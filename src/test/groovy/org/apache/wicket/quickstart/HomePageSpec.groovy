package org.apache.wicket.quickstart

import org.apache.wicket.util.tester.WicketTester
import spock.lang.Specification

class HomePageSpec extends Specification {
    private WicketTester tester;

    def setup() {
        tester = new WicketTester(new WicketApplication());
    }

    def 'should render the homepage'() {

        when: 'the home page is started'
        tester.startPage(HomePage.class);

        then: 'the home page was rendered'
        tester.assertRenderedPage(HomePage.class);

        and: 'the welcome message is displayed'
        tester.assertLabel("message", "If you see this message wicket is properly configured and running");
    }
}
