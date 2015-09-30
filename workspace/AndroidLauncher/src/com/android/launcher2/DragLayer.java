/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.launcher2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * A ViewGroup that coordinated dragging across its dscendants
 */
public class DragLayer extends FrameLayout {
    DragController mDragController;
    
    // AAD 24/2/2011 - Added.
    private AllAppsTransition mAllAppsTransition;
    private boolean mTouchEventInterceptedByAllAppsTransition;
    //

    /**
     * Used to create a new DragLayer from XML.
     *
     * @param context The application's context.
     * @param attrs The attribtues set containing the Workspace's customization values.
     */
    public DragLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDragController(DragController controller) {
        mDragController = controller;
    }

    // AAD 24/2/2011 - Added.
    public void setAllAppsTransition(AllAppsTransition allAppsTransition) {
    	mAllAppsTransition = allAppsTransition;
    }
    //
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mDragController.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // AAD 24/2/2011 - Added.
    	if (mAllAppsTransition.onInterceptTouchEvent(ev)) {
    		mTouchEventInterceptedByAllAppsTransition = true;
    		return true;
    	}
    	mTouchEventInterceptedByAllAppsTransition = false;
    	//
   		return mDragController.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // AAD 24/2/2011 - Added.
    	if (mTouchEventInterceptedByAllAppsTransition) {
    		if (mAllAppsTransition.onTouchEvent(ev)) {
    			return true;
    		}
    		mTouchEventInterceptedByAllAppsTransition = false;
    		return false;
    	}
    	//
        return mDragController.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchUnhandledMove(View focused, int direction) {
        return mDragController.dispatchUnhandledMove(focused, direction);
    }
}
