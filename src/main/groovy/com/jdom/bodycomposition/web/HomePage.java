package com.jdom.bodycomposition.web;

import com.jdom.bodycomposition.domain.DailyEntry;
import com.jdom.bodycomposition.service.BodyCompositionService;
import com.jdom.util.TimeUtil;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomePage extends WebPage {

   private static final long serialVersionUID = 1L;

   @SpringBean
   private BodyCompositionService bodyCompositionService;

   /**
    * Constructor that is invoked when page is invoked without a session.
    *
    * @param parameters Page parameters
    */
   public HomePage(final PageParameters parameters) {

      IModel<List<DailyEntry>> weeksWorthOfEntries =  new LoadableDetachableModel<List<DailyEntry>>() {
         protected List<DailyEntry> load() {
            return bodyCompositionService.getWeeksWorthOfEntries();
         }
      };

      final WebMarkupContainer previousEntries = new WebMarkupContainer("previousEntriesDiv");
      previousEntries.setOutputMarkupId(true);
      add(previousEntries);

      final ListView<DailyEntry> entries = new ListView<DailyEntry>("entries", weeksWorthOfEntries) {

         @Override
         protected void populateItem(ListItem<DailyEntry> item) {
            DailyEntry dailyEntry = item.getModelObject();
            item.add(new Label("date", new SimpleDateFormat("MM/dd/yyyy").format(dailyEntry.getDate())));
            item.add(new Label("weight", Double.toString(dailyEntry.getWeight())));
            item.add(new Label("bodyFat", Double.toString(dailyEntry.getBodyFat())));
            item.add(new Label("waterPercentage", Double.toString(dailyEntry.getWaterPercentage())));
         }
      };
      previousEntries.add(entries);

      List<DailyEntry> listOfEntries = weeksWorthOfEntries.getObject();
      final DailyEntry lastEntry = listOfEntries.get(listOfEntries.size() - 1);

      DailyEntry dailyEntry = new DailyEntry();
      dailyEntry.setDate(new Date(lastEntry.getDate().getTime() + TimeUtil.MILLIS_PER_DAY));
      dailyEntry.setWeight(lastEntry.getWeight());
      dailyEntry.setBodyFat(lastEntry.getBodyFat());
      dailyEntry.setWaterPercentage(lastEntry.getWaterPercentage());


      final LoadableDetachableModel<DailyEntry> lastEntryModel = new LoadableDetachableModel<DailyEntry>() {
         @Override
         protected DailyEntry load() {
            List<DailyEntry> weeksWorthOfEntries1 = bodyCompositionService.getWeeksWorthOfEntries();
            DailyEntry newestEntry = weeksWorthOfEntries1.get(weeksWorthOfEntries1.size() - 1);
            newestEntry.setDate(new Date(newestEntry.getDate().getTime() + TimeUtil.MILLIS_PER_DAY));
            newestEntry.setId(null);
            return newestEntry;
         }
      };

      final CompoundPropertyModel<DailyEntry> model = new CompoundPropertyModel<DailyEntry>(lastEntryModel);
      final Form<DailyEntry> form = new Form<>("newEntryForm", model);
      form.setOutputMarkupId(true);
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
            DailyEntry modelObject = model.getObject();
            bodyCompositionService.saveEntry(modelObject);

            modelObject.setDate(new Date(modelObject.getDate().getTime() + TimeUtil.MILLIS_PER_DAY));
            modelObject.setId(null);

            target.add(previousEntries);
            target.add(aForm);
         }
      });
   }
}
