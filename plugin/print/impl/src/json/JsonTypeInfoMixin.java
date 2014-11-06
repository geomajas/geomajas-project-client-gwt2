package org.geomajas.gwt2.plugin.print.client.json;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS, include=As.PROPERTY, property="class")
public class JsonTypeInfoMixin {

}
