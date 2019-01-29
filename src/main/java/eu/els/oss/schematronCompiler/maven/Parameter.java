package eu.els.oss.schematronCompiler.maven;

import javax.xml.namespace.QName;

public class Parameter {
	
	private String localName;
	private String Value;
	private String namespaceURI;
	
	
	
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public String getNamespaceURI() {
		return namespaceURI;
	}
	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}
	
	public QName getQName() {
		return new QName(getNamespaceURI(), getLocalName());
	}
	
	

}
