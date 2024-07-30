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
        $filtro .= " and mT.idFormato='" . $id_formato . "'";
    }

    if ($id_estado != "" && $id_estado != "all") {
        $filtro .= " and mT.idEstado='" . $id_estado . "'";
    }
    if ($id_tipoT != "") {
        $filtro .= " and mT.idTipoTienda='" . $id_tipoT . "'";
    }
    $manager = new bdManager();

    $mes = $manager->mesNombre($fecha_ini[1]);

    /*$query_marca="select nombre from Marca where idMarca='".$id_marca."'";
    $resul_marca=$manager->ejecutarConsulta($query_marca);
    $datos_marca=mysqli_fetch_array($resul_marca);*/

    $titulo = $mes . ' ' . $fecha_ini[2];


// Set properties
    $objPHPExcel->getProperties()->setCreator("Codpaa Web");
    $objPHPExcel->getProperties()->setLastModifiedBy("Codpaa Web");
    $objPHPExcel->getProperties()->setTitle("Tiendas Visitadas " . $titulo);
    $objPHPExcel->getProperties()->setSubject("Tiendas Visitadas");
    $objPHPExcel->getProperties()->setDescription("Archivo de Tiendas Visitadas creado por Codpaa Web");


    //******************************************************************************************//
    $objPHPExcel->setActiveSheetIndex(0);

