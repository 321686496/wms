package com.hongan.template.base.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class JacksonObject {
    private static ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getJSONMapper() {
        return mapper;
    }


    private static XmlMapper xmlMapper = new XmlMapper();

    static {
        xmlMapper.setAnnotationIntrospector(new XmlAnnotationIntrospector());
    }

    public static XmlMapper getXMLMapper() {
        return xmlMapper;
    }


    public static class XmlAnnotationIntrospector extends JacksonXmlAnnotationIntrospector {
        @Override
        public boolean hasIgnoreMarker(AnnotatedMember m) {
            return m.hasAnnotation(XmlIgnore.class) || super.hasIgnoreMarker(m);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface XmlIgnore {
    }
}
