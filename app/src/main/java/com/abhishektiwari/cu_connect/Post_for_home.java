package com.abhishektiwari.cu_connect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UProperty;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Post_for_home extends AppCompatActivity {

    ImageView imagepicker,cancel;
    EditText et_post;
    int i = 0,count;
    TextView adlinkbtn;
    AppCompatButton bt_submit;
    ImageButton bt_photo, bt_link, bt_setting;
    TextView category_button;
    String link="null"
            ,text="null"
            ,image_link="null";
    CardView linkbox;
    FirebaseStorage storage;
    StorageReference storageReference;
    String[] cameraPermission;
    String[] storagePermission;
    Uri imageuri;
    Dialog dialoglink;
    boolean a = false;
    int k=0;
    String[] listItems;
    boolean[] checkedItems;
    List<String> selectedItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_for_home);

        imageuri=null;
        linkbox=findViewById(R.id.linkview);
        linkbox.setVisibility(View.GONE);
        cancel=findViewById(R.id.cancel_bth);
        imagepicker = findViewById(R.id.imagepickerimage);
        et_post = findViewById(R.id.et_post);
        bt_submit = findViewById(R.id.bt_submit);
        bt_photo = findViewById(R.id.bt_photo);
        bt_link = findViewById(R.id.bt_link);
        bt_setting = findViewById(R.id.bt_setting);
        category_button = findViewById(R.id.category_post);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        bt_submit.setEnabled(true);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkbox.setVisibility(View.GONE);
                link=null;
            }
        });
        listItems = new String[]{"C", "C++", "JAVA", "PYTHON"};
        checkedItems = new boolean[listItems.length];
        selectedItems = Arrays.asList(listItems);

        category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_button.setText(null);
                AlertDialog.Builder builder = new AlertDialog.Builder(Post_for_home.this);
                builder.setTitle("Choose Items");
                builder.setIcon(R.mipmap.nonbglogo);
                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                        String currentItem = selectedItems.get(which);
                    }
                });

                builder.setCancelable(false);
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                category_button.setText(category_button.getText() + selectedItems.get(i) + "  ");
                            }
                        }
                        if (selectedItems.size() > 0) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                category_button.setText(category_button.getText() + selectedItems.get(i) + "  ");
                            }
                        }
                        if (selectedItems.size() > 0) {

                        } else {
                            i = 0;
                        }
                    }
                });
                builder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                        }
                        i = 0;
                    }
                });
                builder.create();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(Post_for_home.this).compress(150)
                        .crop().maxResultSize(1080,1080).start(101);
            }
        });
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialoglink=new Dialog(Post_for_home.this);
        dialoglink.setContentView(R.layout.linkbox);
        dialoglink.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialoglink.getWindow().setLayout((6 * width)/7, (4 * height)/12);
        adlinkbtn=dialoglink.findViewById(R.id.addlink_bth);
        TextView linktext=findViewById(R.id.link);
        adlinkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText lnk=dialoglink.findViewById(R.id.linktext);
                link=lnk.getText().toString();
                if(link.isEmpty())
                {
                    linktext.setText(null);
                    linkbox.setVisibility(View.GONE);
                }
                else
                {
                    linktext.setText(link);
                    linkbox.setVisibility(View.VISIBLE);


                }
                dialoglink.dismiss();
            }
        });
        bt_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialoglink.show();


            }
        });

        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Post_for_home.this, "MENU", Toast.LENGTH_SHORT).show();
            }
        });




        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=false;
                text=et_post.getText().toString();
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        a=true;
                    }
                }
                if(!text.isEmpty() && a==true)
                {
                    uploadImage();
                }
            }
        });


    }
    private void uploadImage()
    {
        Toast.makeText(this, "In upload image", Toast.LENGTH_SHORT).show();
        if (imageuri != null) {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images"+ UUID.randomUUID().toString());

            ref.putFile(imageuri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(Post_for_home.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            image_link=uri.toString();
                                            savepost(text,link,image_link);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Post_for_home.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            progressDialog.dismiss();
                            Toast
                                    .makeText(Post_for_home.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress=(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+ (int)progress + "%");
                                }
                            });
                    }
        else
        {
            savepost(text,link,image_link);
            Toast.makeText(this, "image uri is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void savepost(String text, String link, String image_link) {

        if(link==null && image_link==null)
        {
            link="null";
            image_link="null";
        }
        else if(image_link==null)
        {
            image_link="null";
        }
        else if(link==null)
        {
            link="null";
        }
        else
        {

        }

        home_post_data home_post_data=new home_post_data(image_link,text,link,0,0);
        home_post_data_for_profile home_post_data_for_profile=new home_post_data_for_profile(image_link,text,link);
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                FirebaseUser user=auth.getCurrentUser();
                assert user != null;
                String uid= user.getUid();
                if(user!=null)
                {
                    count=snapshot.child("users").child(uid).child("Total Posts").getValue(Integer.class);
                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("Posts").child(String.valueOf(count+1)).setValue(home_post_data_for_profile);
                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("Total Posts").setValue(count+1);
                    for (int i = 0; i < checkedItems.length; i++) {

                        if (checkedItems[i]) {

                            FirebaseDatabase.getInstance().getReference().child(selectedItems.get(i)).child(uid+":::"+String.valueOf(count + 1)).setValue(home_post_data);

                        }
                    }
                    FirebaseDatabase.getInstance().getReference().child("others").child(uid+":::"+String.valueOf(count + 1)).setValue(home_post_data);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Post_for_home.this, "error message", Toast.LENGTH_SHORT).show();

            }
        });


        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, link, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, image_link, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageuri=data.getData();
        imagepicker.setImageURI(imageuri);

    }
}