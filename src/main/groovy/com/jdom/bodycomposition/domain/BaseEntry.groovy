package com.jdom.bodycomposition.domain

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Created by djohnson on 11/15/14.
 */
@MappedSuperclass
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
