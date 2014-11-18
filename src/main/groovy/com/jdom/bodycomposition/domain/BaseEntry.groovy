package com.jdom.bodycomposition.domain

import groovy.transform.ToString

import javax.persistence.*
/**
 * Created by djohnson on 11/15/14.
 */
@MappedSuperclass
@ToString
class BaseEntry implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id

    @Column
    Date date

    @Column
    Double weight

    @Column(name = 'body_fat')
    Double bodyFat

    @Column(name = 'water_percentage')
    Double waterPercentage
}
