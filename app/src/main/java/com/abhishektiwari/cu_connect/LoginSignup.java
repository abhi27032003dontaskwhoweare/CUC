package com.abhishektiwari.cu_connect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginSignup extends AppCompatActivity{

    CardView loginbutton,signupbutton,one,two,three,four;
    TextView login,signup;
    LinearLayout view;
    boolean opened;
    String steps;
    int i=0;
    FirebaseUser user;

    SharedPreferences sharedpreferences;
    stepsindicator s;

    @Override
    protected void onStart() {
        super.onStart();
        user=FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null)
        {
           // Intent intent=new Intent(LoginSignup.this,MainActivity.class);
           // LoginSignup.this.startActivity(intent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        sharedpreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("step", String.valueOf(4));
        steps = sharedpreferences.getString("step", null);

        getsteps();
        s=new stepsindicator();
        loginbutton=findViewById(R.id.loginbutton);
        signupbutton=findViewById(R.id.signupbutton);

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                s.getI();
                indicatestep();
                handler.postDelayed(this, 1000);
            }
        });


        indicatestep();
        one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);
        four=findViewById(R.id.four);
        login=findViewById(R.id.logintext);
        signup=findViewById(R.id.signuptext);
        view = findViewById(R.id.up_downlayout);
        view.setVisibility(View.INVISIBLE);



        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.logsincon,new Login()).commit();
        loginbutton.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.red)));
        login.setTextColor(getResources().getColor(R.color.white));
        signup.setTextColor(getResources().getColor(R.color.black));
        i=0;

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s.setI(1);
                indicatestep();
                fragmentManager.beginTransaction().setCustomAnimations(
                        // exit
                        R.anim.slide_up,   // popEnter
                        R.anim.slide_down  // popExit
                ).replace(R.id.logsincon,new Sign_up()).commit();
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getsteps();
                if(Integer.valueOf(steps)>=2)
                {
                    s.setI(2);
                    indicatestep();
                    fragmentManager.beginTransaction().setCustomAnimations(
                            // exit
                            R.anim.slide_up,   // popEnter
                            R.anim.slide_down  // popExit
                    ).replace(R.id.logsincon,new Steptwo()).commit();
                }
                else
                {
                    Toast.makeText(LoginSignup.this, "complete previous steps first", Toast.LENGTH_SHORT).show();
                }


            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getsteps();
                if(Integer.valueOf(steps)>=3) {
                    s.setI(3);
                    indicatestep();
                    fragmentManager.beginTransaction().setCustomAnimations(
                            // exit
                            R.anim.slide_up,   // popEnter
                            R.anim.slide_down  // popExit
                    ).replace(R.id.logsincon,new stepthree()).commit();
                }
                else
                {
                    Toast.makeText(LoginSignup.this, "complete previous steps first", Toast.LENGTH_SHORT).show();
                }



            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getsteps();
                if(Integer.valueOf(steps)>=3) {
                    s.setI(4);
                    indicatestep();
                    fragmentManager.beginTransaction().setCustomAnimations(
                            // exit
                            R.anim.slide_up,   // popEnter
                            R.anim.slide_down  // popExit
                    ).replace(R.id.logsincon,new stepfour()).commit();
                }

                else
                {
                    Toast.makeText(LoginSignup.this, "complete previous steps first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(i!=0)
                {
                    openmenu();
                }
                i=0;
                loginbutton.setClickable(false);
                signupbutton.setClickable(true);
                login.setTextColor(getResources().getColor(R.color.white));
                signup.setTextColor(getResources().getColor(R.color.black));
                loginbutton.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.red)));
                signupbutton.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
                fragmentManager.beginTransaction().setCustomAnimations(
                        // exit
                        R.anim.slide_up,   // popEnter
                        R.anim.slide_down  // popExit
                ).replace(R.id.logsincon,new Login()).commit();

            }
        });
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0)
                {
                    openmenu();
                    one.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                    two.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    three.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    four.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));

                }

                i=1;
                loginbutton.setClickable(true);
                signupbutton.setClickable(false);
                login.setTextColor(getResources().getColor(R.color.black));
                signup.setTextColor(getResources().getColor(R.color.white));
                loginbutton.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
                signupbutton.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.red)));
                fragmentManager.beginTransaction().setCustomAnimations(
                        // exit
                        R.anim.slide_up,   // popEnter
                        R.anim.slide_down  // popExit
                ).replace(R.id.logsincon,new Sign_up()).commit();
            }



        });


    }

    private void getsteps() {
        steps = sharedpreferences.getString("step", null);
    }

    private void indicatestep() {
        if(s.getI()==1)
        {
            one.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
            two.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
            three.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
            four.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));

        }
        else if(s.getI()==2)
        {
            one.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
            two.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
            three.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
            four.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));



        }
        else if(s.getI()==3)
        {
            one.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
            two.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
            three.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
            four.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));

        }
        else if(s.getI()==4)
        {
            one.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
            two.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
            three.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.white)));
            four.setBackgroundTintList(  ColorStateList.valueOf(getResources().getColor(R.color.yellow)));



        }
        else {

        }

    }

    private void openmenu() {
        if(!opened){
            view.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,
                    0,
                    view.getHeight(),
                    0);
            animate.setDuration(1000);
            animate.setFillAfter(true);
            view.startAnimation(animate);
        } else {
            view.setVisibility(View.INVISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,
                    0,
                    0,
                    view.getHeight());
            animate.setDuration(1000);
            animate.setFillAfter(true);
            view.startAnimation(animate);
        }
        opened = !opened;
    }


}