//**********Estilo de la primer fila de titulos
    $styleArray = array(
        'font' => array(
            'bold' => true,
            'size' => 13
        ),
        'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => 'FF8000')
        )
    );
    $styleArray2 = array(
        'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => '58FA58')
        )
    );

    $styleArray3 = array(
        'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => '01DF01')
        )
    );

    $styleArray4 = array(
        'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => 'FF8000')
        )
    );

    $styleArray5 = array(
        'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => 'FF0000')
        )
    );

    $sql_tienda = "SELECT DISTINCT mT.idTienda FROM supervisionRutas r  
		INNER JOIN maestroTiendas mT ON (mT.idTienda=r.idTienda)
		WHERE idPromotor IN (SELECT idCelular FROM Promotores p INNER JOIN marcaAsignadaPromotor mp 
		ON (p.idCelular=mp.idPromotor) 
		WHERE p.status='a' ";

    $n_marc = count($id_marca);
    $k = 0;
    if ($n_marc > 0) {
        foreach ($id_marca as $marcas) {
            if ($k == 0) {
                $sql_tienda .= " and (mp.idMarca='" . $marcas . "' ";
            } else {
                $sql_tienda .= " or mp.idMarca='" . $marcas . "'";
            }
            $k++;
        }
        $sql_tienda .= ")";
    }
    $sql_tienda .= " " . $filtro . "  
		GROUP BY p.idCelular)
		GROUP BY r.idTienda,r.idPromotor
		order by r.idTienda ASC";

    $rs_tienda = $manager->ejecutarConsulta($sql_tienda);
    // Add some data

    $objPHPExcel->getActiveSheet()->SetCellValue('A5', 'ID');
    $objPHPExcel->getActiveSheet()->getStyle('A5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('A6');

    $objPHPExcel->getActiveSheet()->SetCellValue('B5', 'CADENA');
    $objPHPExcel->getActiveSheet()->getStyle('B5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('B6');

    $objPHPExcel->getActiveSheet()->SetCellValue('C5', 'No');
    $objPHPExcel->getActiveSheet()->getStyle('C5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('C6');

    $objPHPExcel->getActiveSheet()->SetCellValue('D5', 'TIENDA');
    $objPHPExcel->getActiveSheet()->getStyle('D5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('D6');

    $objPHPExcel->getActiveSheet()->SetCellValue('E5', 'CIUDAD');
    $objPHPExcel->getActiveSheet()->getStyle('E5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('E6');

    $objPHPExcel->getActiveSheet()->SetCellValue('F5', 'FORMATO');
    $objPHPExcel->getActiveSheet()->getStyle('F5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('F6');

    $objPHPExcel->getActiveSheet()->SetCellValue('G5', 'ESTADO');
    $objPHPExcel->getActiveSheet()->getStyle('G5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('G6');

    $objPHPExcel->getActiveSheet()->SetCellValue('H5', 'ISLAS');
    $objPHPExcel->getActiveSheet()->getStyle('H5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('H6');

    $objPHPExcel->getActiveSheet()->SetCellValue('I5', 'FRENTES');
    $objPHPExcel->getActiveSheet()->getStyle('I5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('I6');

    $objPHPExcel->getActiveSheet()->SetCellValue('J5', 'VISITAS X SEM REQ.');
    $objPHPExcel->getActiveSheet()->getStyle('J5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('J6');

    $objPHPExcel->getActiveSheet()->SetCellValue('K5', 'VISITAS MINIMAS');
    $objPHPExcel->getActiveSheet()->getStyle('K5')->applyFromArray($styleArray);
    $objPHPExcel->getActiveSheet()->freezePane('K6');


    $letra_head = 'L';
    ///***************Recorrer los dias laborados **/////
    $desde2 = $desde;
    while (strtotime($desde2) <= strtotime($hasta)) {

        $fecha = explode('-', $desde2);
        $n_dia = $manager->diaSemana($fecha[0], $fecha[1], $fecha[2]);

        if ($n_dia <= 6 || $n_dia >= 1) {
            $objPHPExcel->getActiveSheet()->SetCellValue($letra_head . '5', strtoupper($desde2));
            $objPHPExcel->getActiveSheet()->getStyle($letra_head . '5')->applyFromArray($styleArray);

            $letra_head++;
        }

        $desde2 = date("d-m-Y", strtotime("$desde2 + 1 DAY"));


    }
    $objPHPExcel->getActiveSheet()->SetCellValue($letra_head . '5', 'FOTOS');
    $objPHPExcel->getActiveSheet()->getStyle($letra_head . '5')->applyFromArray($styleArray);

    $cel_des = $letra_head;
    $j = 0;
    $i = 15;
    while ($j < $i) {
        $objPHPExcel->getActiveSheet()->getStyle($letra_head . '5')->applyFromArray($styleArray4);
        $j++;
        $letra_head++;

    }

    $objPHPExcel->getActiveSheet()->mergeCells($cel_des . '5:' . $letra_head . '5');

    $objPHPExcel->getActiveSheet()->SetCellValue($letra_head . '5', 'CUMPLE');
    $objPHPExcel->getActiveSheet()->getStyle($letra_head . '5')->applyFromArray($styleArray);

    $letra_head++;

    $objPHPExcel->getActiveSheet()->SetCellValue($letra_head . '5', 'TOTAL');
    $objPHPExcel->getActiveSheet()->getStyle($letra_head . '5')->applyFromArray($styleArray);


    $tiendas_n = 6;
    ///// **********************************Ciclo Tiendas
    while ($dato_tienda = mysqli_fetch_array($rs_tienda)) {
        //****Verifica si tiene visitas en el rango de fecha

        $sql_ver = "SELECT count(idTiendasVisitadas) AS total_vis
			FROM tiendasVisitadas
			WHERE idTienda = '" . $dato_tienda['idTienda'] . "'
			AND (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('" . $desde . "','%d-%m-%Y') 
			AND STR_TO_DATE('" . $hasta . "','%d-%m-%Y') ) AND tipo='E'";

        $rs_ver = $manager->ejecutarConsulta($sql_ver);
        $dat_ver = mysqli_fetch_array($rs_ver);

        if ($dat_ver['total_vis'] > 0) {
            $sql_ruta = "SELECT mT.*,
			f.grupo AS formato,
			f.cadena AS cadenaf,
			e.nombre AS estadoMex,
			l.nombre AS ciudadN
			FROM maestroTiendas mT 
			LEFT JOIN tiendas_formatos f ON (mT.idFormato = f.idFormato)
			LEFT JOIN estados e ON (e.id = mT.idEstado)
			LEFT JOIN localidades l ON (mT.municipio=l.id)
			WHERE idTienda='" . $dato_tienda['idTienda'] . "'";

            $rs_ruta = $manager->ejecutarConsulta($sql_ruta);

            $dato_ruta = mysqli_fetch_array($rs_ruta);

            $objPHPExcel->getActiveSheet()->SetCellValue('A' . $tiendas_n, $dato_ruta['idTienda']);
            $objPHPExcel->getActiveSheet()->SetCellValue('B' . $tiendas_n, $dato_ruta['cadenaf']);
            $objPHPExcel->getActiveSheet()->SetCellValue('C' . $tiendas_n, $dato_ruta['numeroEconomico']);
            $objPHPExcel->getActiveSheet()->SetCellValue('D' . $tiendas_n, utf8_encode($dato_ruta['sucursal']));
            $objPHPExcel->getActiveSheet()->SetCellValue('E' . $tiendas_n, utf8_encode($dato_ruta['ciudadN']));
            $objPHPExcel->getActiveSheet()->SetCellValue('F' . $tiendas_n, $dato_ruta['formato']);
            $objPHPExcel->getActiveSheet()->SetCellValue('G' . $tiendas_n, utf8_encode($dato_ruta['estadoMex']));


            //******************************Cuenta de Islas **************//

            $n_marc = count($id_marca);
            $islas_tot = 0;
            $frentes_tot = 0;
            if ($n_marc > 0) {
                foreach ($id_marca as $marcas) {
                    $query_isla_fr = "CALL tiendas_vis(" . $dato_tienda['idTienda'] . "," . $marcas . ",'" . $desde . "','" . $hasta . "');";

                    $rs_isla_fr = $manager->ejecutarConsulta($query_isla_fr);

                    $dat_isla_fr = mysqli_fetch_array($rs_isla_fr);


                    if ($dat_isla_fr['islas'] != NULL || $dat_isla_fr['islas'] != "") {
                        $islas_tot += $dat_isla_fr['islas'];
                    }
                    if ($dat_isla_fr['frentesTot'] != NULL || $dat_isla_fr['frentesTot'] != "") {
                        $frentes_tot += $dat_isla_fr['frentesTot'];
                    }

                }

            }
            /*
            $query_isla="select count(idphotoCatalogo) as islas from photoCatalogo where id_exhibicion='1'
            and (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') )
            and id_tienda='".$dato_tienda['idTienda']."' and id_marca='".$id_marca."'";

            $rs_isla=$manager->ejecutarConsulta($query_isla);

            $dat_isla=mysqli_fetch_array($rs_isla);*/

            if ($islas_tot != 0) {
                $objPHPExcel->getActiveSheet()->SetCellValue('H' . $tiendas_n, $islas_tot);
            } else {
                $objPHPExcel->getActiveSheet()->SetCellValue('H' . $tiendas_n, '---');

            }


            /*$query_frentes="select sum(cha1+cha2+cha3+cha4+cha5+cha6) as frentes_tot from frentesCharola
            where (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') )
                    and idTienda='".$dato_tienda['idTienda']."' and idMarca='".$id_marca."'";

            $rs_frentes=$manager->ejecutarConsulta($query_frentes);

            $dat_frentes=mysqli_fetch_array($rs_frentes);*/

            //********************************* Sumar Frentes **********//
            if ($frentes_tot != 0) {
                $objPHPExcel->getActiveSheet()->SetCellValue('I' . $tiendas_n, $frentes_tot);

            } else {
                $objPHPExcel->getActiveSheet()->SetCellValue('I' . $tiendas_n, '---');

            }

            ///******************Visitas Requeridas dependiendo del tipo de tienda *****//

            if ($dato_ruta['idTipoTienda'] == 2) {
                $objPHPExcel->getActiveSheet()->SetCellValue('J' . $tiendas_n, '3');
                $objPHPExcel->getActiveSheet()->SetCellValue('K' . $tiendas_n, '3');
            } else {
                $objPHPExcel->getActiveSheet()->SetCellValue('J' . $tiendas_n, '1');
                $objPHPExcel->getActiveSheet()->SetCellValue('K' . $tiendas_n, '1');
            }


            $letra_tien = "L";

            //*****************************Recorrer los dias seleccionados para ver si hay visita ese dia *******************************//
            $total_visit = 0;
            $desde2 = $desde;
            while (strtotime($desde2) <= strtotime($hasta)) {

                $fecha = explode('-', $desde2);
                $n_dia = $manager->diaSemana($fecha[0], $fecha[1], $fecha[2]);

                if ($n_dia <= 6 || $n_dia >= 1) {

                    $q_visit = "SELECT * FROM tiendasVisitadas WHERE idTienda='" . $dato_tienda['idTienda'] . "' AND fecha='" . $desde2 . "'";

                    $r_visit = $manager->ejecutarConsulta($q_visit);

                    $d_visit = mysqli_fetch_array($r_visit);

                    if ($d_visit['idTiendasVisitadas'] != NULL && $d_visit['idTiendasVisitadas'] != "") {
                        $objPHPExcel->getActiveSheet()->SetCellValue($letra_tien . $tiendas_n, '');
                        $objPHPExcel->getActiveSheet()->getStyle($letra_tien . $tiendas_n)->applyFromArray($styleArray2);


                        $total_visit++;
                    } else {
                        $objPHPExcel->getActiveSheet()->SetCellValue($letra_tien . $tiendas_n, '');
                    }

                }

                $desde2 = date("d-m-Y", strtotime("$desde2 + 1 DAY"));

                $letra_tien++;
                usleep(1000);
            }//******************************************** Termina ciclo de Dias

            //////////////////////////////****Agrega links de las fotos *****///////////////// Agregar hyperlink
            $q_fotos = "SELECT * FROM photoCatalogo 
			WHERE (STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('" . $desde . "','%d-%m-%Y') AND STR_TO_DATE('" . $hasta . "','%d-%m-%Y') ) 
			AND id_tienda='" . $dato_tienda['idTienda'] . "' ";


            $n_marc = count($id_marca);
            $k = 0;
            if ($n_marc > 0) {
                foreach ($id_marca as $marcas) {
                    if ($k == 0) {
                        $q_fotos .= "and (id_marca='" . $marcas . "' ";
                    } else {
                        $q_fotos .= " or id_marca='" . $marcas . "'";
                    }
                    $k++;
                }
                $q_fotos .= ")";
            }


            $rs_fotos = $manager->ejecutarConsulta($q_fotos);

            $i = 1;
            while ($d_fotos = mysqli_fetch_array($rs_fotos)) {

                $objPHPExcel->getActiveSheet()->setCellValue($letra_tien . $tiendas_n, 'Foto_' . $d_fotos['idphotoCatalogo']);
                $objPHPExcel->getActiveSheet()->getCell($letra_tien . $tiendas_n)->getHyperlink()->setUrl('http://plataformavanguardia.net/' . $d_fotos['imagen']);

                $letra_tien++;
                $i++;
            }

            if ($i < 15) {
                while ($i <= 15) {
                    $letra_tien++;
                    $i++;
                }
            }

            ///****************************************Termina links Fotos

            ///************ Totales de Visitas dependiendo el tipo de tienda ****//

            if ($dato_ruta['idTipoTienda'] == 2) {
                $visit_res = $total_visit - 3;
            } else {
                $visit_res = $total_visit - 1;
            }

            if ($visit_res >= 0) {
                $objPHPExcel->getActiveSheet()->setCellValue($letra_tien . $tiendas_n, '');
                $objPHPExcel->getActiveSheet()->getStyle($letra_tien . $tiendas_n)->applyFromArray($styleArray3);
                $letra_tien++;
            } else {
                $objPHPExcel->getActiveSheet()->setCellValue($letra_tien . $tiendas_n, '');
                $objPHPExcel->getActiveSheet()->getStyle($letra_tien . $tiendas_n)->applyFromArray($styleArray5);
                $letra_tien++;
            }

            $objPHPExcel->getActiveSheet()->setCellValue($letra_tien . $tiendas_n, $total_visit);

            $tiendas_n++;
        }

    }
/////Final While Tiendas

    /*//***********Si se tiene el logo de la marca *****
    if(file_exists('../imagenes/logos/'.$datos_marca['nombre'].'.png'))
    {
        //  " Add a drawing to the worksheet\n";
        $objDrawing = new PHPExcel_Worksheet_Drawing();
        $objDrawing->setName('Logo Image');
        $objDrawing->setDescription('Logo');
        $logo = '../imagenes/logos/'.$datos_marca['nombre'].'.png';
        $objDrawing->setPath($logo);
        $objDrawing->setCoordinates('A1');
        $objDrawing->setHeight(150);
        $objDrawing->setWorksheet($objPHPExcel->getActiveSheet());

    }*/
// Rename sheet
    $objPHPExcel->getActiveSheet()->setTitle('TiendasVisitadas');


// Save Excel 2007 file
    $objWriter = new PHPExcel_Writer_Excel2007($objPHPExcel);

    $path_file = 'TiendasVisitadas  ' . $mes . '.xlsx';
//str_replace('.php', '.xlsx', __FILE__);
    echo $objWriter->save('../archivos/' . $path_file);

    echo $path_file;

} else {

    echo '0';

}



