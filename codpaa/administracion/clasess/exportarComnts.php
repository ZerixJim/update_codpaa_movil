<?php

/**

 * Created by DreamW.

 * User: Christian

 * Date: 31/09/16	

 * Time: 17:59

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
include_once('../php/seguridad.php');


if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	

///*********************************Recibo los datos de los filtros *******************************//
   
    $tipo = $_SESSION['tipo'];
	$idUsuario = $_SESSION['idUser'];

	
	$filtro="";
		
	if($_REQUEST['fechaIni']!="" && isset($_REQUEST['fechaIni']) && $_REQUEST['fechaFin']!="" && isset($_REQUEST['fechaFin']))		
	{
		
		 $filtro.=" and (STR_TO_DATE(ct.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$_REQUEST['fechaIni']."','%d-%m-%Y') and STR_TO_DATE('".$_REQUEST['fechaFin']."','%d-%m-%Y') )";
		}
	  
	if($_REQUEST['buscarComnt']!="*" and isset($_REQUEST['buscarComnt']))
	{
		
		$filtro="  and (p.nombre like '%".$_REQUEST['buscarComnt']."%' OR ct.idTienda='".$_REQUEST['buscarComnt']."' OR ct.idCelular='".$_REQUEST['buscarComnt']."') ";
		}

	$manager = new bdManager();
	
	$fecha_ini=explode('-',$_REQUEST['fechaIni']);
		
	$mes=$manager->mesNombre($fecha_ini[1]);
		
	$titulo=$_REQUEST['fechaIni']." al ".$_REQUEST['fechaFin'];


// Set properties
$objPHPExcel->getProperties()->setCreator("Codpaa Web");
$objPHPExcel->getProperties()->setLastModifiedBy("Codpaa Web");
$objPHPExcel->getProperties()->setTitle("Comentarios ".$titulo);
$objPHPExcel->getProperties()->setSubject("Comentarios");
$objPHPExcel->getProperties()->setDescription("Archivo de Comentarios creado por Codpaa Web");


	//******************************************************************************************//
	$objPHPExcel->setActiveSheetIndex(0);
		
	
	
	$styleArray = array(
    'font'  => array(
        'bold'  => false,
        'size'  => 11
    ),
	'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => 'A4A4A4')
        )
	);
	
	$styleArray2 = array(
	'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => 'D8D8D8')
        )
	);
	
	$linea=3;
	
	
			$objPHPExcel->getActiveSheet()->SetCellValue('A'.$linea, 'ID TIENDA');
			$objPHPExcel->getActiveSheet()->getStyle('A'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('B'.$linea, 'TIENDA');
			$objPHPExcel->getActiveSheet()->getStyle('B'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('C'.$linea, 'FECHA');
			$objPHPExcel->getActiveSheet()->getStyle('C'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('D'.$linea, 'ID PROM');
			$objPHPExcel->getActiveSheet()->getStyle('D'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('E'.$linea, 'PROMOTOR');
			$objPHPExcel->getActiveSheet()->getStyle('E'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('F'.$linea, 'COMENTARIO');
			$objPHPExcel->getActiveSheet()->getStyle('F'.$linea)->applyFromArray($styleArray);
						
			$linea++;
	
	
	if($_SESSION['id_perfil'] == '1')
	{	
      $sql = "select ct.*,p.nombre as Promotor,mt.sucursal as Tienda from comentarioTienda ct 
		left join Promotores p on (ct.idCelular=p.idCelular)
		left join maestroTiendas mt on (mt.idTienda=ct.idTienda) 
		where comentario!='' 
		".$filtro."
		order by ct.idCelular";
	
		}
	else if ($_SESSION['id_perfil'] == '2' || $_SESSION['id_perfil']=='5')
	{
		$sql = "select ct.*,p.nombre as Promotor,mt.sucursal as Tienda from comentarioTienda ct 
		left join Promotores p on (ct.idCelular=p.idCelular)
		left join maestroTiendas mt on (mt.idTienda=ct.idTienda) 
		inner join  marcaAsignadaPromotor M on (M.idPromotor=p.idCelular)
		where comentario!='' and M.idMarca IN (select idMarca from usuariosMarcaAsignada where idUsuario='".$idUsuario."') 
		".$filtro."
		group by ct.idcomentarioTienda
		order by ct.idCelular";
		
		}
	else if($_SESSION['id_perfil']=='3' || $_SESSION['id_perfil']=='8')
	{
	  	$sql = "select ct.*,p.nombre as Promotor,mt.sucursal as Tienda from comentarioTienda ct 
		left join Promotores p on (ct.idCelular=p.idCelular)
		left join maestroTiendas mt on (mt.idTienda=ct.idTienda) 
		left join Supervisores S on (p.Supervisor=S.idSupervisores) 
		where comentario!='' and p.Supervisor = (select idSupervisor from usuarios where idUsuario='".$idUsuario."') 
		".$filtro."
		group by ct.idcomentarioTienda
		order by ct.idCelular";
	}
	
	else if ($_SESSION['id_perfil']=='6')
	{
	
		$sql = "select ct.*,p.nombre as Promotor,mt.sucursal as Tienda from comentarioTienda ct 
		left join Promotores p on (ct.idCelular=p.idCelular)
		left join maestroTiendas mt on (mt.idTienda=ct.idTienda) 
		inner join  marcaAsignadaPromotor M on (M.idPromotor=p.idCelular)
		where comentario!='' and
		M.idMarca IN (select idMarca from ClientesMarcas where idCliente = (select idCliente from usuarios where idUsuario='".$idUsuario."'))
		".$filtro."
		group by ct.idcomentarioTienda
		order by ct.idCelular";
	}
	
	$rs_comnt=$manager->ejecutarConsulta($sql);
	while($dato_comnt=mysqli_fetch_array($rs_comnt))
	{
		
			$objPHPExcel->getActiveSheet()->SetCellValue('A'.$linea, $dato_comnt['idTienda']);
			$objPHPExcel->getActiveSheet()->getStyle('A'.$linea)->applyFromArray($styleArray2);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('B'.$linea,htmlentities($dato_comnt['Tienda']));
			$objPHPExcel->getActiveSheet()->getStyle('B'.$linea)->applyFromArray($styleArray2);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('C'.$linea,$dato_comnt['fecha']);
			$objPHPExcel->getActiveSheet()->getStyle('C'.$linea)->applyFromArray($styleArray2);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('D'.$linea, $dato_comnt['idCelular']);
			$objPHPExcel->getActiveSheet()->getStyle('D'.$linea)->applyFromArray($styleArray2);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('E'.$linea,utf8_encode($dato_comnt['Promotor']));
			$objPHPExcel->getActiveSheet()->getStyle('E'.$linea)->applyFromArray($styleArray2);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('F'.$linea, utf8_decode($dato_comnt['comentario']));
			$objPHPExcel->getActiveSheet()->getStyle('F'.$linea)->applyFromArray($styleArray2);
					
			$linea++;
		
	}////********Final While Comentarios
	
	// Rename sheet
$objPHPExcel->getActiveSheet()->setTitle('Comentarios');

		
// Save Excel 2007 file
$objWriter = new PHPExcel_Writer_Excel2007($objPHPExcel);

$path_file='Comentarios '.$mes.'.xlsx';
//str_replace('.php', '.xlsx', __FILE__);
echo $objWriter->save('../archivos/'.$path_file);

echo $path_file;

}else{

    echo '0';

}
?>


