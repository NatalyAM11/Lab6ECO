package com.example.sextolabeco;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sextolabeco.model.Usuario;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.SocketHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener   {

    private EditText username;
    private EditText password;
    private Button bIngresar;
    Socket socket;
    BufferedWriter writer;
    BufferedReader reader;
    String name,contrasena, id;
    String line, ingreso, noticia;
    String [] mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        bIngresar = findViewById(R.id.bIngresar);

        //
        initCliente();

        //Boton
        bIngresar.setOnClickListener(this);

    }

    public void initCliente(){

        new Thread(
                () -> {

                    try {
                        //Creo el socket del usuario
                        Log.e(">", "Esperando conexión");
                        socket = new Socket("192.168.0.5", 5000);
                        Log.e(">", "Conectado con el servidor");

                        //Inputs y Outputs
                        OutputStream os= socket.getOutputStream();
                        InputStream is= socket.getInputStream();

                        //Reader and writer
                        reader= new BufferedReader(new InputStreamReader(is));
                        writer = new BufferedWriter(new OutputStreamWriter(os));

                        //Recepción del mensaje
                        while (true) {
                            line = reader.readLine();

                            Log.e(">", "Llego el mensaje");
                            Log.e(">", line);
                            mensaje=line.split(":");
                            ingreso=mensaje[0];
                            noticia=mensaje[1];


                            //Verifico el mensaje que deje pasar o no a la siguiente pantalla
                            verificarUser();
                        }



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }


    public void onClick(View view)  {

        //Creo el Gson
        Gson gson= new Gson();

        //Obtengo los datos que mete el usuario
        name = username.getText().toString();
        contrasena = password.getText().toString();
        id= UUID.randomUUID().toString();

        Usuario usuario= new Usuario(name,contrasena,id);
        String json= gson.toJson(usuario);


        //No lo dejo pasar si no lleno todos los campos
        if (name.trim().isEmpty() || contrasena.trim().isEmpty()){
            Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        //metodo para enciar el mensaje
        enviar(json);
        Log.e(">", json);


        //verificarUser();

      /*  new Thread(
            ()->{
                if(line!=null) {
                    if (line.equals("Exito")) {
                    Intent i = new Intent(this, Pantalla2.class);
                    startActivity(i);
                    Log.e(">", "quepajo");
                }

                    if (line.equals("Fail")) {
                      runOnUiThread(
                              ()->{
                                  Toast.makeText(this,"Este usuario no esta registrado", Toast.LENGTH_LONG).show();
                              }
                      );
                    }

            }
        }

        ).start();*/

    }



    public void enviar (String mensaje) {

        //Junto todos los datos en una variable para enviarlos
       // Serializable mensajeFinal= mensaje;

        //Ya envio el mensaje
        new Thread(
                ()-> {

                    try {
                        if(mensaje!=null) {
                            writer.write(mensaje + "\n");
                            writer.flush();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }


    //metodo que verifica el mensaje que le envio el server para ver si lo deja pasar a la otra pantalla o no
    public void verificarUser(){

        new Thread(
                ()->{
                    if(line!=null) {
                        //Si lo deja pasar
                        if (ingreso.equals("Exito")) {
                            Intent i = new Intent(this, Pantalla2.class);
                            startActivity(i);
                            Log.e(">", noticia);
                        }

                        //No lo deja pasar y le tira el aviso
                        if (ingreso.equals("Fail")) {
                            Log.e(">", noticia);
                            runOnUiThread(
                                    ()->{
                                        Toast.makeText(this,"Este usuario no esta registrado", Toast.LENGTH_LONG).show();
                                    }
                            );
                        }

                    }
                }

        ).start();
    }
}