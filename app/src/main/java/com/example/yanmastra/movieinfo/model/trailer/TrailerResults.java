package com.example.yanmastra.movieinfo.model.trailer;

/**
 * Created by Yan Mastra on 8/13/2017.
 */

public class TrailerResults {
    private String id;
    private String iso_639_1;
    private String Iso_3166_1;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return Iso_3166_1;
    }

    public void setIso_3166_1(String getIso_3166_1) {
        this.Iso_3166_1 = getIso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
