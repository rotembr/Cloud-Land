/*
 * Copyright 2016, 2017 IBM Corp.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.bluemix.appid.cloud.directory.android.sample.appid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Static utility methods
 */
class Utils {
    static boolean isItemInJsonArray(JSONArray jsonArray, String itemToFind) {
        return jsonArray != null && jsonArray.toString().contains("\"" + itemToFind + "\"");
    }

    static void removeValueFromJsonArray(JSONArray jArray, String value) throws JSONException {
        if (jArray == null) {
            return;
        }
        for (int i = 0; i < jArray.length(); i++) {
            if (jArray.getString(i).equals(value)) {
                jArray.remove(i);
                return;
            }
        }
    }

    static void showMessage(final String status, final String msg, Activity activity) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.setTitle(status);
                alertDialog.setMessage(msg);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }
}