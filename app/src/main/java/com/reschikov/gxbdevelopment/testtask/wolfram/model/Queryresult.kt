package com.reschikov.gxbdevelopment.testtask.wolfram.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "queryresult", strict = false)
data class Queryresult (@field:Attribute(name = "success")
                        @param:Attribute(name = "success") var success : Boolean,
                        @field:Element(required = false, name = "error")
                        @param:Element(required = false, name = "error")var error : Error?,
                        @field:Attribute(name = "error")
                        @param:Attribute(name = "error") var hasError : Boolean,
                        @field:ElementList(required = false, name = "pod", inline = true)
                        @param:ElementList(required = false, name = "pod", inline = true) var pods : List<Pod>?)