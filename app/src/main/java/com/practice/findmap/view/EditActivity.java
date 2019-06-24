package com.practice.findmap.view;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.practice.findmap.R;
import com.practice.findmap.data.AddNewFindDB;
import com.practice.findmap.data.DBHelper;
import com.practice.findmap.domain.model.FindData;
import com.practice.findmap.domain.model.MarkerCoordinates;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    public static final String LAT = "LAT";
    public static final String LNG = "LNG";
    private DBHelper dbHelper;
    private AddNewFindDB addNewFindDB;
    private RadioGroup category;
    private String catFind;
    private TextInputEditText comment;
    private int currentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitle("Редактирование находки");

        dbHelper = new DBHelper(this);
        addNewFindDB = new AddNewFindDB(dbHelper);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        double latitude = Double.parseDouble(Objects.requireNonNull(bundle.getString(LAT)));
        double longitude = Double.parseDouble(Objects.requireNonNull(bundle.getString(LNG)));

        MarkerCoordinates coordinates = new MarkerCoordinates(latitude, longitude);
        FindData find = addNewFindDB.findByCoordinates(coordinates);

        category = (RadioGroup) findViewById(R.id.radioGroupCategory);
        comment = (TextInputEditText) findViewById(R.id.add_comment);

        setCategory(find.getCategory());
        catFind = find.getCategory();
        comment.setText(find.getComment());

    }

    public void onClickUpdate(View view) {
        String find_comment = comment.getText().toString();
        if (find_comment.equals("")) {
            TextInputLayout error = (TextInputLayout) view.findViewById(R.id.add_text);
            error.setErrorTextAppearance(R.style.error);
            error.setErrorEnabled(true);
            error.setError(getString(R.string.error));
        } else {
            checkRadioGroup();

            addNewFindDB.updateFind(currentID, comment.getText().toString(), catFind);
            System.out.println(currentID);
            System.out.println(comment.getText().toString());
            System.out.println(catFind);
            Intent startMapIntent = new Intent(EditActivity.this, MapsActivity.class);
            startActivity(startMapIntent);
        }
    }


    public void setCategory(String cat) {
        switch (cat) {
            case "Еда и напитки":
                ((RadioButton) category.findViewById(R.id.radioButtonFood)).setChecked(true);
                break;
            case "Магазины":
                ((RadioButton) category.findViewById(R.id.radioButtonShop)).setChecked(true);
                break;
            case "Достопримечательности":
                ((RadioButton) category.findViewById(R.id.radioButtonSight)).setChecked(true);
                break;
            case "Природа":
                ((RadioButton) category.findViewById(R.id.radioButtonNature)).setChecked(true);
                break;
            case "Развлечения":
                ((RadioButton) category.findViewById(R.id.radioButtonEnterteinmant)).setChecked(true);
                break;
            case "Другое":
                ((RadioButton) category.findViewById(R.id.radioButtonOther)).setChecked(true);
                break;
        }
    }

    public void checkRadioGroup() {
        if (((RadioButton) category.findViewById(R.id.radioButtonFood)).isChecked())
        {catFind = "Еда и напитки";}
            else
        if (((RadioButton) category.findViewById(R.id.radioButtonShop)).isChecked())
        {catFind = "Магазины";}
            else
        if (((RadioButton) category.findViewById(R.id.radioButtonSight)).isChecked())
        {catFind = "Достопримечательности";}
            else
        if (((RadioButton) category.findViewById(R.id.radioButtonNature)).isChecked())
        {catFind = "Природа";}
            else
        if (((RadioButton) category.findViewById(R.id.radioButtonEnterteinmant)).isChecked())
        {catFind = "Развлечения";}
            else
        if (((RadioButton) category.findViewById(R.id.radioButtonOther)).isChecked())
        {catFind = "Другое";}
    }

}
