package com.sistemaReclutador.sistemaReclutador;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite de Regresión Completa - Sistema Reclutador")
@SelectPackages({
    "com.sistemaReclutador.sistemaReclutador"
})
public class RegresionCompletaTestSuite {
    // Esta clase se deja vacía. Solo sirve como lanzador de todo el proyecto.
}