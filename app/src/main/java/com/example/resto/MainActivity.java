package com.example.resto;

import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.resto.ui.main.Booking;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.example.resto.ui.main.SectionsPagerAdapter;
import com.example.resto.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ViewPager viewPager;
    TabLayout tabs;
    SectionsPagerAdapter sectionsPagerAdapter;
    Button clearAll;
    Button add;
    Animation in;
    View addPopUp;
    View cordinatorLayput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadFragments();
        addPopUp=getLayoutInflater().inflate(R.layout.popup_window_add,null);
        clearAll=findViewById(R.id.clearAll);
        add=findViewById(R.id.add);
        in=AnimationUtils.loadAnimation(this,R.anim.zoomin);
        cordinatorLayput=findViewById(R.id.cordinatorLayput);
        clearAll.setOnClickListener((v)->{
            DatabaseHelper h = new DatabaseHelper(this);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setCancelable(false)
                    .setPositiveButton("Так",(dialog,which)->{
                        h.clearAll();
                        dialog.cancel();
                        loadFragments();
                    })
                    .setNegativeButton("Ні",(dialog,which)->dialog.cancel())
                    .setMessage("Ви точно хочете очистит всі данні?\n Ця операція невідворотня");
            AlertDialog dialog=dialogBuilder.create();
            dialog.setTitle("Очищення");
            dialog.show();
            h.close();
            loadFragments();
        });
        add.setOnClickListener((view)->{
            PopupWindow popupWindow = new PopupWindow(
                    addPopUp,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
            );
            View parent = findViewById(R.id.cordinatorLayput);
            popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            View v = popupWindow.getContentView();
            v.startAnimation(in);
            EditText countEditText = v.findViewById(R.id.count);
            Button popUpAddBtn=v.findViewById(R.id.popup_btn_add);
            popUpAddBtn.setOnClickListener((vButton)->{
                DatabaseHelper h = new DatabaseHelper(this);
                int count=Integer.parseInt(countEditText.getText().toString());
                int currentNumber;
                try {
                    currentNumber=h.getLast().getTableNumber();
                }catch (CursorIndexOutOfBoundsException e){
                    currentNumber=0;
                }
                for (int i = 0; i < count; i++) {
                    currentNumber++;
                    h.addOne(new Table(0,false,"",0,"",currentNumber));
                }
                popupWindow.dismiss();
                h.close();
                loadFragments();

            });
        });


    }
    public void loadFragments(){
         sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
         viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
         tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}