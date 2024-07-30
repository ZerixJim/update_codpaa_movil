<?php
/**
 * Created by DreamW.
 * User: Christian
 * Date: 11/12/14
 * Time: 13:59
 */
ob_start();
session_start();


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');
///*********************************Recibo los datos de los filtros *******************************//


   // $id_formato = $_REQUEST['idFormato'];

    $id_estado = $_REQUEST['idEstado'];

    $desde = $_REQUEST['Desde'];

    $hasta = $_REQUEST['Hasta'];

    $id_marca = $_REQUEST['idMarca'];

    $id_tipoT = $_REQUEST['idTipoTie'];

    $filtro = "";

   /* if ($id_formato != "") {
        $filtro .= " and t.idFormato='" . $id_formato . "'";
    }*/

    if ($id_estado != "" && $id_estado != "all") {
        $filtro .= " and t.idEstado='" . $id_estado . "'";
    }


    if ($id_tipoT != ""){

        $filtro .= " and t.idTipoTienda = " . $id_tipoT . " ";

    }


    if (!empty($id_marca)){

        $n_marc = count($id_marca);


        $filtro.= " and pro.idMarca in (";
        $k = 0;

        foreach ($id_marca as $marcas) {

            if (is_numeric($marcas)){

                $filtro.= $marcas;

                if ($k != $n_marc - 1){

                    $filtro .= ",";
                }
            }

            $k++;
        }
        $filtro .= ")";
    }



    $manager = DataBase::getInstance();

    //******************************************************************************************//

    $dateIn = date("Y-m-d", strtotime($desde));
    $dateOut = date("Y-m-d", strtotime($hasta));


    $sql = "SELECT DISTINCT t.idTienda, pro.idMarca, mar.nombre marca,t.grupo, t.sucursal, t.numeroEconomico, f.grupo AS formato,f.cadena AS cadenaf,e.nombre AS estadoMex, 
      inv.idProducto, CONCAT(pro.nombre,' ', pro.presentacion) producto, CONCAT('SKU ', pro.codigoBarras) sku, inv.fecha_captura, inv.`cantidadFisico`, inv.`cantidadSistema`, inv.fecha_caducidad,
      inv.lote, inv.tipo, p.nombre, p.idCelular as idPromotor
    

    FROM maestroTiendas t 
	INNER JOIN inventarioBodega inv ON (t.idTienda=inv.idTienda) 
	LEFT JOIN tiendas_formatos f ON (t.idFormato=f.idFormato) 
	LEFT JOIN estados e ON (e.id=t.idEstado) 
	LEFT JOIN Promotores p ON (inv.idPromotor=p.idCelular) 
	LEFT JOIN Producto pro ON (pro.idProducto= inv.idProducto) 
    left join Marca mar on (mar.idMarca = pro.idMarca)

	RIGHT JOIN ( SELECT MAX(fecha_captura) fecha, idProducto , idTienda FROM inventarioBodega
		GROUP BY idProducto, idTienda
	
	) AS invmax ON (invmax.fecha =inv.fecha_captura AND invmax.idProducto=inv.`idProducto` AND invmax.idTienda=inv.idTienda) 
	
	WHERE (inv.fecha_captura BETWEEN '". $dateIn ."' AND '" .  $dateOut ."') 
	
	
	" .$filtro . "
	
	
	

	ORDER BY pro.`idMarca`, inv.`idProducto`, inv.`fecha_captura`";

    $datos_tienda = $manager->ejecutarConsulta($sql);


    $json = array();

    ///// **********************************Ciclo Tiendas
    while ($row = mysqli_fetch_object($datos_tienda)) {

    //******************************************** Final While Inventarios Tienda-Producto*******************************************************//
        array_push($json, $row);



    }/////Final While Tiendas



    echo json_encode($json, JSON_PRETTY_PRINT | JSON_NUMERIC_CHECK);



} else {

    echo 'no has iniciado sesion';

    http_response_code(422);

    header('refresh:2,../index.php');

}



