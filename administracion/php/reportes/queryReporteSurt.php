<?php
/**
 * Created by DreamW.
 * User: Christian
 * Date: 11/12/14
 * Time: 13:59
 */
ob_start();

session_start();

include_once('../../connexion/bdManager.php');
include_once('../../php/seguridad.php');

if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {


///*********************************Recibo los datos de los filtros *******************************//
    $manager = new bdManager();

    $id_estado = $_REQUEST['idEstado'];

    $desde = $_REQUEST['Desde'];

    $hasta = $_REQUEST['Hasta'];

    $id_marca = $_REQUEST['idMarca'];

    $id_prod = $_REQUEST['idProd'];

    $idUsuario = $_SESSION['idUser'];

    $idTipoTien = $_SESSION['idTipoTien'];

    $filtro = "";


    if ($id_estado != "" && $id_estado != "all") {
        $filtro .= " and t.idEstado='" . $id_estado . "'";
    }

    if ($idTipoTien != "") {
        $filtro .= " and t.idTipoTienda='" . $idTipoTien . "'";
    }

    $filtro2 = "";

    $n_prod = count($id_prod);
    $k = 0;
    if ($n_prod > 0) {
        foreach ($id_prod as $product) {
            if ($k == 0) {
                $filtro2 .= " and (p.idProducto='" . $product . "'";
            } else {
                $filtro2 .= " or p.idProducto='" . $product . "'";
            }
            $k++;
        }
        $filtro2 .= ")";
    }

    //******************************************************************************************//
    /*$query_marca="select nombre from Marca where idMarca='".$id_marca."'";
    $resul_marca=$manager->ejecutarConsulta($query_marca);
    $datos_marca=mysqli_fetch_array($resul_marca);

    //***********Si se tiene el logo de la marca *********
    if(file_exists('../../imagenes/logos/'.$datos_marca['nombre'].'.png'))
    {
        echo '<img src="../../imagenes/logos/'.$datos_marca['nombre'].'.png" height="200px" />';
    }*/
    $sql_tienda = "select t.*,p.idMarca, mar.nombre marca,
	f.grupo AS formato,
	f.cadena AS cadenaf,
	e.nombre AS estadoMex,
	lo.nombre AS nom_mun,
	surt.idCelular AS idcelular,
	pro.nombre as nombre
	FROM
		maestroTiendas t
	LEFT JOIN surtidoMueble surt ON (t.idTienda = surt.idTienda)
	LEFT JOIN tiendas_formatos f ON (t.idFormato = f.idFormato)
	LEFT JOIN Promotores pro ON (pro.idCelular = surt.idCelular)
	LEFT JOIN estados e ON (e.id = t.idEstado)
	LEFT JOIN Producto p ON (p.idProducto=surt.idProducto)
		    
	left join Marca mar on (mar.idMarca = p.idMarca)
		    
	LEFT JOIN localidades lo ON (lo.id=t.id_localidad)
	WHERE p.idMarca='" . $id_marca . "'  " . $filtro . "
	GROUP BY t.idTienda,p.idMarca";


    $rs_tienda = $manager->ejecutarConsulta($sql_tienda);

    echo '<table width="100%" border="1" bordercolor="#D8D8D8">
		<tr bgcolor="#46E82F">
		<td><span><strong>ID</strong></span></td>
		<td><span><strong>CADENA</strong></span></td>
		<td><span><strong>TIENDA</strong></span></td>
		<td><span><strong>IDCARTERA</strong></span></td>
		<td><span><strong>NOMBRE</strong></span></td>
		<td><span><strong>CIUDAD</strong></span></td>
		<td><span><strong>FORMATO</strong></span></td>
		<td><span><strong>ESTADO</strong></span></td>';


    $query_prod = "select surt.idProducto,concat(p.nombre,' ',p.presentacion) as nombre from surtidoMueble surt 
		inner join Producto p on (surt.idProducto=p.idProducto) 
		inner join maestroTiendas t on (surt.idTienda=t.idTienda)
		where p.idMarca='" . $id_marca . "'  " . $filtro2 . " 
		group by surt.idProducto order by surt.idProducto";

    $rs_prod = $manager->ejecutarConsulta($query_prod);
    while ($datos_prod = mysqli_fetch_array($rs_prod)) {
        echo '<td align="center"><span>' . strtoupper($datos_prod['nombre']) . '</span>
			</td>';

    }
    echo '</tr>';

    ///// **********************************Ciclo Tiendas
    while ($dato_tienda = mysqli_fetch_array($rs_tienda)) {
        $query_prod = "select surt.idProducto,p.nombre from surtidoMueble surt 
		inner join Producto p on (surt.idProducto=p.idProducto) 
		inner join maestroTiendas t on (surt.idTienda=t.idTienda)
		where p.idMarca='" . $id_marca . "' " . $filtro2 . " 
		group by surt.idProducto order by surt.idProducto";


        $i = 0;

        $rs_prod = $manager->ejecutarConsulta($query_prod);


        echo '
		<tr>
		
<td><span>' . $dato_tienda['idTienda'] . '</span></td>
		<td><span>' . $dato_tienda['cadenaf'] . '</span></td>
		<td><span>' . $dato_tienda['sucursal'] . '</span></td>
			<td><span>' . $dato_tienda['idcelular'] . '</span></td>
			<td><span>' . $dato_tienda['nombre'] . '</span></td>
		
		<td><span>' . utf8_encode($dato_tienda['nom_mun']) . '</span></td>
		<td><span>' . $dato_tienda['formato'] . '</span></td>
		<td><span>' . utf8_encode($dato_tienda['estadoMex']) . '</span></td>
		';
        $total_prom_visit = 0;
        $total_prom_fil = 0;
        //*****************************Ciclo Surtido de Mueble Por Tienda Por Producto *******************************//
        while ($datos_prod = mysqli_fetch_array($rs_prod)) {

            $sql_surt = "select sum(cajas) as total_cajas from surtidoMueble 
			where idProducto='" . $datos_prod['idProducto'] . "' and 
			(STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('" . $desde . "','%d-%m-%Y') and STR_TO_DATE('" . $hasta . "','%d-%m-%Y') ) 
			and idTienda='" . $dato_tienda['idTienda'] . "' and surtido='Si' group by idProducto order by idsurtidoMueble DESC";

            $rs_surt = $manager->ejecutarConsulta($sql_surt);

            $datos_surt = mysqli_fetch_array($rs_surt);

            if ($datos_surt['total_cajas'] != 0) {
                echo '<td align="center"><span>' . $datos_surt['total_cajas'] . '
			</span>
			</td>';
            } else {
                echo '<td align="center"><span>0</span>
			</td>';
            }

            usleep(4000);
        }
        echo '</tr>';
//******************************************** Final While Surtido Mueble Tienda-Producto*******************************************************//

    }/////Final While Tiendas

    echo '</table>';// Cierra Tabla Principal

} else {

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



