<?
ob_start();
session_start();


if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){


    include_once('../../connexion/ConexionPDO.php');

    $idCel = $_GET['id'];
    $fecha = $_GET['fecha'];


    $pdo = ConexionPDO::getInstance()->getDB();

    $fecha = date("Y-m-d", strtotime($fecha));


    $command = "SELECT DISTINCT tv.idTienda, mT.sucursal, mT.x, mT.y, tv.idCelular,tv.fecha, 
            tv.hora, tv.latitud, tv.longitud, tv.tiempollegadaregistro, tv.tipo,f.grupo, 
            CONCAT(COUNT(DISTINCT(tv.idTienda), DATE(tv.tiempollegadaregistro)),'/',(rp.lunes+rp.martes+rp.miercoles+rp.jueves+rp.viernes+rp.sabado+rp.domingo)) visitas,
            auto_time    
                
            
            FROM tiendasVisitadas tv 
            LEFT JOIN maestroTiendas AS mT ON mT.idTienda=tv.idTienda 
            LEFT JOIN tiendas_formatos f ON (f.idFormato=mT.idFormato)
            
            LEFT JOIN rutasPromotores rp ON (rp.idTienda = tv.idTienda AND rp.idPromotor = tv.idCelular)
                
            
            WHERE tv.idCelular= :idPromo  AND DATE(tv.fecha_captura) = :fecha
             GROUP BY tv.idTienda, tv.tipo 
             ORDER BY tv.hora 
	 ";

    $sentense = $pdo->prepare($command);
    $sentense->bindParam(':idPromo', $idCel, PDO::PARAM_INT);
    $sentense->bindParam(':fecha', $fecha, PDO::PARAM_STR);


    $sentense->execute();

    $result = $sentense->fetchAll(PDO::FETCH_ASSOC);


    echo json_encode($result,JSON_PRETTY_PRINT);

}else{
    echo 'no has iniciado sesion';
}


