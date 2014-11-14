package com.jdom.bodycomposition.web

import com.jdom.bodycomposition.domain.BodyComposition
import com.jdom.bodycomposition.service.BodyCompositionService
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean

class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

    @SpringBean BodyCompositionService bodyCompositionService

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    HomePage(final PageParameters parameters) {

        for (BodyComposition bodyComposition : bodyCompositionService.findInRange(new Date(), new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)))) {
            println bodyComposition.date
        }

        add(new Label("message", "If you see this message wicket is properly configured and running"));
    }
}
