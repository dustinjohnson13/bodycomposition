package com.jdom.bodycomposition.web;

import com.jdom.bodycomposition.domain.BaseEntry;
import com.jdom.bodycomposition.domain.DailyEntry;
import com.jdom.bodycomposition.domain.TrendMetrics;
import com.jdom.bodycomposition.service.BodyCompositionService;
import com.jdom.util.MathUtil;
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

      IModel<List<? extends BaseEntry>> weeksWorthOfEntries =  new LoadableDetachableModel<List<? extends BaseEntry>>() {
         protected List<DailyEntry> load() {
            return bodyCompositionService.getLastSevenEntries();
         }
      };

      final EntriesPanel previousEntries = new EntriesPanel("previousEntriesDiv", "Previous Entries", weeksWorthOfEntries);
      previousEntries.setOutputMarkupId(true);
      add(previousEntries);

      final IModel<List<TrendMetrics>> metricsToDisplay =  new LoadableDetachableModel<List<TrendMetrics>>() {
         protected List<TrendMetrics> load() {
            return  bodyCompositionService.getTrendMetricsToDisplay();
         }
      };

      final WebMarkupContainer metricsDisplay = new WebMarkupContainer("metricsDiv");
      metricsDisplay.setOutputMarkupId(true);
      add(metricsDisplay);

      final ListView<TrendMetrics> metrics = new ListView<TrendMetrics>("metrics", metricsToDisplay) {

         @Override
         protected void populateItem(ListItem<TrendMetrics> item) {
            TrendMetrics metrics = item.getModelObject();
            item.add(new Label("period", metrics.getPeriodInDays() + " Days"));
            item.add(new Label("weightDifference", MathUtil.formatPositiveOrNegative(metrics.getWeightDifference())));
            item.add(new Label("bodyFatDifference", MathUtil.formatPositiveOrNegative(metrics.getBodyFatDifference())));
            item.add(new Label("waterPercentageDifference", MathUtil.formatPositiveOrNegative(metrics.getWaterPercentageDifference())));
         }
      };
      metricsDisplay.add(metrics);

      final IModel<List<? extends BaseEntry>> similarDaysEntries =  new LoadableDetachableModel<List<? extends BaseEntry>>() {
         protected List<? extends BaseEntry> load() {
            return bodyCompositionService.getSimilarDays();
         }
      };

      final EntriesPanel similarDays = new EntriesPanel("similarDays", "Similar Days", similarDaysEntries);
      similarDays.setOutputMarkupId(true);
      add(similarDays);

      final LoadableDetachableModel<DailyEntry> lastEntryModel = new LoadableDetachableModel<DailyEntry>() {
         @Override
         protected DailyEntry load() {
            DailyEntry newestEntry = bodyCompositionService.getNewestEntry();
            newestEntry.setDate(new Date(newestEntry.getDate().getTime() + TimeUtil.MILLIS_PER_DAY));
            newestEntry.setId(null);
            return newestEntry;
         }
      };

      final CompoundPropertyModel<DailyEntry> model = new CompoundPropertyModel<>(lastEntryModel);
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

            lastEntryModel.detach();
            metricsToDisplay.detach();
            similarDaysEntries.detach();

            target.add(previousEntries);
            target.add(metricsDisplay);
            target.add(similarDays);
            target.add(aForm);
         }
      });

   }
}
