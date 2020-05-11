package com.reschikov.gxbdevelopment.testtask.wolfram.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "subpod", strict = false)
data class Subpod (@field:Element(name = "img", required = false)
                   @param:Element(name = "img", required = false) var img : Img?)