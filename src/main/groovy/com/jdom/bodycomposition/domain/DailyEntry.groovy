package com.jdom.bodycomposition.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*

/**
 * Created by djohnson on 11/14/14.
 */
@Entity
@Table(name = 'daily_entry')
@EqualsAndHashCode
@ToString
class DailyEntry extends BaseEntry {

}
