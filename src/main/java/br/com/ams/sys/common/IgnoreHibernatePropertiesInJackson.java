package br.com.ams.sys.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public abstract class IgnoreHibernatePropertiesInJackson {

}
