package com.example.actaprice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class PdfGenerator {

    private static final int PAGE_WIDTH = 595;   // A4 a 72 dpi (puntos)
    private static final int PAGE_HEIGHT = 842;
    private static final int MARGIN = 36;

    private PdfDocument documento;
    private PdfDocument.Page pagina;
    private Canvas canvas;
    private int numeroPagina;
    private float y;

    private Paint paintTitulo, paintSubtitulo, paintSeccionTexto, paintSeccionFondo;
    private Paint paintEtiqueta, paintLinea, paintBorde, paintPie, paintFirmaLabel;
    private TextPaint paintValor, paintTablaTexto, paintTablaTextoHeader;

    public static File generar(Context context, DatosActa datos) throws IOException {
        PdfGenerator generador = new PdfGenerator();
        return generador.construir(context, datos);
    }

    private void inicializarPinturas() {
        paintTitulo = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTitulo.setColor(Color.BLACK);
        paintTitulo.setTextSize(16);
        paintTitulo.setFakeBoldText(true);
        paintTitulo.setTextAlign(Paint.Align.CENTER);

        paintSubtitulo = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSubtitulo.setColor(Color.DKGRAY);
        paintSubtitulo.setTextSize(9);
        paintSubtitulo.setTextAlign(Paint.Align.CENTER);

        paintSeccionFondo = new Paint();
        paintSeccionFondo.setColor(Color.rgb(13, 71, 161));
        paintSeccionFondo.setStyle(Paint.Style.FILL);

        paintSeccionTexto = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSeccionTexto.setColor(Color.WHITE);
        paintSeccionTexto.setTextSize(11);
        paintSeccionTexto.setFakeBoldText(true);

        paintEtiqueta = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintEtiqueta.setColor(Color.DKGRAY);
        paintEtiqueta.setTextSize(9);
        paintEtiqueta.setFakeBoldText(true);

        paintValor = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintValor.setColor(Color.BLACK);
        paintValor.setTextSize(10);

        paintLinea = new Paint();
        paintLinea.setColor(Color.rgb(150, 150, 150));
        paintLinea.setStrokeWidth(1f);

        paintBorde = new Paint();
        paintBorde.setColor(Color.rgb(120, 120, 120));
        paintBorde.setStyle(Paint.Style.STROKE);
        paintBorde.setStrokeWidth(0.75f);

        paintTablaTexto = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintTablaTexto.setColor(Color.BLACK);
        paintTablaTexto.setTextSize(8.5f);

        paintTablaTextoHeader = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintTablaTextoHeader.setColor(Color.WHITE);
        paintTablaTextoHeader.setTextSize(8.5f);
        paintTablaTextoHeader.setFakeBoldText(true);

        paintPie = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPie.setColor(Color.GRAY);
        paintPie.setTextSize(7.5f);
        paintPie.setTextAlign(Paint.Align.RIGHT);

        paintFirmaLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFirmaLabel.setColor(Color.DKGRAY);
        paintFirmaLabel.setTextSize(8.5f);
        paintFirmaLabel.setTextAlign(Paint.Align.CENTER);
    }

    private File construir(Context context, DatosActa datos) throws IOException {
        documento = new PdfDocument();
        inicializarPinturas();
        numeroPagina = 0;
        nuevaPagina();

        dibujarEncabezado();
        dibujarSeccionEtiquetaValor("I. DATOS DEL ESTABLECIMIENTO", datos.establecimiento);
        dibujarSeccionEtiquetaValor("II. DATOS DEL FISCALIZADOR", datos.fiscalizador);

        dibujarTituloBloque("III. PRECIOS - COMBUSTIBLES LÍQUIDOS");
        dibujarTablaPrecios(datos.preciosLiquidos);

        dibujarTituloBloque("GLP AUTOMOTOR");
        dibujarTablaPrecios(datos.glpAutomotor);

        dibujarTituloBloque("GLP EN CILINDROS");
        dibujarEtiquetaValor("Marca", valorOGuion(datos.marcaGlp));
        dibujarTablaGlpCilindros(datos.glpCilindros);

        dibujarSeccionEtiquetaValor("IV. VERIFICACIONES", datos.verificaciones);

        dibujarTituloBloque("V. HECHOS VERIFICADOS DURANTE LA FISCALIZACIÓN");
        for (int i = 0; i < datos.hechosVerificados.length; i++) {
            dibujarEtiquetaValor("Hecho verificado N.º " + (i + 1), valorOGuion(datos.hechosVerificados[i]));
        }
        y += 6;

        dibujarSeccionEtiquetaValor("VI. OTROS", datos.otros);
        dibujarSeccionEtiquetaValor("VII. DATOS DE QUIEN RECIBE EL ACTA", datos.receptor);

        dibujarFirmas();
        finalizarPagina();

        File carpeta = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (carpeta == null) carpeta = context.getFilesDir();
        if (!carpeta.exists()) carpeta.mkdirs();

        String expediente = datos.establecimiento.get("Expediente");
        String sufijo = (expediente == null || expediente.trim().isEmpty())
                ? "" : ("_" + expediente.replaceAll("[^A-Za-z0-9]", ""));
        String marcaTiempo = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File archivo = new File(carpeta, "ActaPRICE" + sufijo + "_" + marcaTiempo + ".pdf");

        try (FileOutputStream salida = new FileOutputStream(archivo)) {
            documento.writeTo(salida);
        }
        documento.close();
        return archivo;
    }

    private void nuevaPagina() {
        if (pagina != null) documento.finishPage(pagina);
        numeroPagina++;
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, numeroPagina).create();
        pagina = documento.startPage(info);
        canvas = pagina.getCanvas();
        y = MARGIN;
        canvas.drawText("Página " + numeroPagina, PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 18, paintPie);
    }

    private void finalizarPagina() {
        if (pagina != null) { documento.finishPage(pagina); pagina = null; }
    }

    private void asegurarEspacio(float alturaNecesaria) {
        if (y + alturaNecesaria > PAGE_HEIGHT - MARGIN - 20) nuevaPagina();
    }

    private void dibujarEncabezado() {
        canvas.drawText("ACTA DE FISCALIZACIÓN - CUMPLIMIENTO PRICE", PAGE_WIDTH / 2f, y + 14, paintTitulo);
        y += 22;
        canvas.drawText("Procedimiento de entrega de información de precios de combustibles derivados de hidrocarburos",
                PAGE_WIDTH / 2f, y, paintSubtitulo);
        y += 14;
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, paintLinea);
        y += 14;
    }

    private void dibujarTituloBloque(String titulo) {
        asegurarEspacio(24);
        canvas.drawRect(MARGIN, y, PAGE_WIDTH - MARGIN, y + 18, paintSeccionFondo);
        canvas.drawText(titulo, MARGIN + 5, y + 13, paintSeccionTexto);
        y += 24;
    }

    private void dibujarSeccionEtiquetaValor(String titulo, Map<String, String> filas) {
        dibujarTituloBloque(titulo);
        for (Map.Entry<String, String> fila : filas.entrySet()) {
            dibujarEtiquetaValor(fila.getKey(), valorOGuion(fila.getValue()));
        }
        y += 6;
    }

    private void dibujarEtiquetaValor(String etiqueta, String valor) {
        float anchoDisponible = PAGE_WIDTH - 2 * MARGIN;
        StaticLayout layoutValor = construirStaticLayout(valor, paintValor, (int) (anchoDisponible - 4));
        float alturaFila = Math.max(14, layoutValor.getHeight() + 4);
        asegurarEspacio(alturaFila);

        canvas.drawText(etiqueta + ":", MARGIN, y + 9, paintEtiqueta);
        canvas.save();
        canvas.translate(MARGIN, y + 10);
        layoutValor.draw(canvas);
        canvas.restore();
        y += alturaFila + 2;
    }

    private StaticLayout construirStaticLayout(String texto, TextPaint paint, int ancho) {
        String contenido = (texto == null) ? "" : texto;
        if (ancho < 10) ancho = 10;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return StaticLayout.Builder.obtain(contenido, 0, contenido.length(), paint, ancho)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL).setLineSpacing(0, 1f).build();
        }
        return new StaticLayout(contenido, paint, ancho, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
    }

    private void dibujarTablaPrecios(String[][] filas) {
        dibujarTablaGenerica(new String[]{"Producto", "PRICE", "Publicado", "Surtidor", "Descuento"},
                filas, new float[]{2.2f, 1f, 1f, 1f, 1f});
    }

    private void dibujarTablaGlpCilindros(String[][] filas) {
        dibujarTablaGenerica(new String[]{"Presentación", "Precio (S/)"}, filas, new float[]{1.5f, 1f});
    }

    private void dibujarTablaGenerica(String[] encabezados, String[][] filas, float[] pesos) {
        float anchoDisponible = PAGE_WIDTH - 2 * MARGIN;
        float alturaFila = 18f;
        asegurarEspacio(alturaFila * (filas.length + 1) + 8);

        float sumaPesos = 0;
        for (float p : pesos) sumaPesos += p;
        float[] anchos = new float[pesos.length];
        for (int i = 0; i < pesos.length; i++) anchos[i] = anchoDisponible * (pesos[i] / sumaPesos);

        float filaY = y, celdaX;
        canvas.drawRect(MARGIN, filaY, MARGIN + anchoDisponible, filaY + alturaFila, paintSeccionFondo);
        celdaX = MARGIN;
        for (int i = 0; i < encabezados.length; i++) {
            canvas.drawRect(celdaX, filaY, celdaX + anchos[i], filaY + alturaFila, paintBorde);
            canvas.drawText(encabezados[i], celdaX + 4, filaY + alturaFila - 6, paintTablaTextoHeader);
            celdaX += anchos[i];
        }
        filaY += alturaFila;

        for (String[] fila : filas) {
            celdaX = MARGIN;
            for (int i = 0; i < anchos.length; i++) {
                String texto = (i < fila.length) ? valorOGuion(fila[i]) : "-";
                canvas.drawRect(celdaX, filaY, celdaX + anchos[i], filaY + alturaFila, paintBorde);
                canvas.drawText(recortarTexto(texto, paintTablaTexto, anchos[i] - 6), celdaX + 4, filaY + alturaFila - 6, paintTablaTexto);
                celdaX += anchos[i];
            }
            filaY += alturaFila;
        }
        y = filaY + 10;
    }

    private String recortarTexto(String texto, Paint paint, float anchoMax) {
        if (texto == null) return "-";
        if (paint.measureText(texto) <= anchoMax) return texto;
        String recortado = texto;
        while (recortado.length() > 1 && paint.measureText(recortado + "…") > anchoMax) {
            recortado = recortado.substring(0, recortado.length() - 1);
        }
        return recortado + "…";
    }

    private void dibujarFirmas() {
        asegurarEspacio(70);
        y += 30;
        float mitad = PAGE_WIDTH / 2f;
        canvas.drawLine(MARGIN + 10, y, mitad - 20, y, paintLinea);
        canvas.drawLine(mitad + 20, y, PAGE_WIDTH - MARGIN - 10, y, paintLinea);
        y += 12;
        canvas.drawText("Firma del Fiscalizador", (MARGIN + 10 + mitad - 20) / 2, y, paintFirmaLabel);
        canvas.drawText("Firma / Sello del Agente Fiscalizado", (mitad + 20 + PAGE_WIDTH - MARGIN - 10) / 2, y, paintFirmaLabel);
        y += 20;
    }

    private String valorOGuion(String valor) {
        return (valor == null || valor.trim().isEmpty()) ? "-" : valor;
    }
}