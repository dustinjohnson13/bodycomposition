package com.jdom.bodycomposition.web;

import com.jdom.bodycomposition.domain.BaseEntry;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by djohnson on 11/16/14.
 */
public class EntriesPanel extends Panel {

   public EntriesPanel(String id, String title, IModel<List<? extends BaseEntry>> model) {
      super(id, model);

      add(new Label("title", title));

      final ListView<BaseEntry> entries = new ListView<BaseEntry>("entries", model) {

         @Override
         protected void populateItem(ListItem<BaseEntry> item) {
            BaseEntry BaseEntry = item.getModelObject();
            item.add(new Label("date", new SimpleDateFormat("MM/dd/yyyy").format(BaseEntry.getDate())));
            item.add(new Label("weight", Double.toString(BaseEntry.getWeight())));
            item.add(new Label("bodyFat", Double.toString(BaseEntry.getBodyFat())));
            item.add(new Label("waterPercentage", Double.toString(BaseEntry.getWaterPercentage())));
            item.add(new Label("normalizedBodyFat", Double.toString(BaseEntry.getNormalizedBodyFat())));
         }
      };
      add(entries);
   }
}
