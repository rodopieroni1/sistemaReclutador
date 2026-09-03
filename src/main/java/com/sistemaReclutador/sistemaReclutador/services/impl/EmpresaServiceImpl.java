package com.sistemaReclutador.sistemaReclutador.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sistemaReclutador.sistemaReclutador.dto.EmpresaRequest;
import com.sistemaReclutador.sistemaReclutador.entities.Empresa;
import com.sistemaReclutador.sistemaReclutador.entities.Rubro;
import com.sistemaReclutador.sistemaReclutador.repositories.EmpresaRepository;
import com.sistemaReclutador.sistemaReclutador.services.EmpresaService;
import com.sistemaReclutador.sistemaReclutador.services.RubroService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private static final List<String> EXTENSIONES_PERMITIDAS = List.of("image/png", "image/jpeg", "image/webp");

    private final EmpresaRepository empresaRepository;
    private final RubroService rubroService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Transactional
    @Override
    public Empresa saveEmpresa(EmpresaRequest empresaRequest) {
        validarEmpresaRequest(empresaRequest, null);
        Rubro rubro = obtenerRubroOThrow(empresaRequest.getIdRubro());
        Empresa empresa = convertirDtoAEntidad(empresaRequest, rubro);
        procesarLogoSiExiste(empresaRequest.getLogo(), empresa::setLogo);
        return empresaRepository.save(empresa);
    }

    @Transactional
    @Override
    public Empresa updateEmpresa(Long id, EmpresaRequest empresaDetails) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La empresa con ID " + id + " no existe."));

        validarEmpresaRequest(empresaDetails, empresa);

        Rubro rubro = obtenerRubroOThrow(empresaDetails.getIdRubro());

        actualizarCamposEmpresa(empresa, empresaDetails, rubro);
        procesarLogoSiExiste(empresaDetails.getLogo(), empresa::setLogo);

        return empresaRepository.save(empresa);
    }

    @Transactional
    @Override
    public void deleteEmpresa(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la empresa con el ID: " + id));
        empresaRepository.delete(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Empresa> buscarPorEmpresa() {
        return empresaRepository.findAllDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCuit(Long cuit) {
        return empresaRepository.existsByCuit(cuit);
    }

    @Override
    @Transactional(readOnly = true)
    public Empresa findEmpresa(Long id) {
        return empresaRepository.findById(id).orElse(null);
    }

    // ==========================================
    // MÉTODOS PRIVADOS AUXILIARES Y VALIDACIONES
    // ==========================================

    /**
     * Valida reglas de negocio del DTO. 
     * Si empresaExistente es null, asume que es una creación (save).
     */
    private void validarEmpresaRequest(EmpresaRequest request, Empresa empresaExistente) {
        // --- NOMBRE ---
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la empresa es obligatorio");
        }
        if (request.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede superar los 100 caracteres");
        }

        // --- CUIT ---
        if (request.getCuit() == null || String.valueOf(request.getCuit()).length() != 11) {
            throw new IllegalArgumentException("El CUIT debe tener exactamente 11 dígitos");
        }
        boolean cuitCambio = empresaExistente == null || !empresaExistente.getCuit().equals(request.getCuit());
        if (cuitCambio && empresaRepository.existsByCuit(request.getCuit())) {
            throw new IllegalArgumentException(empresaExistente == null ? "El Cuit ya está registrado" : "El nuevo CUIT ya se encuentra en uso.");
        }

        // --- EMAIL ---
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio");
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El formato del correo electrónico no es válido");
        }
        if (request.getEmail().length() > 100) {
            throw new IllegalArgumentException("El correo electrónico no puede superar los 100 caracteres");
        }
        boolean emailCambio = empresaExistente == null || !empresaExistente.getEmail().equalsIgnoreCase(request.getEmail());
        if (emailCambio && empresaRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(empresaExistente == null ? "El email ya está registrado" : "El nuevo email ya se encuentra en uso.");
        }

        // --- LOGO ---
        if (request.getLogo() != null && !request.getLogo().isEmpty()) {
            String nombreOriginal = request.getLogo().getOriginalFilename();
            if (nombreOriginal != null && nombreOriginal.length() > 245) {
                throw new IllegalArgumentException("El nombre del archivo del logo es demasiado largo (máximo 245 caracteres)");
            }
        }
    }

    private Rubro obtenerRubroOThrow(Integer idRubro) {
        Rubro rubro = rubroService.findRubro(idRubro);
        if (rubro == null) {
            throw new IllegalArgumentException("El rubro especificado no existe.");
        }
        return rubro;
    }

    private Empresa convertirDtoAEntidad(EmpresaRequest dto, Rubro rubro) {
        Empresa empresa = new Empresa();
        empresa.setNombre(dto.getNombre());
        empresa.setDireccion(dto.getDireccion());
        empresa.setHistoriaEmpresa(dto.getHistoriaEmpresa());
        empresa.setObservaciones(dto.getObservaciones());
        empresa.setTelefono(dto.getTelefono());
        empresa.setCuit(dto.getCuit());
        empresa.setEmail(dto.getEmail());
        empresa.setRubro(rubro);
        return empresa;
    }

    private void actualizarCamposEmpresa(Empresa empresa, EmpresaRequest dto, Rubro rubro) {
        empresa.setCuit(dto.getCuit());
        empresa.setNombre(dto.getNombre());
        empresa.setEmail(dto.getEmail());
        empresa.setTelefono(dto.getTelefono());
        empresa.setDireccion(dto.getDireccion());
        empresa.setHistoriaEmpresa(dto.getHistoriaEmpresa());
        empresa.setObservaciones(dto.getObservaciones());
        empresa.setRubro(rubro);
    }

    private void procesarLogoSiExiste(MultipartFile logo, java.util.function.Consumer<String> setterLogo) {
        if (logo != null && !logo.isEmpty()) {
            String nombreGuardado = guardarArchivoLogo(logo);
            setterLogo.accept(nombreGuardado);
        }
    }

    private String guardarArchivoLogo(MultipartFile logo) {
        String contentType = logo.getContentType();
        if (contentType == null || !EXTENSIONES_PERMITIDAS.contains(contentType)) {
            throw new IllegalArgumentException("Formato de imagen no permitido. Solo se aceptan PNG, JPEG y WEBP.");
        }

        try {
            Path baseRuta = Paths.get(uploadDir, "logos").normalize();
            Files.createDirectories(baseRuta);

            String originalFilename = Objects.requireNonNullElse(logo.getOriginalFilename(), "file");
            String extension = "";
            int i = originalFilename.lastIndexOf('.');
            if (i > 0) {
                extension = originalFilename.substring(i);
            }

            String nombreUnico = UUID.randomUUID().toString() + extension;
            Path archivoDestino = baseRuta.resolve(nombreUnico).normalize();

            if (!archivoDestino.startsWith(baseRuta)) {
                throw new SecurityException("Intento de acceso fuera del directorio de subida.");
            }

            Files.copy(logo.getInputStream(), archivoDestino, StandardCopyOption.REPLACE_EXISTING);
            return nombreUnico;

        } catch (IOException e) {
            log.error("Error de I/O al guardar el logo de empresa: {}", e.getMessage(), e);
            throw new RuntimeException("Error al procesar el almacenamiento del logo.", e);
        }
    }
}