/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Valter Kasper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package sk.kasper.movieapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Bus;

import sk.kasper.movieapp.MovieApplication;

/**
 * Base class for creating activities in application
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final int MOVIE_ACTIVITY = 711;
    public static final int BOOKMARKS_ACTIVITY = 712;
    public static final int HAS_NOT_DRAWER_ACTIVITY = 710;
    protected Bus bus;
    private DrawerHelper drawerHelper;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bus = ((MovieApplication) getApplication()).getBus();
	}

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (getDrawerId() != HAS_NOT_DRAWER_ACTIVITY)
            drawerHelper = new DrawerHelper(this);
    }

    public abstract int getDrawerId();
}
