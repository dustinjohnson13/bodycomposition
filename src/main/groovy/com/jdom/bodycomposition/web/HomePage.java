package com.jdom.bodycomposition.web;

import com.jdom.bodycomposition.domain.BodyComposition;
import com.jdom.bodycomposition.service.BodyCompositionService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomePage extends WebPage {

   private static final long serialVersionUID = 1L;

   private static final int MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

   @SpringBean
   private BodyCompositionService bodyCompositionService;

   /**
    * Constructor that is invoked when page is invoked without a session.
    *
    * @param parameters Page parameters
    */
   public HomePage(final PageParameters parameters) {

      List<BodyComposition> weeksWorthOfEntries =
            bodyCompositionService.findInRange(new Date(),
                  new Date(System.currentTimeMillis() + (MILLIS_PER_DAY * 7)));

      ListView<BodyComposition> entries = new ListView<BodyComposition>("entries", weeksWorthOfEntries) {

         @Override
         protected void populateItem(ListItem<BodyComposition> item) {
            BodyComposition bodyComposition = item.getModelObject();
            item.add(new Label("date", new SimpleDateFormat("MM/dd/yyyy").format(bodyComposition.getDate())));
            item.add(new Label("weight", Double.toString(bodyComposition.getWeight())));
            item.add(new Label("bodyFat", Double.toString(bodyComposition.getBodyFat())));
            item.add(new Label("waterPercentage", Double.toString(bodyComposition.getWaterPercentage())));
         }
      };
      add(entries);

      BodyComposition lastEntry = weeksWorthOfEntries.get(weeksWorthOfEntries.size() - 1);

      BodyComposition bodyComposition = new BodyComposition();
      bodyComposition.setDate(new Date(lastEntry.getDate().getTime() + MILLIS_PER_DAY));
      bodyComposition.setWeight(lastEntry.getWeight());
      bodyComposition.setBodyFat(lastEntry.getBodyFat());
      bodyComposition.setWaterPercentage(lastEntry.getWaterPercentage());

      CompoundPropertyModel<BodyComposition> model = new CompoundPropertyModel<BodyComposition>(bodyComposition);
      Form<BodyComposition> form = new Form<>("newEntryForm", model);
      add(form);

      DateTextField date = new DateTextField("date");
      date.setRequired(true);
      form.add(date);

      form.add(new RequiredTextField("weight", Double.class).setRequired(true));
      form.add(new RequiredTextField("bodyFat", Double.class).setRequired(true));
      form.add(new RequiredTextField("waterPercentage", Double.class).setRequired(true));
      form.add(new AjaxSubmitLink("add") {
         @Override
         protected void onSubmit(AjaxRequestTarget target, Form<?> aForm) {
            super.onSubmit(target, aForm);
         }
      });
   }
}
