package com.sistemaReclutador.sistemaReclutador.strategies.impl;

import java.util.List;
import org.springframework.stereotype.Component;
import com.sistemaReclutador.sistemaReclutador.entities.Oferta;
import com.sistemaReclutador.sistemaReclutador.repositories.OfertaRepository;
import com.sistemaReclutador.sistemaReclutador.strategies.OfertasStrategy;

public class OfertaStrategiesImpl {

    @Component
    public static class OfertaStrategyImpl implements OfertasStrategy {
        @Override
        public boolean aplica(String nombre, String empresa, String rubro) {
            return nombre == null && empresa == null && rubro == null;
        }

        @Override
        public List<Oferta> buscar(OfertaRepository repository, String nombre, String empresa, String rubro) {
            return repository.findAll();
        }
    }

    @Component
    public static class BusquedaTodosCamposStrategy implements OfertasStrategy {
        @Override
        public boolean aplica(String nombre, String empresa, String rubro) {
            return nombre != null && empresa != null && rubro != null;
        }

        @Override
        public List<Oferta> buscar(OfertaRepository repository, String nombre, String empresa, String rubro) {
            return repository.buscarPorCampos(nombre, empresa, rubro);
        }
    }

    @Component
    public static class BusquedaNombreYRubroStrategy implements OfertasStrategy {
        @Override
        public boolean aplica(String nombre, String empresa, String rubro) {
            return nombre != null && empresa == null && rubro != null;
        }

        @Override
        public List<Oferta> buscar(OfertaRepository repository, String nombre, String empresa, String rubro) {
            return repository.buscarPorNombreYRubro(nombre, rubro);
        }
    }

    @Component
    public static class BusquedaEmpresaYRubroStrategy implements OfertasStrategy {
        @Override
        public boolean aplica(String nombre, String empresa, String rubro) {
            return nombre == null && empresa != null && rubro != null;
        }

        @Override
        public List<Oferta> buscar(OfertaRepository repository, String nombre, String empresa, String rubro) {
            return repository.buscarPorDescripcionYRubro(empresa, rubro);
        }
    }

    @Component
    public static class BusquedaPorRubroStrategy implements OfertasStrategy {
        @Override
        public boolean aplica(String nombre, String empresa, String rubro) {
            return nombre == null && empresa == null && rubro != null;
        }

        @Override
        public List<Oferta> buscar(OfertaRepository repository, String nombre, String empresa, String rubro) {
            return repository.buscarPorRubro(rubro);
        }
    }

    @Component
    public static class BusquedaPorNombreStrategy implements OfertasStrategy {
        @Override
        public boolean aplica(String nombre, String empresa, String rubro) {
            return nombre != null && empresa == null && rubro == null;
        }

        @Override
        public List<Oferta> buscar(OfertaRepository repository, String nombre, String empresa, String rubro) {
            return repository.buscarPorNombreOferta(nombre);
        }
    }

    @Component
    public static class BusquedaPorEmpresaStrategy implements OfertasStrategy {
        @Override
        public boolean aplica(String nombre, String empresa, String rubro) {
            return nombre == null && empresa != null && rubro == null;
        }

        @Override
        public List<Oferta> buscar(OfertaRepository repository, String nombre, String empresa, String rubro) {
            return repository.buscarPorDescripcionEmpresa(empresa);
        }
    }
}