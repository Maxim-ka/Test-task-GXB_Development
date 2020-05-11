package com.reschikov.gxbdevelopment.testtask.wolfram.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "pod", strict = false)
data class Pod(@field:Attribute(name = "title") @param:Attribute(name = "title") var title : String,
               @field:Attribute(name = "error") @param:Attribute(name = "error") var hasError : Boolean,
               @field:Element(name = "error", required = false)
               @param:Element(name = "error", required = false) var error : Error?,
               @field:ElementList(name = "subpod", required = false, inline = true)
               @param:ElementList(name = "subpod", required = false, inline = true)var subpods : List<Subpod>?)