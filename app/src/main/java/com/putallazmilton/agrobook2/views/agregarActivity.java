package com.putallazmilton.agrobook2.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.RequestQueue;

import com.getbase.floatingactionbutton.FloatingActionButton;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.putallazmilton.agrobook2.R;
import com.putallazmilton.agrobook2.models.Problema;
import com.putallazmilton.agrobook2.retrofit.RetrofitInterface;
import com.putallazmilton.agrobook2.socket.sockets;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class agregarActivity extends AppCompatActivity {

    File photoFile = null;
    ImageView vi;

    private RequestQueue requestQueue;
    Bitmap bitmap;
    String PathImagen;
    private Socket mSocket;
    private String url= "http://192.168.1.2:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        PathImagen="";


        final EditText eddescripcion = (EditText) findViewById(R.id.etdescripcion);
        FloatingActionButton fab_camara=(FloatingActionButton) findViewById(R.id.fab_camara);


        mSocket= sockets.getInstance().getmSocket();
        final FloatingActionButton fab_galeria = (FloatingActionButton) findViewById(R.id.fab_galeria);
        Button btnconfirmar = (Button) findViewById(R.id.btnconfirmar);

         vi = (ImageView) findViewById(R.id.imageview);

        fab_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();

            }
        });

        fab_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });

        btnconfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(eddescripcion.getText().toString().trim().length()==0)) {
                if(!(PathImagen.toString().trim().length()==0)){

                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(5, TimeUnit.MINUTES)
                            .connectTimeout(5, TimeUnit.MINUTES)
                            .build();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .client(okHttpClient)
                            .build();


                    RetrofitInterface service = retrofit.create(RetrofitInterface.class);
                    File file = new File(PathImagen);
                    RequestBody imagen = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("imagen", file.getName(), imagen);
                    String des = eddescripcion.getText().toString();
                    RequestBody descripcion = RequestBody.create(MediaType.parse("multipart/form-data"), des);
                    String user = "Milton";
                    RequestBody usuario = RequestBody.create(MediaType.parse("multipart/form-data"), user);


                    Call<ResponseBody> call = service.upload(body, descripcion, usuario);
                    call.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, final retrofit2.Response<ResponseBody> response) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(agregarActivity.this);
                            builder.setMessage("Su problema se ha publicado con exito!")
                                    .setTitle("Exito!!")
                                    .setCancelable(false)
                                    .setNeutralButton("Aceptar",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                    if(mSocket.connected()){
                                                        try {
                                                            JSONObject object=new JSONObject(response.body().string());
                                                            mSocket.emit("nuevo_problema",object);

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "No conectado a socket", Toast.LENGTH_LONG);
                                                    }
                                                    Intent inte = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(inte);
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
                        }
                    });
           } else {
                      Toast.makeText(getApplicationContext(),"Debe ingresar una imagen!",Toast.LENGTH_LONG).show();
                    }



                } else {
                    Toast.makeText(getApplicationContext(),"Debe ingresar una descripcion!",Toast.LENGTH_LONG).show();
                }
                }

        });

    }



    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),1);
    }


    private void cameraIntent()
    {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                bitmap=onSelectFromGalleryResult(data);
                vi.setImageBitmap(bitmap);

            }
            else if (requestCode == 0) {
                PathImagen=Environment.getExternalStorageDirectory()+File.separator + "image.jpg";
                File file = new File(PathImagen);
                 bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000, 700);
                vi.setImageBitmap(bitmap);


            }
        }
    }



    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }



    @SuppressWarnings("deprecation")
    private Bitmap onSelectFromGalleryResult(Intent data) {
    Bitmap bmp=null;

        if (data != null) {
            try {
                bmp = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                PathImagen= getRealPathFromURI(data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bmp;
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
