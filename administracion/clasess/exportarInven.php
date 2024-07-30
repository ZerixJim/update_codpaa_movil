<?php
/**
 * Created by DreamW.
 * User: Christian
 * Date: 11/12/14
 * Time: 13:59
 */
ob_start();

session_start();

/** Include path **/
//ini_set('include_path', ini_get('include_path').';../PHPExcel/Classes/');
set_include_path(get_include_path() . PATH_SEPARATOR . './PHPExcel/Classes/');

/** PHPExcel */
include 'PHPExcel.php';

/** PHPExcel_Writer_Excel2007 */
include 'PHPExcel/Writer/Excel2007.php';

// Create new PHPExcel object
$objPHPExcel = new PHPExcel();


include_once('../connexion/bdManager.php');


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {


///*********************************Recibo los datos de los filtros *******************************//

    $id_formato = $_REQUEST['idFormato'];

    $id_estado = $_REQUEST['idEstado'];

    $desde = $_REQUEST['Desde'];

    $hasta = $_REQUEST['Hasta'];

    $id_marca = $_REQUEST['idMarca'];

    $id_tipoT = $_REQUEST['idTipoTie'];


    $fecha_ini = explode('-', $_REQUEST['Desde']);


    $filtro = "";
    if ($id_formato != "") {
        $filtro .= " and t.idFormato='" . $id_formato . "'";
    }

    if ($id_estado != "" && $id_estado != "all") {
        $filtro .= " and t.idEstado";
    }

    $manager = new bdManager();

    $mes = $manager->mesNombre($fecha_ini[1]);

    $query_marca = "SELECT nombre FROM Marca WHERE idMarca='" . $id_marca . "'";
    $resul_marca = $manager->ejecutarConsulta($query_marca);
    $datos_marca = mysqli_fetch_array($resul_marca);

    $titulo = $mes . ' ' . $fecha_ini[2];


// Set properties
    $objPHPExcel->getProperties()->setCreator("Codpaa Web");
    $objPHPExcel->getProperties()->setLastModifiedBy("Codpaa Web");
    $objPHPExcel->getProperties()->setTitle("Inventarios " . $titulo);
    $objPHPExcel->getProperties()->setSubject("Inventarios " . $datos_marca['nombre']);
    $objPHPExcel->getProperties()->setDescription("Archivo de Inventarios creado por Codpaa Web");


    //******************************************************************************************//
    $objPHPExcel->setActiveSheetIndex(0);

//**********Estilo de la primer fila de titulos
    $styleArray = array(
        'font' => array(
            'bold' => true,
            'size' => 15
        ),
        'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => 'FF8000')
        )
    );

    $sql_tienda = "SELECT t.*,p.idMarca,
	f.grupo AS formato,
	f.cadena AS cadenaf,
	e.nombre AS estadoMex
	FROM
		maestroTiendas t
	LEFT JOIN inventarioBodega inv ON (t.idTienda = inv.idTienda)
	LEFT JOIN tiendas_formatos f ON (t.idFormato = f.idFormato)
	LEFT JOIN estados e ON (e.id = t.idEstado)
	LEFT JOIN Producto p ON (p.idProducto=inv.idProducto)
	WHERE p.idMarca='" . $id_marca . "' AND t.idTipoTienda='" . $id_tipoT . "' " . $filtro . "
	GROUP BY t.idTienda,p.idMarca";


    $rs_tienda = $manager->ejecutarConsulta($sql_tienda);
    // Add some data

    $objPHPExcel->getActiveSheet()->SetCellValue('A3', 'ID');
    $objPHPExcel->getActiveSheet()->getStyle('A3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('B3', 'CADENA');
    $objPHPExcel->getActiveSheet()->getStyle('B3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('C3', 'TIENDA');
    $objPHPExcel->getActiveSheet()->getStyle('C3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('D3', 'CIUDAD');
    $objPHPExcel->getActiveSheet()->getStyle('D3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('E3', 'FORMATO');
    $objPHPExcel->getActiveSheet()->getStyle('E3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('F3', 'ESTADO');
    $objPHPExcel->getActiveSheet()->getStyle('F3')->applyFromArray($styleArray);


    $query_prod = "SELECT inv.idProducto,concat(p.nombre,' ',p.presentacion) AS nombre FROM inventarioBodega inv 
		INNER JOIN Producto p ON (inv.idProducto=p.idProducto) 
		INNER JOIN maestroTiendas t ON (inv.idTienda=t.idTienda)
		WHERE p.idMarca='" . $id_marca . "' AND t.idTipoTienda='" . $id_tipoT . "' '" . $filtro . "' 
		GROUP BY inv.idProducto ORDER BY inv.idProducto";

    $rs_prod = $manager->ejecutarConsulta($query_prod);

    $letra_head = 'G';

    while ($datos_prod = mysqli_fetch_array($rs_prod)) {

        $objPHPExcel->getActiveSheet()->SetCellValue($letra_head . '3', strtoupper($datos_prod['nombre']) . " Fisico ");
        $objPHPExcel->getActiveSheet()->getStyle($letra_head . '3')->applyFromArray($styleArray);

        $letra_head++;

        $objPHPExcel->getActiveSheet()->SetCellValue($letra_head . '3', strtoupper($datos_prod['nombre']) . " Sistema");
        $objPHPExcel->getActiveSheet()->getStyle($letra_head . '3')->applyFromArray($styleArray);

        $letra_head++;

    }

    $tiendas_n = 4;
    ///// **********************************Ciclo Tiendas
    while ($dato_tienda = mysqli_fetch_array($rs_tienda)) {
        $query_prod = "SELECT inv.idProducto,p.nombre FROM inventarioBodega inv 
		INNER JOIN Producto p ON (inv.idProducto=p.idProducto) 
		INNER JOIN maestroTiendas t ON (inv.idTienda=t.idTienda)
		WHERE p.idMarca='" . $id_marca . "' AND t.idTipoTienda='" . $id_tipoT . "' " . $filtro . " 
		GROUP BY inv.idProducto ORDER BY inv.idProducto";


        $rs_prod = $manager->ejecutarConsulta($query_prod);

        $objPHPExcel->getActiveSheet()->SetCellValue('A' . $tiendas_n, $dato_tienda['idTienda']);
        $objPHPExcel->getActiveSheet()->SetCellValue('B' . $tiendas_n, $dato_tienda['cadenaf']);
        $objPHPExcel->getActiveSheet()->SetCellValue('C' . $tiendas_n, $dato_tienda['sucursal']);
        $objPHPExcel->getActiveSheet()->SetCellValue('D' . $tiendas_n, $dato_tienda['municipio']);
        $objPHPExcel->getActiveSheet()->SetCellValue('E' . $tiendas_n, $dato_tienda['formato']);
        $objPHPExcel->getActiveSheet()->SetCellValue('F' . $tiendas_n, $dato_tienda['estadoMex']);

        $letra_tien = "G";

        //*****************************Ciclo Inventarios Por Tienda Por Producto *******************************//
        while ($datos_prod = mysqli_fetch_array($rs_prod)) {

            $sql_invent = "SELECT inv.* FROM inventarioBodega inv 
			WHERE inv.idProducto='" . $datos_prod['idProducto'] . "' AND 
			(STR_TO_DATE(inv.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('" . $desde . "','%d-%m-%Y') 
			AND STR_TO_DATE('" . $hasta . "','%d-%m-%Y') ) 
			AND inv.idTienda='" . $dato_tienda['idTienda'] . "' ORDER BY inv.idInventario DESC";

            $rs_invent = $manager->ejecutarConsulta($sql_invent);

            $datos_invent = mysqli_fetch_array($rs_invent);

            if ($datos_invent['cantidadFisico'] == '') {
                $datos_invent['cantidadFisico'] = 0;
            }
            if ($datos_invent['cantidadSistema'] == '') {
                $datos_invent['cantidadSistema'] = 0;
            }

            $objPHPExcel->getActiveSheet()->SetCellValue($letra_tien . $tiendas_n, $datos_invent['cantidadFisico']);
            $letra_tien++;

            $objPHPExcel->getActiveSheet()->SetCellValue($letra_tien. $tiendas_n, $datos_invent['cantidadSistema']);

            $letra_tien++;



            usleep(4000);
        }
        //******************************************** Final While Inventarios Tienda-Producto***************************************//


        $tiendas_n++;

    }/////Final While Tiendas

    //***********Si se tiene el logo de la marca ******//
    if (file_exists('../imagenes/logos/' . $datos_marca['nombre'] . '.png')) {
        //  " Add a drawing to the worksheet\n";
        $objDrawing = new PHPExcel_Worksheet_Drawing();
        $objDrawing->setName('Logo Image');
        $objDrawing->setDescription('Logo');
        $logo = '../imagenes/logos/' . $datos_marca['nombre'] . '.png';
        $objDrawing->setPath($logo);
        $objDrawing->setCoordinates('A1');
        $objDrawing->setHeight(150);
        $objDrawing->setWorksheet($objPHPExcel->getActiveSheet());

    }
// Rename sheet
    $objPHPExcel->getActiveSheet()->setTitle('Inventarios');


// Save Excel 2007 file
    $objWriter = new PHPExcel_Writer_Excel2007($objPHPExcel);

    $path_file = 'Inventarios ' . $datos_marca['nombre'] . ' ' . $mes . '.xlsx';
//str_replace('.php', '.xlsx', __FILE__);
    echo $objWriter->save('../archivos/' . $path_file);

    echo $path_file;

} else {

    echo '0';

}


