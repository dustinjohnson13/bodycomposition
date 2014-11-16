package com.jdom.bodycomposition.service;

import com.jdom.bodycomposition.domain.BaseEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;
import java.util.List;

/**
 * Created by djohnson on 11/15/14.
 */
@NoRepositoryBean
public interface BaseEntryDao<T extends BaseEntry> extends CrudRepository<T, Long> {

   List<T> findByDateBetween(Date start, Date end);

   T findByDate(Date date);

   List<T> findByWaterPercentage(double waterPercentage);
}
