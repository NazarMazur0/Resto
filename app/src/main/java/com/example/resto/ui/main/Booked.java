package com.example.resto.ui.main;

import android.content.Context;
import android.os.Bundle;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.resto.DatabaseHelper;
import com.example.resto.MainActivity;
import com.example.resto.R;
import com.example.resto.Table;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Locale;

public class Booked extends Fragment {
    View popupView;
    ConstraintLayout mainConstraint;
    Animation in;
    View fragment;
    ArrayList<Table> tables;
    public Booked(){
        super(R.layout.booked_fragment_layout);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.booked_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View fragment, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);
        LayoutInflater inflater=getLayoutInflater();
        popupView=inflater.inflate(R.layout.popup_window_booked,null);
        in=AnimationUtils.loadAnimation(getContext(),R.anim.zoomin);
        this.fragment=fragment;
        mainConstraint=fragment.findViewById(R.id.mainConstraint1);
        loadTables(fragment,getContext());
    }

    public View getFragment() {
        return fragment;
    }

    public void loadTables(View fragment, Context context){
        DatabaseHelper dbHelper=new DatabaseHelper(context);
        tables=dbHelper.selectBooked();
        LinearLayout[] l = {fragment.findViewById(R.id.btn_layout_h1), fragment.findViewById(R.id.btn_layout_h2), fragment.findViewById(R.id.btn_layout_h3), fragment.findViewById(R.id.btn_layout_h4)};
        for (int i = 0; i < tables.size(); i++) {
            AppCompatButton v;
            Table t = tables.get(i);
            v = (AppCompatButton) getLayoutInflater().inflate(R.layout.button, null);
            v.setBackground( AppCompatResources.getDrawable(getActivity().getApplicationContext(),R.drawable.booked_button_background) );
            v.setText(String.format(Locale.ROOT, "Стіл %d",t.getTableNumber()));
            v.setTag("booked" + t.getId());
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
            v.setOnClickListener((this::show));
        }
    }
     void show(View btn){
         PopupWindow popupWindow = new PopupWindow(
                 popupView,
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 LinearLayout.LayoutParams.WRAP_CONTENT,
                 true
         );
         popupWindow.showAtLocation(mainConstraint, Gravity.CENTER, 0, 0);
         View popupView=popupWindow.getContentView();
         Button btn_popup=popupView.findViewById(R.id.popup_btn_show);
         TextView name = popupView.findViewById(R.id.nameText);
         TextView telephone = popupView.findViewById(R.id.telephoneText);
         TextView time = popupView.findViewById(R.id.timeText);
         int id =Integer.parseInt(((String)btn.getTag()).substring(6));
         int number=Integer.parseInt(((AppCompatButton)btn).getText().toString().substring(5));
         DatabaseHelper databaseHelper=new DatabaseHelper(getContext());
         Table t= databaseHelper.getOne(id);
         name.setText(t.getClientName());
         telephone.setText(""+t.getClientPhone());
         time.setText(t.getBookingTime());


         btn_popup.setOnClickListener((view)->{
             AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
             dialogBuilder.setCancelable(false)
                     .setPositiveButton("Так",(dialog,which)->{
                         databaseHelper.updateOne(new Table(id,false,"",0,"",number));
                         popupWindow.dismiss();
                         dialog.cancel();
                         ((MainActivity)getActivity()).loadFragments();
                     })
                     .setNegativeButton("Ні",(dialog,which)->dialog.cancel())
                     .setMessage("Ви точно хочете відмінити бронювання?");
             AlertDialog dialog=dialogBuilder.create();
             dialog.setTitle("Відміна");
             dialog.show();
         });
     }
}

