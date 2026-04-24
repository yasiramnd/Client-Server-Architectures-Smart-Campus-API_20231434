package com.coursework.api.model;

import java.util.Map;

public class DiscoveryResponse {

    private String apiName;
    private String version;
    private String contact;
    private String baseUrl;
    private Map<String, String> resources;

    public DiscoveryResponse() {
    }

    public DiscoveryResponse(String apiName, String version, String contact, String baseUrl, Map<String, String> resources) {
        this.apiName = apiName;
        this.version = version;
        this.contact = contact;
        this.baseUrl = baseUrl;
        this.resources = resources;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Map<String, String> getResources() {
        return resources;
    }

    public void setResources(Map<String, String> resources) {
        this.resources = resources;
    }
}
