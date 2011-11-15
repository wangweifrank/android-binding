package com.gueei.demo.abgallery;


public class DirectoryCategory {
    private String name;
    public final DirectoryEntry[] Entries;

    public DirectoryCategory(String name, DirectoryEntry[] entries) {
        this.name = name;
        Entries = entries;
    }

    public String getName() {
        return name;
    }

    public int getEntryCount() {
    	return Entries.length;
    }

    public DirectoryEntry getEntry(int i) {
        return Entries[i];
    }
}
