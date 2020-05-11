package com.reschikov.gxbdevelopment.testtask.wolfram.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "img", strict = false)
data class Img(@field:Attribute(name = "src") @param:Attribute(name = "src") var src : String,
               @field:Attribute(name = "alt") @param:Attribute(name = "alt") var alt : String,
               @field:Attribute(name = "width") @param:Attribute(name = "width")var width : Int,
               @field:Attribute(name = "height") @param:Attribute(name = "height")var height : Int)