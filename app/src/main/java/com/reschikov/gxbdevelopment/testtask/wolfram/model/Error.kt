package com.reschikov.gxbdevelopment.testtask.wolfram.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element

@Element(name = "error")
data class Error(@field:Attribute(name = "code") @param:Attribute(name = "code") var code : Int,
                 @field:Attribute(name = "msg") @param:Attribute(name = "msg") var msg : String)