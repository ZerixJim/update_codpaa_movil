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



if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){


///*********************************Recibo los datos de los filtros *******************************//

    $id_estado = $_REQUEST['idEstado'];

    $desde = $_REQUEST['Desde'];

    $hasta = $_REQUEST['Hasta'];

    $id_marca=$_REQUEST['idMarca'];

    $id_prod=$_REQUEST['idProd'];

    $idUsuario=$_SESSION['idUser'];

    $idTipoTien=$_SESSION['idTipoTien'];


    if($id_estado!="" && $id_estado!="all")
    {
        $filtro.=" and t.idEstado='".$id_estado."'";
    }

    if($idTipoTien!="")
    {
        $filtro.=" and t.idTipoTienda='".$idTipoTien."'";
    }

    $filtro2="";

    $n_prod=count($id_prod);
    $k=0;
    if($n_prod>0)
    {
        foreach($id_prod as $product)
        {
            if($k==0)
            {
                $filtro2.=" and (p.idProducto='".$product."'";
            }
            else
            {
                $filtro2.=" or p.idProducto='".$product."'";
            }
            $k++;
        }
        $filtro2.=")";
    }


    $manager = new bdManager();

    $mes=$manager->mesNombre($fecha_ini[1]);

    $query_marca="select nombre from Marca where idMarca='".$id_marca."'";
    $resul_marca=$manager->ejecutarConsulta($query_marca);
    $datos_marca=mysqli_fetch_array($resul_marca);

    $titulo=$mes.' '.$fecha_ini[2];


// Set properties
    $objPHPExcel->getProperties()->setCreator("Codpaa Web");
    $objPHPExcel->getProperties()->setLastModifiedBy("Codpaa Web");
    $objPHPExcel->getProperties()->setTitle("Surtido Mueble ".$titulo);
    $objPHPExcel->getProperties()->setSubject("Surtido Mueble ".$datos_marca['nombre']);
    $objPHPExcel->getProperties()->setDescription("Archivo de Surtido de Mueble creado por Codpaa Web");


    //******************************************************************************************//
    $objPHPExcel->setActiveSheetIndex(0);

//**********Estilo de la primer fila de titulos
    $styleArray = array(
        'font'  => array(
            'bold'  => true,
            'size'  => 13
        ),
        'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => '46E82F')
        ),
        'alignment' => array(
            'horizontal' => PHPExcel_Style_Alignment::HORIZONTAL_JUSTIFY,
        )
    );

    $sql_tienda="select t.*,p.idMarca,
	f.grupo AS formato,
	f.cadena AS cadenaf,
	e.nombre AS estadoMex,
	lo.nombre AS nom_mun,
	surt.idCelular AS idcelular,
	pro.nombre as nombre
	FROM maestroTiendas t
	LEFT JOIN surtidoMueble surt ON (t.idTienda = surt.idTienda)
	LEFT JOIN tiendas_formatos f ON (t.idFormato = f.idFormato)
	LEFT JOIN Promotores pro ON (pro.idCelular = surt.idCelular)
	LEFT JOIN estados e ON (e.id = t.idEstado)
	LEFT JOIN Producto p ON (p.idProducto=surt.idProducto)
	LEFT JOIN localidades lo ON (lo.id=t.municipio)
	WHERE p.idMarca='".$id_marca."'  ".$filtro."
	GROUP BY t.idTienda,p.idMarca";


    $rs_tienda=$manager->ejecutarConsulta($sql_tienda);
    // Add some data

    $objPHPExcel->getActiveSheet()->SetCellValue('A3', 'ID');
    $objPHPExcel->getActiveSheet()->getStyle('A3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('B3', 'CADENA');
    $objPHPExcel->getActiveSheet()->getStyle('B3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('C3', 'TIENDA');
    $objPHPExcel->getActiveSheet()->getStyle('C3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('D3', 'IDCARTERA');
    $objPHPExcel->getActiveSheet()->getStyle('D3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('E3', 'NOMBRE');
    $objPHPExcel->getActiveSheet()->getStyle('E3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('F3', 'CIUDAD');
    $objPHPExcel->getActiveSheet()->getStyle('F3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('G3', 'FORMATO');
    $objPHPExcel->getActiveSheet()->getStyle('G3')->applyFromArray($styleArray);

    $objPHPExcel->getActiveSheet()->SetCellValue('H3', 'ESTADO');
    $objPHPExcel->getActiveSheet()->getStyle('H3')->applyFromArray($styleArray);


    $query_prod="select surt.idProducto,concat(p.nombre,' ',p.presentacion) as nombre from surtidoMueble surt 
		inner join Producto p on (surt.idProducto=p.idProducto) 
		inner join maestroTiendas t on (surt.idTienda=t.idTienda)
		where p.idMarca='".$id_marca."'  ".$filtro2." 
		group by surt.idProducto order by surt.idProducto";

    $rs_prod=$manager->ejecutarConsulta($query_prod);

    $letra_head='I';

    while($datos_prod=mysqli_fetch_array($rs_prod))
    {
        $letra_head;
        $objPHPExcel->getActiveSheet()->SetCellValue($letra_head.'3', strtoupper($datos_prod['nombre']));
        $objPHPExcel->getActiveSheet()->getStyle($letra_head.'3')->applyFromArray($styleArray);

        $letra_head++;

    }

    $tiendas_n=4;
    ///// **********************************Ciclo Tiendas
    while($dato_tienda=mysqli_fetch_array($rs_tienda))
    {
        $query_prod="select surt.idProducto,p.nombre from surtidoMueble surt 
		inner join Producto p on (surt.idProducto=p.idProducto) 
		inner join maestroTiendas t on (surt.idTienda=t.idTienda)
		where p.idMarca='".$id_marca."' ".$filtro2." 
		group by surt.idProducto order by surt.idProducto";


        $rs_prod = $manager->ejecutarConsulta($query_prod);

        $objPHPExcel->getActiveSheet()->SetCellValue('A'.$tiendas_n, $dato_tienda['idTienda']);
        $objPHPExcel->getActiveSheet()->SetCellValue('B'.$tiendas_n, $dato_tienda['cadenaf']);
        $objPHPExcel->getActiveSheet()->SetCellValue('C'.$tiendas_n, $dato_tienda['sucursal']);
        $objPHPExcel->getActiveSheet()->SetCellValue('D'.$tiendas_n, $dato_tienda['idcelular']);
        $objPHPExcel->getActiveSheet()->SetCellValue('E'.$tiendas_n, $dato_tienda['nombre']);
        $objPHPExcel->getActiveSheet()->SetCellValue('F'.$tiendas_n,utf8_encode($dato_tienda['nom_mun']));
        $objPHPExcel->getActiveSheet()->SetCellValue('G'.$tiendas_n, $dato_tienda['formato']);
        $objPHPExcel->getActiveSheet()->SetCellValue('H'.$tiendas_n,utf8_encode($dato_tienda['estadoMex']));

        $letra_tien="I";

        //*****************************Ciclo Inventarios Por Tienda Por Producto *******************************//
        while($datos_prod = mysqli_fetch_array($rs_prod))
        {

            $sql_surt = "select sum(cajas) as total_cajas from surtidoMueble 
			where idProducto='".$datos_prod['idProducto']."' and 
			(STR_TO_DATE(fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) 
			and idTienda='".$dato_tienda['idTienda']."' and surtido='Si' group by idProducto order by idsurtidoMueble DESC";

            $rs_surt=$manager->ejecutarConsulta($sql_surt);

            $datos_surt=mysqli_fetch_array($rs_surt);

            if($datos_surt['total_cajas']!=0)
            {

                $objPHPExcel->getActiveSheet()->SetCellValue($letra_tien.$tiendas_n,$datos_surt['total_cajas']);
            }
            else
            {
                $objPHPExcel->getActiveSheet()->SetCellValue($letra_tien.$tiendas_n,'0');
            }


            $letra_tien++;
            usleep(4000);
        }
        //******************************************** Final While Inventarios Tienda-Producto***************************************//


        $tiendas_n++;

    }/////Final While Tiendas

    //***********Si se tiene el logo de la marca ******//
    /*if(file_exists('../imagenes/logos/'.$datos_marca['nombre'].'.png'))
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
    $objPHPExcel->getActiveSheet()->setTitle('Surtido Mueble');


// Save Excel 2007 file
    $objWriter = new PHPExcel_Writer_Excel2007($objPHPExcel);

    $path_file='Surtido Mueble '.$datos_marca['nombre'].' '.$mes.'.xlsx';
//str_replace('.php', '.xlsx', __FILE__);
    echo $objWriter->save('../archivos/'.$path_file);

    echo $path_file;

}else{

    echo '0';

}
?>



