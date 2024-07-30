<?php

ob_start();
session_start();

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {
    
    include_once('../../connexion/DataBase.php');
    
    $fechaInicio = $_REQUEST['fechaInicio'];
    $fechaFin = $_REQUEST['fechaFin'];
    
    $manager = DataBase::getInstance();
    
    $dateIn = date("Y-m-d", strtotime($fechaInicio));
    $dateOut = date("Y-m-d", strtotime($fechaFin));
    
    //INICIO CONSULTA
    $sql = "
    SELECT 
    inicial.idPromotor as 'CARTERA',
    p.nombre as 'PROMOTOR',
    inicial.idTienda as 'ID TIENDA',
    ma.sucursal as 'TIENDA',
    tf.grupo as 'CADENA',
    tf.cadena as 'FORMATO',
    inicial.idProducto as 'ID PRODUCTO',
    pr.nombre as 'PRODUCTO',
    DATE(inicial.fechaServidor) as 'FECHA', 
    inicial.cantidadInicial as 'INVENTARIO INICIAL',
    final.cantidadFinal as 'INVENTARIO FINAL',
    frentes.cantidad as 'FRENTES' 
    FROM
    (
    SELECT
    idPromotor,
    idTienda,
    idProducto,
    fechaServidor,
    cantidadFisico AS cantidadInicial,
    CONCAT(idPromotor,-idProducto,-idTienda,'-',date(fecha_captura)) AS Llave
    FROM inventarioBodega 
    WHERE idProducto IN
    (
    	SELECT idProducto FROM Producto WHERE idMarca IN (342, 345, 346)
    )
    AND DATE(fecha_captura) BETWEEN '". $dateIn ."' AND '" .  $dateOut ."'
    GROUP BY idPromotor, idTienda, idProducto, fecha_captura
    )inicial
    LEFT JOIN
    (
    SELECT
      idPromotor,
      idTienda,
      idProducto,
      fechaServidor,
      cantidadFisico AS cantidadFinal,
    CONCAT(idPromotor,-idProducto,-idTienda,'-',date(fecha_captura)) AS Llave
    FROM inventarioBodega
    WHERE idProducto IN (4616, 4617, 4618, 4619)
      AND DATE(fecha_captura) BETWEEN '". $dateIn ."' AND '" .  $dateOut ."'
      AND (idPromotor, idTienda, idProducto, fechaServidor) NOT IN (
        SELECT
          idPromotor,
          idTienda,
          idProducto,
          MIN(fechaServidor) AS fechaServidor
        FROM inventarioBodega
        WHERE idProducto IN (
            SELECT idProducto FROM Producto WHERE idMarca IN (342, 345, 346)
        )
          AND DATE(fecha_captura) BETWEEN '". $dateIn ."' AND '" .  $dateOut ."'
        GROUP BY idPromotor, idTienda, idProducto, fecha_captura
      )
    ORDER BY idPromotor ASC, idTienda ASC, idProducto ASC, fechaServidor ASC
    )final
    ON inicial.Llave = final.Llave
    LEFT JOIN
    (
    	SELECT *,
        CONCAT(fc.idCelular, '-', fc.idProducto, '-', fc.idTienda, '-', DATE(fc.fecha_captura)) AS Llave
        FROM frentesCharola fc
        WHERE fc.idProducto IN (
            SELECT idProducto FROM Producto WHERE idMarca IN (342, 345, 346)
        )
        AND DATE(fc.fecha_captura) BETWEEN '". $dateIn ."' AND '" .  $dateOut ."'
        GROUP BY fc.idCelular, fc.idTienda, fc.idProducto, fc.fecha_captura
    ) frentes
    ON frentes.Llave = inicial.Llave
    LEFT JOIN Promotores p
    ON inicial.idPromotor = p.idCelular
    LEFT JOIN maestroTiendas ma
    ON p.idEstado = ma.idEstado
    LEFT JOIN tiendas_formatos tf
    ON ma.idFormato = tf.idFormato
    LEFT JOIN Producto pr
    ON inicial.idProducto = pr.idProducto
    GROUP BY inicial.Llave
    ORDER BY DATE(inicial.fechaServidor), inicial.idPromotor, inicial.idTienda";
    //FIN CONSULTA
    
    $datos_tienda = $manager->ejecutarConsulta($sql);
    
    $json = array();
    
    while ($row = mysqli_fetch_object($datos_tienda)) {
        array_push($json, $row);
    }
    
    echo json_encode($json, JSON_PRETTY_PRINT | JSON_NUMERIC_CHECK);
    
}else{
    echo 'no has iniciado sesion';
    http_response_code(422);
    header('refresh:2,../index.php');
}