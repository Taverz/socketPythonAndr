package com.client.myapplication.client;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    Thread Thread1 = null;

    EditText etIP, etPort;
    TextView tvMessages;

    EditText etMessage;
    Button btnSend;

    String SERVER_IP;
    int SERVER_PORT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etIP = findViewById(R.id.etIP);
        etPort = findViewById(R.id.etPort);
        //test
        etIP.setText("192.168.0.101");
        etPort.setText("8080");

        tvMessages = findViewById(R.id.tvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        Button btnConnect = findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessages.setText("");
                SERVER_IP = etIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(etPort.getText().toString().trim());
                Thread1 = new Thread(new Thread1());
                Thread1.start();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    new Thread(new Thread3(message)).start();
                }
            }
        });
    }

    private PrintWriter output;
    private BufferedReader input;

    class Thread1 implements Runnable {

        @Override
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);

                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                InputStream is = socket.getInputStream();
                byte[] buffer = new byte[8192];
                // ByteArrayOutputStream используется, как накопитель байт, чтобы
                //   потом превратить в строку все полученные данные.
                //   преобразовывать часть потока в строку опасно, т.к.
                //   если данные идут в многобайтной кодировке, один символ может
                //   быть разрезан между чтениями
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // InputStream.read( byte[] ) возвращает количество прочитанных байт
                //   и -1, если поток кончился (сервер закрыл соединение)
                for ( int received; (received = is.read( buffer )) != -1; ) {
                    // записываем прочитанное из потока, от 0 до количества считанных
                    baos.write( buffer, 0, received );
                }

                // преобразуем в строку ( кодировку желательно указывать )
                String reply = baos.toString( "UTF-8" );
                // можно так, но toByteArray() создает копию массива, а я у мамы оптимизатор
                //String reply = new String( baos.toByteArray(), StandardCharsets.UTF_8 );
                Log.d("INPUTSTREAM_loader",reply);
                //tvMessages.setText(reply);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        tvMessages.setText("Connected\n");
                    }
                });
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMessages.append("server: " + message + "\n");
                                Log.d("INPUTSTREAM_loader",message);
                            }
                        });
                    } else {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Thread3 implements Runnable {
        private String message;

        Thread3(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            output.write(message);
            output.flush();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvMessages.append("client: " + message + "\n");
                    etMessage.setText("");
                }
            });
        }
    }
}


