<?php
/**

 * Created by DreamW.

 * User: Christian

 * Date: 29/06/16

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
include_once('../php/seguridad.php');

if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
		

///*********************************Recibo los datos de los filtros *******************************//

	$manager = new bdManager();

	$id_estado = $_REQUEST['idEstado'];
	
	$MesLab = $_REQUEST['MesLab'];
		
	$fecha_c=explode('-',$MesLab);
	
	$dias_mes=date('t',$fecha_c[0]);
	
		
	$mes=intval($fecha_c[0]);

	 if($mes<10)
	 {
		 $mes="0".$mes;
		 }
	//Fechas del mes para consultas....
	$fecha_ini="01-".$mes."-".$fecha_c[1];
	
	$fecha_fin=$dias_mes."-".$mes."-".$fecha_c[1];
	
	
	
	$mes_vent=intval($fecha_c[0])-1;
	
	$dias_mes_v=date('t',$mes_vent);
	
	if($mes_vent<10)
	{
		$mes_vent="0".$mes_vent;
	}
	if($fecha_c[0]==1)
	{
		$anio_vent=$fecha_c[1]-1;
	}
	else
	{
		$anio_vent=$fecha_c[1];	
		}
	//Fechas del mes para consulta de ventas...
	$fecha_ini_v="01-".$mes_vent."-".$anio_vent;
	$fecha_fin_v=$dias_mes_v."-".$mes_vent."-".$anio_vent;
	
	$id_marca=$_REQUEST['idMarca'];
	
	$filtro="";
		
	if($id_estado!="" && $id_estado!="all")
	{
		$filtro.=" and mT.idEstado='".$id_estado."'";
		}
	
	$titulo=$mes.' '.$fecha_c[1];
	
	// Set properties
	$objPHPExcel->getProperties()->setCreator("Codpaa Web");
	$objPHPExcel->getProperties()->setLastModifiedBy("Codpaa Web");
	$objPHPExcel->getProperties()->setTitle("Inteli de Mercado ".$titulo);
	$objPHPExcel->getProperties()->setSubject("Inteligencia de Mercado Por Precios");
	$objPHPExcel->getProperties()->setDescription("Archivo de Inteligencia de Mercado creado por Codpaa Web");


	//******************************************************************************************//
	$objPHPExcel->setActiveSheetIndex(0);
	
	
	$filtro_mar="";
	$sql_tienda="Select DISTINCT mT.idTienda from cod_tienda_marca_promotor ctm  
	inner join maestroTiendas mT on (mT.idTienda=ctm.idTienda)
	where mT.idTipoTienda='2' and mes='".$fecha_c[0]."' and anio='".$fecha_c[1]."' and idMarca='".$id_marca."' ";
	$sql_tienda.=" ".$filtro."  
		GROUP BY ctm.idTienda
		order by mT.grupo ASC";
	/*$n_marc=count($id_marca);
		$k=0;
			if($n_marc>0)
			{
					foreach($id_marca as $marcas)
					{
					    if($k==0)
					  {
						$sql_tienda.=" '".$marcas."' ";
						$filtro_mar.=" '".$marcas."'";
					  }
					else
					{
						$sql_tienda.=" ,'".$marcas."'";
						$filtro_mar.= " ,'".$marcas."'";
						}
						$k++;
					}
					$sql_tienda.=")";
			}*/
		
	
	   $rs_tienda=$manager->ejecutarConsulta($sql_tienda);
	   
	   $styleArray = array(
		'font'  => array(
			'bold'  => false,
			'size'  => 11,
			'color' =>array('rgb' => 'FFFFFF')
		),
		'fill' => array(
				'type' => PHPExcel_Style_Fill::FILL_SOLID,
				'color' => array('rgb' => 'FFA726')
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
				'color' => array('rgb' => 'FE2E2E')
			)
		);
	   
	   $linea=3;
	
	
		$objPHPExcel->getActiveSheet()->SetCellValue('A'.$linea, 'No Econ');
		$objPHPExcel->getActiveSheet()->getStyle('A'.$linea)->applyFromArray($styleArray);
		$objPHPExcel->getActiveSheet()->freezePane('A4');
		
		$objPHPExcel->getActiveSheet()->SetCellValue('B'.$linea, 'ID');
		$objPHPExcel->getActiveSheet()->getStyle('B'.$linea)->applyFromArray($styleArray);
		
		
		$objPHPExcel->getActiveSheet()->SetCellValue('C'.$linea, 'Cadena');
		$objPHPExcel->getActiveSheet()->getStyle('C'.$linea)->applyFromArray($styleArray);
		
		
		$objPHPExcel->getActiveSheet()->SetCellValue('D'.$linea, 'Grupo');
		$objPHPExcel->getActiveSheet()->getStyle('D'.$linea)->applyFromArray($styleArray);
		
		
		$objPHPExcel->getActiveSheet()->SetCellValue('E'.$linea, 'Tienda');
		$objPHPExcel->getActiveSheet()->getStyle('E'.$linea)->applyFromArray($styleArray);
		
		
		$objPHPExcel->getActiveSheet()->SetCellValue('F'.$linea, 'Ciudad');
		$objPHPExcel->getActiveSheet()->getStyle('F'.$linea)->applyFromArray($styleArray);
		
		
		$objPHPExcel->getActiveSheet()->SetCellValue('G'.$linea, 'Estado');
		$objPHPExcel->getActiveSheet()->getStyle('G'.$linea)->applyFromArray($styleArray);
		
		
	   	
				
		$query_prod="select concat(SUBSTRING(pro.nombre,1,20),' ',pro.presentacion) as nombre 
			from Producto pro 
			inner join inteligenciaMercado im on (im.idProducto=pro.idProducto)
			where pro.idMarca='".$id_marca."' and (STR_TO_DATE(im.fecha,'%d-%m-%Y') 
			BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
			and pro.estatus='1' 
			Group by pro.idProducto order by nombre";
		
		$rs_prod=$manager->ejecutarConsulta($query_prod);
		
		$col_tit='H';
		while($dat_prod=mysqli_fetch_array($rs_prod))
		{
						
			$objPHPExcel->getActiveSheet()->SetCellValue($col_tit.$linea, $dat_prod['nombre']);
			$objPHPExcel->getActiveSheet()->getStyle($col_tit.$linea)->applyFromArray($styleArray);
			$objPHPExcel->getActiveSheet()->getStyle($col_tit.$linea)->getAlignment()->setWrapText(true);
			$objPHPExcel->getActiveSheet()->getRowDimension($linea)->setRowHeight(60);
			
			$col_tit++;
		}
		
		$linea++;
	$visitada = array();
	///// **********************************Ciclo Tiendas
	while($dato_tienda=mysqli_fetch_array($rs_tienda))
	{
		
			$sql_ruta = "select mT.*,
			f.cadena AS cadenaf,
			e.nombre AS estadoMex,
			l.nombre AS ciudadN
			from maestroTiendas mT 
			LEFT JOIN tiendas_formatos f ON (mT.idFormato = f.idFormato)
			LEFT JOIN estados e ON (e.id = mT.idEstado)
			LEFT JOIN localidades l on (mT.municipio=l.id)
			where idTienda='".$dato_tienda['idTienda']."'";
				
			$rs_ruta=$manager->ejecutarConsulta($sql_ruta);
			
			$dato_ruta=mysqli_fetch_array($rs_ruta);
			
			$sql_vis="select COUNT(idTiendasVisitadas) as tot_vis from tiendasVisitadas 
			where fecha like '%".$fecha_c[0]."-".$fecha_c[1]."%' and idTienda='".$dato_tienda['idTienda']."' and tipo='E'";
			
			$rs_vis=$manager->ejecutarConsulta($sql_vis);
			
			$dato_vis=mysqli_fetch_array($rs_vis);
			
			if($dato_vis['tot_vis']>0)
			{
				$visitada=$styleArray2;
				}
			else
			{
				$visitada=$styleArray3;
				}
				
			$objPHPExcel->getActiveSheet()->SetCellValue('A'.$linea, $dato_ruta['numeroEconomico']);
			$objPHPExcel->getActiveSheet()->getStyle('A'.$linea)->applyFromArray($visitada);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('B'.$linea, $dato_ruta['idTienda']);
			$objPHPExcel->getActiveSheet()->SetCellValue('C'.$linea, $dato_ruta['cadenaf']);
			$objPHPExcel->getActiveSheet()->SetCellValue('D'.$linea, $dato_ruta['grupo']);
			$objPHPExcel->getActiveSheet()->SetCellValue('E'.$linea,utf8_encode($dato_ruta['sucursal']));
			$objPHPExcel->getActiveSheet()->SetCellValue('F'.$linea, utf8_encode($dato_ruta['ciudadN']));
			$objPHPExcel->getActiveSheet()->SetCellValue('G'.$linea, utf8_encode($dato_ruta['estadoMex']));
						
			$query_prod="select pro.idProducto from Producto pro
				inner join inteligenciaMercado im on (im.idProducto=pro.idProducto) 
				where pro.idMarca='".$id_marca."' and pro.estatus='1'
				and (STR_TO_DATE(im.fecha,'%d-%m-%Y') 
				BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))
				Group by pro.idProducto
				order by nombre";
		
			$rs_prod=$manager->ejecutarConsulta($query_prod);
			
			$col_dat='H';
			while($dat_prod=mysqli_fetch_array($rs_prod))
			{
				
				$query_im_prod="select precioNormal from inteligenciaMercado where idTienda='".$dato_ruta['idTienda']."' 
				and idProducto='".$dat_prod['idProducto']."' and (STR_TO_DATE(fecha,'%d-%m-%Y') 
				BETWEEN STR_TO_DATE('".$fecha_ini."','%d-%m-%Y') and STR_TO_DATE('".$fecha_fin."','%d-%m-%Y'))";
				
				$rs_im_prod=$manager->ejecutarConsulta($query_im_prod);
				
				$precios="";
				while($dat_im_prod=mysqli_fetch_array($rs_im_prod))
				{
					$precios.=$dat_im_prod['precioNormal']."\r";
										
					}
				$objPHPExcel->getActiveSheet()->setCellValue($col_dat.$linea, $precios);
  				$objPHPExcel->getActiveSheet()->getStyle($col_dat.$linea)->getAlignment()->setWrapText(true);
				
				$col_dat++;
			}
			
			
			$linea++;
		usleep(2500);
	
}
/////****************Final While Tiendas
  
 // Rename sheet
$objPHPExcel->getActiveSheet()->setTitle('Inteligencia de Mercado x Prec');

		
// Save Excel 2007 file
$objWriter = new PHPExcel_Writer_Excel2007($objPHPExcel);

$path_file='Inteligencia de Merc Por Precios '.$mes.'-'.$fecha_c[1].'.xlsx';
//str_replace('.php', '.xlsx', __FILE__);
echo $objWriter->save('../archivos/'.$path_file);

echo $path_file;
?>

<?
}else{

    echo 'no has iniciado sesion';

    header('refresh:2,../index.php');

}



