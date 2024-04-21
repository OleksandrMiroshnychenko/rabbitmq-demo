package com.miroshnychenko.consumer.dto

import java.io.Serializable

class Order() : Serializable {
    var id: Int? = null
    var description: String? = null
    var type: OrderType? = null

    constructor(id: Int?, description: String?, type: OrderType?): this() {
        this.id = id
        this.description = description
        this.type = type
    }

    override fun toString(): String {
        return "Order[id=$id, type=$type]"
    }
}