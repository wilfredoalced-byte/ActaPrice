package com.example.actaprice;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etExpediente, etAgenteFiscalizado, etCodigoOsinergmin, etRegistroHidrocarburos;
    private EditText etFechaDiligencia, etHoraApertura, etHoraCierre;
    private EditText etDireccion, etDistrito, etProvincia, etDepartamento, etRucDni, etTelefonoFax;
    private EditText etDniFiscalizador, etNombresFiscalizador;
    private EditText etDieselPrice, etDieselPublicado, etDieselSurtidor, etDieselDescuento;
    private EditText etG84Price, etG84Publicado, etG84Surtidor, etG84Descuento;
    private EditText etRegularPrice, etRegularPublicado, etRegularSurtidor, etRegularDescuento;
    private EditText etPremiumPrice, etPremiumPublicado, etPremiumSurtidor, etPremiumDescuento;
    private EditText etGlpAutoPrice, etGlpAutoPublicado, etGlpAutoSurtidor, etGlpAutoDescuento;
    private EditText etGlp3kg, etGlp5kg, etGlp10kg, etGlp15kg, etGlp45kg, etGlpOtros, etGlpMarca;
    private RadioGroup rgTelefonoPublicado, rgTelefonoActualizado, rgHorarioPublicado;
    private EditText etHecho1, etHecho2, etHecho3, etHecho4, etHecho5, etHecho6;
    private EditText etOtrasOcurrencias, etDocumentacionRecabada, etManifestaciones, etNegativa;
    private EditText etReceptorDni, etReceptorNombres, etReceptorRelacion;
    private Button btnGuardar, btnGenerarPdf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enlazarVistas();
        configurarSelectorFecha();
        configurarSelectoresHora();
        configurarBotones();
    }

    private void enlazarVistas() {
        etExpediente = findViewById(R.id.et_expediente);
        etAgenteFiscalizado = findViewById(R.id.et_agente_fiscalizado);
        etCodigoOsinergmin = findViewById(R.id.et_codigo_osinergmin);
        etRegistroHidrocarburos = findViewById(R.id.et_registro_hidrocarburos);
        etFechaDiligencia = findViewById(R.id.et_fecha_diligencia);
        etHoraApertura = findViewById(R.id.et_hora_apertura);
        etHoraCierre = findViewById(R.id.et_hora_cierre);
        etDireccion = findViewById(R.id.et_direccion);
        etDistrito = findViewById(R.id.et_distrito);
        etProvincia = findViewById(R.id.et_provincia);
        etDepartamento = findViewById(R.id.et_departamento);
        etRucDni = findViewById(R.id.et_ruc_dni);
        etTelefonoFax = findViewById(R.id.et_telefono_fax);
        etDniFiscalizador = findViewById(R.id.et_dni_fiscalizador);
        etNombresFiscalizador = findViewById(R.id.et_nombres_fiscalizador);
        etDieselPrice = findViewById(R.id.et_diesel_price);
        etDieselPublicado = findViewById(R.id.et_diesel_publicado);
        etDieselSurtidor = findViewById(R.id.et_diesel_surtidor);
        etDieselDescuento = findViewById(R.id.et_diesel_descuento);
        etG84Price = findViewById(R.id.et_g84_price);
        etG84Publicado = findViewById(R.id.et_g84_publicado);
        etG84Surtidor = findViewById(R.id.et_g84_surtidor);
        etG84Descuento = findViewById(R.id.et_g84_descuento);
        etRegularPrice = findViewById(R.id.et_regular_price);
        etRegularPublicado = findViewById(R.id.et_regular_publicado);
        etRegularSurtidor = findViewById(R.id.et_regular_surtidor);
        etRegularDescuento = findViewById(R.id.et_regular_descuento);
        etPremiumPrice = findViewById(R.id.et_premium_price);
        etPremiumPublicado = findViewById(R.id.et_premium_publicado);
        etPremiumSurtidor = findViewById(R.id.et_premium_surtidor);
        etPremiumDescuento = findViewById(R.id.et_premium_descuento);
        etGlpAutoPrice = findViewById(R.id.et_glp_auto_price);
        etGlpAutoPublicado = findViewById(R.id.et_glp_auto_publicado);
        etGlpAutoSurtidor = findViewById(R.id.et_glp_auto_surtidor);
        etGlpAutoDescuento = findViewById(R.id.et_glp_auto_descuento);
        etGlp3kg = findViewById(R.id.et_glp_3kg);
        etGlp5kg = findViewById(R.id.et_glp_5kg);
        etGlp10kg = findViewById(R.id.et_glp_10kg);
        etGlp15kg = findViewById(R.id.et_glp_15kg);
        etGlp45kg = findViewById(R.id.et_glp_45kg);
        etGlpOtros = findViewById(R.id.et_glp_otros);
        etGlpMarca = findViewById(R.id.et_glp_marca);
        rgTelefonoPublicado = findViewById(R.id.rg_telefono_publicado);
        rgTelefonoActualizado = findViewById(R.id.rg_telefono_actualizado);
        rgHorarioPublicado = findViewById(R.id.rg_horario_publicado);
        etHecho1 = findViewById(R.id.et_hecho1);
        etHecho2 = findViewById(R.id.et_hecho2);
        etHecho3 = findViewById(R.id.et_hecho3);
        etHecho4 = findViewById(R.id.et_hecho4);
        etHecho5 = findViewById(R.id.et_hecho5);
        etHecho6 = findViewById(R.id.et_hecho6);
        etOtrasOcurrencias = findViewById(R.id.et_otras_ocurrencias);
        etDocumentacionRecabada = findViewById(R.id.et_documentacion_recabada);
        etManifestaciones = findViewById(R.id.et_manifestaciones);
        etNegativa = findViewById(R.id.et_negativa);
        etReceptorDni = findViewById(R.id.et_receptor_dni);
        etReceptorNombres = findViewById(R.id.et_receptor_nombres);
        etReceptorRelacion = findViewById(R.id.et_receptor_relacion);
        btnGuardar = findViewById(R.id.btn_guardar);
        btnGenerarPdf = findViewById(R.id.btn_generar_pdf);
    }

    private void configurarSelectorFecha() {
        etFechaDiligencia.setFocusable(false);
        etFechaDiligencia.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();
            int anio = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialogo = new DatePickerDialog(MainActivity.this,
                    (view, anioSeleccionado, mesSeleccionado, diaSeleccionado) -> {
                        String fecha = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                                diaSeleccionado, mesSeleccionado + 1, anioSeleccionado);
                        etFechaDiligencia.setText(fecha);
                        etFechaDiligencia.setError(null);
                    }, anio, mes, dia);
            dialogo.show();
        });
    }

    private void configurarSelectoresHora() {
        etHoraApertura.setFocusable(false);
        etHoraApertura.setOnClickListener(v -> mostrarSelectorHora(etHoraApertura));
        etHoraCierre.setFocusable(false);
        etHoraCierre.setOnClickListener(v -> mostrarSelectorHora(etHoraCierre));
    }

    private void mostrarSelectorHora(EditText campoDestino) {
        Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);

        TimePickerDialog dialogo = new TimePickerDialog(this,
                (view, horaSeleccionada, minutoSeleccionado) -> {
                    String horaTexto = String.format(Locale.getDefault(), "%02d:%02d", horaSeleccionada, minutoSeleccionado);
                    campoDestino.setText(horaTexto);
                }, hora, minuto, true);
        dialogo.show();
    }

    private void configurarBotones() {
        btnGuardar.setOnClickListener(v -> {
            if (validarDatosObligatorios()) {
                Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        btnGenerarPdf.setOnClickListener(v -> {
            if (!validarDatosObligatorios()) return;
            try {
                DatosActa datos = recolectarDatos();
                File archivoPdf = PdfGenerator.generar(this, datos);
                Toast.makeText(this, "PDF generado: " + archivoPdf.getName(), Toast.LENGTH_LONG).show();
                abrirPdf(archivoPdf);
            } catch (IOException e) {
                Toast.makeText(this, "No se pudo generar el PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validarDatosObligatorios() {
        View primerCampoConError = null;
        boolean esValido = true;

        if (TextUtils.isEmpty(etExpediente.getText())) {
            etExpediente.setError("Ingrese el número de expediente");
            primerCampoConError = etExpediente;
            esValido = false;
        }
        if (TextUtils.isEmpty(etAgenteFiscalizado.getText())) {
            etAgenteFiscalizado.setError("Ingrese el agente fiscalizado");
            if (primerCampoConError == null) primerCampoConError = etAgenteFiscalizado;
            esValido = false;
        }
        if (TextUtils.isEmpty(etCodigoOsinergmin.getText())) {
            etCodigoOsinergmin.setError("Ingrese el código Osinergmin");
            if (primerCampoConError == null) primerCampoConError = etCodigoOsinergmin;
            esValido = false;
        }
        if (TextUtils.isEmpty(etFechaDiligencia.getText())) {
            etFechaDiligencia.setError("Seleccione la fecha de diligencia");
            if (primerCampoConError == null) primerCampoConError = etFechaDiligencia;
            esValido = false;
        }
        if (TextUtils.isEmpty(etDniFiscalizador.getText())) {
            etDniFiscalizador.setError("Ingrese el DNI del fiscalizador");
            if (primerCampoConError == null) primerCampoConError = etDniFiscalizador;
            esValido = false;
        }
        if (TextUtils.isEmpty(etNombresFiscalizador.getText())) {
            etNombresFiscalizador.setError("Ingrese los nombres y apellidos del fiscalizador");
            if (primerCampoConError == null) primerCampoConError = etNombresFiscalizador;
            esValido = false;
        }

        if (!esValido) {
            Toast.makeText(this, "Complete los campos obligatorios marcados en rojo", Toast.LENGTH_LONG).show();
            if (primerCampoConError != null) primerCampoConError.requestFocus();
        }
        return esValido;
    }

    private DatosActa recolectarDatos() {
        DatosActa datos = new DatosActa();

        datos.establecimiento.put("Expediente", texto(etExpediente));
        datos.establecimiento.put("Agente fiscalizado", texto(etAgenteFiscalizado));
        datos.establecimiento.put("Código Osinergmin", texto(etCodigoOsinergmin));
        datos.establecimiento.put("Registro de Hidrocarburos", texto(etRegistroHidrocarburos));
        datos.establecimiento.put("Fecha de diligencia", texto(etFechaDiligencia));
        datos.establecimiento.put("Hora de apertura", texto(etHoraApertura));
        datos.establecimiento.put("Hora de cierre", texto(etHoraCierre));
        datos.establecimiento.put("Dirección", texto(etDireccion));
        datos.establecimiento.put("Distrito", texto(etDistrito));
        datos.establecimiento.put("Provincia", texto(etProvincia));
        datos.establecimiento.put("Departamento", texto(etDepartamento));
        datos.establecimiento.put("RUC/DNI", texto(etRucDni));
        datos.establecimiento.put("Teléfono/Fax", texto(etTelefonoFax));

        datos.fiscalizador.put("DNI del fiscalizador", texto(etDniFiscalizador));
        datos.fiscalizador.put("Nombres y apellidos", texto(etNombresFiscalizador));

        datos.preciosLiquidos = new String[][]{
                {"Diésel B5 / B5-S50", texto(etDieselPrice), texto(etDieselPublicado), texto(etDieselSurtidor), texto(etDieselDescuento)},
                {"G-84 / Gasohol 84 Plus", texto(etG84Price), texto(etG84Publicado), texto(etG84Surtidor), texto(etG84Descuento)},
                {"Gasolina/Gasohol Regular", texto(etRegularPrice), texto(etRegularPublicado), texto(etRegularSurtidor), texto(etRegularDescuento)},
                {"Gasolina/Gasohol Premium", texto(etPremiumPrice), texto(etPremiumPublicado), texto(etPremiumSurtidor), texto(etPremiumDescuento)}
        };

        datos.glpAutomotor = new String[][]{
                {"GLP Automotor", texto(etGlpAutoPrice), texto(etGlpAutoPublicado), texto(etGlpAutoSurtidor), texto(etGlpAutoDescuento)}
        };

        datos.glpCilindros = new String[][]{
                {"3 kg", texto(etGlp3kg)}, {"5 kg", texto(etGlp5kg)}, {"10 kg", texto(etGlp10kg)},
                {"15 kg", texto(etGlp15kg)}, {"45 kg", texto(etGlp45kg)}, {"Otros", texto(etGlpOtros)}
        };
        datos.marcaGlp = texto(etGlpMarca);

        datos.verificaciones.put("¿Teléfono publicado en el establecimiento?", obtenerRespuestaRadio(rgTelefonoPublicado));
        datos.verificaciones.put("¿Teléfono registrado y actualizado en el PRICE?", obtenerRespuestaRadio(rgTelefonoActualizado));
        datos.verificaciones.put("¿Horario de atención publicado?", obtenerRespuestaRadio(rgHorarioPublicado));

        datos.hechosVerificados[0] = texto(etHecho1);
        datos.hechosVerificados[1] = texto(etHecho2);
        datos.hechosVerificados[2] = texto(etHecho3);
        datos.hechosVerificados[3] = texto(etHecho4);
        datos.hechosVerificados[4] = texto(etHecho5);
        datos.hechosVerificados[5] = texto(etHecho6);

        datos.otros.put("Otras ocurrencias detectadas", texto(etOtrasOcurrencias));
        datos.otros.put("Documentación recabada", texto(etDocumentacionRecabada));
        datos.otros.put("Manifestaciones u observaciones", texto(etManifestaciones));
        datos.otros.put("Negativa del agente fiscalizado", texto(etNegativa));

        datos.receptor.put("DNI", texto(etReceptorDni));
        datos.receptor.put("Apellidos y nombres", texto(etReceptorNombres));
        datos.receptor.put("Relación con el agente fiscalizado", texto(etReceptorRelacion));

        return datos;
    }

    private String texto(EditText campo) {
        return campo.getText().toString().trim();
    }

    private String obtenerRespuestaRadio(RadioGroup grupo) {
        int idSeleccionado = grupo.getCheckedRadioButtonId();
        if (idSeleccionado == -1) return "No indicado";
        RadioButton opcion = grupo.findViewById(idSeleccionado);
        return opcion.getText().toString();
    }

    private void abrirPdf(File archivo) {
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Instala un lector de PDF. Archivo guardado en:\n" + archivo.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }
}