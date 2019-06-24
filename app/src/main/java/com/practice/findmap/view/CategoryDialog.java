package com.practice.findmap.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.practice.findmap.R;

public class CategoryDialog extends AppCompatDialogFragment{

    private String[] listItems;
    private String category = "";
    private CategoryDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        listItems = new String[]{"Еда и напитки", "Магазины", "Достопримечательности", "Природа",
                "Развлечения", "Другое"};

        builder.setTitle(R.string.filter_dialog)
                .setSingleChoiceItems(listItems, 5, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        category = listItems[i];
                    }
                }).setNegativeButton(getString(R.string.close), null).setPositiveButton(getString(R.string.add_button), null);

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
                if (category.equals("")) category = listItems[5];
                System.out.println(category);
                listener.applyCategory(category);
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                category = "";
                dismiss();
                listener.applyCategory(category);
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CategoryDialog.CategoryDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CategoryDialogListener");
        }
    }

    public interface CategoryDialogListener {
        void applyCategory(String category);
    }
}
