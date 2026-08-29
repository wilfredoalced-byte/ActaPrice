package com.example.actaprice;

import java.util.LinkedHashMap;

public class DatosActa {
    public LinkedHashMap<String, String> establecimiento = new LinkedHashMap<>();
    public LinkedHashMap<String, String> fiscalizador = new LinkedHashMap<>();

    // Cada fila: {producto, PRICE, publicado, surtidor, descuento}
    public String[][] preciosLiquidos;
    public String[][] glpAutomotor;

    // Cada fila: {presentación, precio}
    public String[][] glpCilindros;
    public String marcaGlp;

    public LinkedHashMap<String, String> verificaciones = new LinkedHashMap<>();
    public String[] hechosVerificados = new String[6];
    public LinkedHashMap<String, String> otros = new LinkedHashMap<>();
    public LinkedHashMap<String, String> receptor = new LinkedHashMap<>();
}