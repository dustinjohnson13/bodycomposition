package com.jdom.bodycomposition.service;

import com.jdom.bodycomposition.domain.BaseEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by djohnson on 11/15/14.
 */
@NoRepositoryBean
public interface BaseEntryDao<T extends BaseEntry> extends PagingAndSortingRepository<T, Long> {

   T findByDate(Date date);

   List<T> findByWaterPercentageOrderByDateDesc(double waterPercentage, Pageable pageable);
}
