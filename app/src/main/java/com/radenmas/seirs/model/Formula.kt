/*
 * Created by RadenMas on 19/8/2022.
 * Copyright (c) 2022.
 */

package com.radenmas.seirs.model

/**
 * Created by RadenMas on 19/08/2022.
 */

class Formula {
    lateinit var id: String
    lateinit var name: String
//    lateinit var image: String
    var ph: Float? = null
    var ppm: Int? = null

    constructor() {}

    constructor(
        id: String,
        name: String,
//        image: String,
        ph: Float,
        ppm: Int,
    ) {
        this.id = id
        this.name = name
//        this.image = image
        this.ph = ph
        this.ppm = ppm
    }
}