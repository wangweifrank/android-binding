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

import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public class DirectoryEntry {
	public final StringObservable Name = new StringObservable();
	public final IntegerObservable ResId = new IntegerObservable();
	
    public DirectoryEntry(String name, int resID) {
    	Name.set(name);
    	ResId.set(resID);
    }
}
