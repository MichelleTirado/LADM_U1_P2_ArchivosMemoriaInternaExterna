package mx.edu.ittepic.ladm_u1_practica2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
 var radioButton4: RadioButton? = null
    var radioButton5: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        radioButton4 = findViewById(R.id.radioButton4)
        radioButton5 = findViewById(R.id.radioButton5)

        button.setOnClickListener {
            if(radioButton4?.isChecked == true) {
            GuardarArchivoInterno()
            }else if (radioButton5?.isChecked == true){
                //Pregunta si esta denegado para solicitarlo
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    //SI ENTRA ENTONCES AUN NO SE OTORGAN LOS PERMISOS
                    //EL SIGUIENTE CODIGO LOS SOLICITA
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 0
                    ) //0 nO VOY A SOLICITAR EL PERMISO DE SE PUDO O NO
                } else {
                    mensaje("PERMISOS YA OTORGADOS")
                }
                GuardarArchivoSD()

            }
    }
        button4.setOnClickListener {
            if (radioButton4?.isChecked == true) {
                AbrirArchivoInterno()
            }else if (radioButton5 ?.isChecked == true){
                //Pregunta si esta denegado para solicitarlo
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    //SI ENTRA ENTONCES AUN NO SE OTORGAN LOS PERMISOS
                    //EL SIGUIENTE CODIGO LOS SOLICITA
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 0
                    ) //0 nO VOY A SOLICITAR EL PERMISO DE SE PUDO O NO
                } else {
                    mensaje("PERMISOS YA OTORGADOS")
                }
                AbrirArchivoSD()

            }
        }

        button2.setOnClickListener {
            finish()
        }
    }


    fun GuardarArchivoInterno(){
        try {

            var flujoSalida=OutputStreamWriter(openFileOutput("archivo.txt", Context.MODE_PRIVATE))
            var data=editText.text.toString()+"&"+
                    editText3.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()//Subirlo
            flujoSalida.close()

            mensaje("Exito! El archivo se guardo correctamente")
            ponerTexto("", "")

        }catch (error:IOException){
            mensaje(error.message.toString())
        }
    }//GuardarArchivoInterno



    fun GuardarArchivoSD(){
        if (noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        try {

            var rutaSD = Environment.getExternalStorageDirectory()
            var  datosAchivo = File(rutaSD.absolutePath,"archivosd.txt")

            var flujoSalida=OutputStreamWriter(FileOutputStream(datosAchivo))
            var data=editText.text.toString()+"&"+
                    editText3.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()//Subirlo
            flujoSalida.close()

            mensaje("Exito! El archivo se guardo correctamente")
            ponerTexto("", "")

        }catch (error:IOException){
            mensaje(error.message.toString())
        }
    }//GuardarArchivoSD


    fun noSD():Boolean{
        var  estado = Environment.getExternalStorageState()
        if(estado!=Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }

    fun AbrirArchivoInterno(){

        try {
            var flujoEntrada=BufferedReader(InputStreamReader(openFileInput("archivo.txt")))
            var data=flujoEntrada.readLine() //Contenido del archivo
            var vector=data.split("&")

            ponerTexto(vector[0],vector[1])
            flujoEntrada.close()

        }catch (error:IOException) {
            mensaje(error.message.toString())
        }
    }
    fun AbrirArchivoSD() {
        if (noSD()) {
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, "archivosd.txt")

            var flujoEntrada = BufferedReader(
                InputStreamReader(FileInputStream(datosArchivo))
            )
            var data = flujoEntrada.readLine() //Contenido del archivo
            var vector = data.split("&")

            ponerTexto(vector[0], vector[1])
            flujoEntrada.close()

        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }
    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("Atencion")
            .setMessage(m)
            .setPositiveButton("Ok"){
                    d,i->
            }
            .show()
    }
    fun ponerTexto(t1:String,t2:String){
        editText.setText(t1)
        editText3.setText(t2)
    }

}
