package domingomg.faycan.plantilladocxandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends AppCompatActivity
{
    private static String name, surname, date;
    private static EditText txtNombre, txtApellidos, txtFecha;
    private static Button btnGenerar;

    public static void generateDocument(String rutaPlantilla, String extension, boolean convertirPdf) throws IOException, XDocReportException
    {
        Map<String, Object> variablesMap = new HashMap<String, Object>();

        variablesMap.put("name", name);
        variablesMap.put("surname", surname);
        variablesMap.put("date", date);

        // 2) Create fields metadata to manage lazy loop (#forech velocity)
        // for table row.
        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsList("listaNumeros.Numero");
        metadata.addFieldAsList("listaNumeros.Cuadrado");
        metadata.addFieldAsList("listaNumeros.Raiz");

        // Mapa con las variables de tipo imagen. Estas variables contienen el path de la imagen
        Map<String, String> imagenesMap = new HashMap<String, String>();
        imagenesMap.put("header_image_logo", "./Logo.png");

        GeneradorDocumentosService generadorDocumentosService = new GeneradorDocumentosService();
        byte[] mergedOutput = generadorDocumentosService.generarDocumento(rutaPlantilla,
                TemplateEngineKind.Freemarker, variablesMap, imagenesMap, convertirPdf
                , metadata
        );

        FileOutputStream os = new FileOutputStream("Faycan_Contract_"+"." +extension);
        os.write(mergedOutput);
        os.flush();
        os.close();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtFecha = (EditText) findViewById(R.id.txtFecha);

        name = txtNombre.getText().toString();
        surname = txtApellidos.getText().toString();
        date = txtFecha.getText().toString();

        btnGenerar = (Button) findViewById(R.id.btnGenerar);
        btnGenerar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    generateDocument("file:///android_asset/test.docx", "docx", false);
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "The document could not be generated"+e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
