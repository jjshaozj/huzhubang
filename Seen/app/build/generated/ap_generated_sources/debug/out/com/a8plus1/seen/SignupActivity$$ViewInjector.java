// Generated code from Butter Knife. Do not modify!
package com.a8plus1.seen;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SignupActivity$$ViewInjector {
  public static void inject(Finder finder, final com.a8plus1.seen.SignupActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296380, "field 'fab'");
    target.fab = (android.support.design.widget.FloatingActionButton) view;
    view = finder.findRequiredView(source, 2131296351, "field 'cvAdd'");
    target.cvAdd = (android.support.v7.widget.CardView) view;
    view = finder.findRequiredView(source, 2131296319, "field 'btGo' and method 'onClick'");
    target.btGo = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296374, "field 'etUsername'");
    target.etUsername = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296372, "field 'etPassword'");
    target.etPassword = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296373, "field 'etRepeatPassword'");
    target.etRepeatPassword = (android.widget.EditText) view;
  }

  public static void reset(com.a8plus1.seen.SignupActivity target) {
    target.fab = null;
    target.cvAdd = null;
    target.btGo = null;
    target.etUsername = null;
    target.etPassword = null;
    target.etRepeatPassword = null;
  }
}
