package com.practice.findmap.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.practice.findmap.R;

public class AddDialog extends AppCompatDialogFragment {

    private TextInputEditText comment;
    private static final String EMPTY_STRING = "";
    private AddDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_dialog, null);

        builder.setView(view).setTitle(R.string.add_new_find)
        .setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton(getString(R.string.next_button), null);


        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;
                //Do stuff, possibly set wantToCloseDialog to true then...
                if(wantToCloseDialog) {
                    dialog.dismiss(); } else {
                    String find_comment = comment.getText().toString();
                    if (!find_comment.equals("")) {
                        CategoryDialog addDialog = new CategoryDialog();
                        addDialog.show(getFragmentManager(), "add dialog");
                        listener.applyComment(find_comment);
                        dismiss();
                    } else {
                        TextInputLayout error = (TextInputLayout) view.findViewById(R.id.add_text);
                        error.setErrorTextAppearance(R.style.error);
                        error.setErrorEnabled(true);
                        error.setError(getString(R.string.error));
                    }
                }
            }
        });


    comment = (TextInputEditText) view.findViewById(R.id.add_comment);
    return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AddDialogListener");
        }
    }

    public interface AddDialogListener {
        void applyComment(String find_comment);
    }

}


