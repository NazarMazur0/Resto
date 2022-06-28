package com.example.resto.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.resto.DatabaseHelper;
import com.example.resto.MainActivity;
import com.example.resto.R;
import com.example.resto.Table;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Booking extends Fragment {
    View popupView;
    View startingPopUp;
    ConstraintLayout mainConstraint;
    Animation in;
    Animation out;
    View fragment;
    public Booking(){
        super(R.layout.booking_fragment_layout);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        return inflater.inflate(R.layout.booking_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View fragment, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);
        LayoutInflater inflater=getLayoutInflater();
        popupView=inflater.inflate(R.layout.popup_window,null);
        startingPopUp=inflater.inflate(R.layout.starting_pop_up,null);
        in=AnimationUtils.loadAnimation(getContext(),R.anim.zoomin);
        out=AnimationUtils.loadAnimation(getContext(),R.anim.zoomout);
        this.fragment=fragment;
        mainConstraint = fragment.findViewById(R.id.mainConstrait);

        loadTables(fragment,getContext());
    }

    public View getFragment() {
        return fragment;
    }

    public void loadTables(View fragment, Context context){
        DatabaseHelper dbHelper=new DatabaseHelper(context);
        ArrayList<Table> tables=dbHelper.selectUnBooked();
        if(tables.size()==0) {
            initializeTables(fragment,dbHelper);
        }else {
            LinearLayout[] l = {fragment.findViewById(R.id.btn_layout_h1), fragment.findViewById(R.id.btn_layout_h2), fragment.findViewById(R.id.btn_layout_h3), fragment.findViewById(R.id.btn_layout_h4)};
            for (int i = 0; i < tables.size(); i++) {
                AppCompatButton v;
                Table t = tables.get(i);
                v = (AppCompatButton) getLayoutInflater().inflate(R.layout.button, null);
                v.setText(String.format(Locale.ROOT, "Стіл %d",t.getTableNumber()));
                v.setTag("unbooked" + t.getId());
                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70f, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams btm_margins = new LinearLayout.LayoutParams(px, px);
                btm_margins.setMargins(2, 5, 2, 5);
                v.setLayoutParams(btm_margins);
                switch ((i + 1) % 4) {
                    case 1: {
                        l[0].addView(v);
                        break;
                    }
                    case 2: {
                        l[1].addView(v);
                        break;
                    }
                    case 3: {
                        l[2].addView(v);
                        break;
                    }
                    case 0: {
                        l[3].addView(v);
                        break;
                    }
                }
                v.setOnClickListener((this::book));
            }
        }
    }
     void initializeTables(View fragment , DatabaseHelper dbHelper) {
         PopupWindow popupWindow = new PopupWindow(
                 startingPopUp,
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 true
         );
         popupWindow.showAtLocation(mainConstraint, Gravity.CENTER, 0, 0);
         View v = popupWindow.getContentView();
         v.startAnimation(in);
         EditText countEditText = v.findViewById(R.id.tablesCount);
         int count;
         Button ok = v.findViewById(R.id.ok);
         ok.setOnClickListener((view -> {
            dbFill(dbHelper,countEditText,popupWindow);
            loadTables(fragment,getContext());
         }));
    }
   void dbFill(DatabaseHelper dbHelper,EditText countEditText,PopupWindow popUp){
      int count= Integer.parseInt( countEditText.getText().toString());

       for (int i = 1; i < count+1; i++) {
           dbHelper.addOne( new Table(i, false, "", 0, "", i));
       }
       popUp.dismiss();
   }
   void book(View btn){
       PopupWindow popupWindow = new PopupWindow(
               popupView,
               LinearLayout.LayoutParams.WRAP_CONTENT,
               LinearLayout.LayoutParams.WRAP_CONTENT,
               true
       );
       popupWindow.showAtLocation(mainConstraint, Gravity.CENTER, 0, 0);
       View popupView=popupWindow.getContentView();
       Button btn_popup=popupView.findViewById(R.id.popup_btn_book);
       EditText nameView = popupView.findViewById(R.id.name);
       EditText telephoneView = popupView.findViewById(R.id.telephone);
       EditText timeView = popupView.findViewById(R.id.time);


       btn_popup.setOnClickListener((view)->{
           String name=nameView.getText().toString();
           if(name.length()<2){
               Toast.makeText(getContext(),"Ім'я занадто коротке",Toast.LENGTH_LONG).show();
               return;
           }

           String phone=telephoneView.getText().toString();
           if(phone.length()<9){
               Toast.makeText(getContext(),"Номер телефону занадто короткий ",Toast.LENGTH_LONG).show();
               return;
           }
           Long phoneL=Long.parseLong(telephoneView.getText().toString());
           String time=timeView.getText().toString();

           Toast.makeText(getContext(),"Неправильний формат часу" ,Toast.LENGTH_LONG).show();
           String timePattern="[0-2][0-4]:[0-5][0-9]";
           Pattern pat=Pattern.compile(timePattern);
           Matcher matcher=pat.matcher(time);
           if(!matcher.find()) {
               Toast.makeText(getContext(),"Неправильний формат часу",Toast.LENGTH_LONG).show();
               return;
           }
            int id =Integer.parseInt(((String)btn.getTag()).substring(8));
           int number=Integer.parseInt(((AppCompatButton)btn).getText().toString().substring(5));
           DatabaseHelper databaseHelper=new DatabaseHelper(getContext());
           databaseHelper.updateOne(new Table(id,true,name,phoneL,time,number));
           LinearLayout btn_parent=((LinearLayout) btn.getParent());
           btn_parent.removeView(btn);
           popupWindow.dismiss();
           ((MainActivity)getActivity()).loadFragments();
       });
   }
}
