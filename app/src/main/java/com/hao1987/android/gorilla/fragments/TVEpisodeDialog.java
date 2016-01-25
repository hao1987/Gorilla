package com.hao1987.android.gorilla.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.hao1987.android.gorilla.R;

public class TVEpisodeDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setMessage(R.string.dialog_fire_missiles)
//                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // FIRE ZE MISSILES!
//                    }
//                })
//                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

//new AlertDialog.Builder(context)
//        .setTitle(R.string.invalid_deck)
//        .setMessage(R.string.add_deck_null_name_error)
//        .setCancelable(false)
//        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//public void onClick(DialogInterface dialog, int which) {
//        dialog.cancel();
//        }
//        })
//        .show();
