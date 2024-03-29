/***
  Copyright (c) 2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Android Development_
    http://commonsware.com/Android
 */

package com.euphor.paperpad.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.euphor.paperpad.activities.fragments.SurveyPageFragment;
import com.euphor.paperpad.activities.fragments.SurveySubmitFragment;
import com.euphor.paperpad.activities.fragments.SurveyThanksFragment;

public class SurveyFragmentsAdapter extends FragmentPagerAdapter {
  Context ctxt=null;
private SurveyPageFragment sPageFragment;
private SurveySubmitFragment sSubmitFragment;
private SurveyThanksFragment thanksFragment;

  public SurveyFragmentsAdapter(Context ctxt, FragmentManager mgr, SurveyPageFragment sPageFragment, SurveySubmitFragment sSubmitFragment, SurveyThanksFragment thanksFragment) {
    super(mgr);
    this.ctxt=ctxt;
    this.sPageFragment= sPageFragment;
    this.sSubmitFragment = sSubmitFragment;
    this.thanksFragment = thanksFragment;
  }

  
  @Override
  public int getCount() {
    return(3);
  }

  @Override
  public Fragment getItem(int position) {
	  if (position == 0) {
		return sPageFragment;
	}else if(position == 1) {
		return sSubmitFragment;
	}else if (position == 2) {
		return thanksFragment;
	}else {
		return sPageFragment;
	}
  }

  @Override
  public String getPageTitle(int position) {
    return position+""/*(EditorFragment.getTitle(ctxt, position))*/;
  }
}