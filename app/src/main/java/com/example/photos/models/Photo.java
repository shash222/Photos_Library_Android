package com.example.photos.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Photo object which is serialized into files that can be retrieved later
 * @author Mohammed Alnadi
 * @author Salman Hashmi
 */
public class Photo implements Serializable {
    private String location;
    private Map<String, Set<String>> tags;
    private Date dateTaken;

    /**
     * Constructor
     * @param location photo filepath
     * @param dateTaken photo date
     */
    public Photo(String location, Date dateTaken) {
        this.location = location;
        this.dateTaken = dateTaken;
        this.tags = new HashMap<>();
        this.tags.put("person", new HashSet<String>());
        this.tags.put("location", new HashSet<String>());

    }

    /**
     * Retrieves location
     * @return photo filepath
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Returns caption
     * @return photo caption
     */
    public Map<String, Set<String>> getTags() {
        return this.tags;
    }

    /**
     * Returns date taken
     * @return data of photo
     */
    public Date getDateTaken() {
        return this.dateTaken;
    }

    /**
     * Deletes tag
     * @param key key of tag to delete
     */
    public void deleteTag(String key) {
        if(tags.containsKey(key)) {
            tags.remove(key);
        } else {

        }
    }

    /**
     * Deletes tag value
     * @param key key associated with value
     * @param value value to delete
     */
    public void deleteTagValue(String key, String value) {
        if(tags.containsKey(key) && tags.get(key).contains(value)) {
            tags.get(key).remove(value);
        } else {

        }
    }

    /**
     * Adds new tag key
     * @param key new tag key
     */
    public void addTag(String key) {
        if(tags.containsKey(key)) {
        } else {
            tags.put(key, new HashSet<String>());
        }
    }

    /**
     * adds new tag
     * @param key key to add value to
     * @param value value to add
     */
    public void addTag(String key, String value) {
        Set<String> values = tags.getOrDefault(key, new HashSet<String>());
        values.add(value);
        tags.put(key, values);
    }

}
