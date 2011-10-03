/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gueei.demo.abgallery;

import gueei.binding.collections.ArrayListObservable;

public class DirectoryCategory {
    private String name;
    public final ArrayListObservable<DirectoryEntry> Entries = 
    		new ArrayListObservable<DirectoryEntry>(DirectoryEntry.class);

    public DirectoryCategory(String name, DirectoryEntry[] entries) {
        this.name = name;
        Entries.setArray(entries);
    }

    public String getName() {
        return name;
    }

    public int getEntryCount() {
    	return Entries.size();
    }

    public DirectoryEntry getEntry(int i) {
        return Entries.getItem(i);
    }
}
