<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 15/05/2018
 * Time: 11:10 AM
 */

ob_start();
session_start();




if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');
///*********************************Recibo los datos de los filtros *******************************//


    $id_formato = $_REQUEST['idFormato'];

    $id_estado = $_REQUEST['idEstado'];

    $desde = $_REQUEST['Desde'];

    $hasta = $_REQUEST['Hasta'];

    $id_marca = $_REQUEST['idMarca'];

    $id_tipoT = $_REQUEST['idTipoTie'];

    $filtro = "";
    if ($id_formato != "") {
        $filtro .= " and t.idFormato='" . $id_formato . "'";
    }

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


    $sql = "SELECT DISTINCT im.idPromotor , t.idTienda, en.descripcion nielsen, im.idProducto, im.precioNormal, im.precioOferta, im.precio_caja ,im.fecha_captura, pro.idMarca, mar.nombre marca,t.grupo, t.sucursal, t.numeroEconomico, f.grupo AS formato,f.cadena AS cadenaf,e.nombre AS estadoMex, 
      CONCAT(pro.nombre,' ') producto, pro.presentacion, cat.categoria, CONCAT(im.`inicio_oferta`,' ', im.`fin_oferta`) oferta 


    FROM maestroTiendas t 
	INNER JOIN inteligenciaMercado im ON (t.idTienda=im.idTienda) 
	LEFT JOIN tiendas_formatos f ON (t.idFormato=f.idFormato) 
	LEFT JOIN estados e ON (e.id=t.idEstado) 
	LEFT JOIN Promotores p ON (im.idPromotor=p.idCelular) 
	LEFT JOIN Producto pro ON (pro.idProducto= im.idProducto) 
    left join Marca mar on (mar.idMarca = pro.idMarca)
    LEFT JOIN categorias_productos cat ON (cat.id_categoria = im.id_categoria)

	RIGHT JOIN ( SELECT idTienda, idProducto,  MAX(fecha_captura) fecha FROM inteligenciaMercado  
	
		GROUP BY idTienda, idProducto
	) AS inteli1 ON (inteli1.idTienda=im.`idTienda` AND inteli1.idProducto = im.`idProducto` AND inteli1.fecha = im.fecha_captura)

    left join estados_nielsen en on e.id_nielsen = en.id_nielsen 

	
	
	  WHERE fecha_captura BETWEEN '". $dateIn ."' AND '" .  $dateOut ."' 
	
	
	" .$filtro . " 
	

	ORDER BY pro.`idMarca`, im.`idProducto`, im.`fecha_captura`";


    /************    end sql   ****************/


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



}