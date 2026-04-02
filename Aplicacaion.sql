SELECT o.nombreOferta, e.nombre, a.Fecha, a.estadoaplicaciones FROM aplicacion a 
JOIN oferta o ON a.id_oferta =  o.id_oferta 
JOIN empresas e ON e.id_empresa = o.id_empresa
WHERE a.id_perfil = 22
ORDER BY a.idaplicacion DESC
