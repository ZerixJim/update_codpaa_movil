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
include_once('../php/seguridad.php');


if(isset($_SESSION['usuario']) && isset($_SESSION['permiso'])){
	

///*********************************Recibo los datos de los filtros *******************************//
   
    $id_formato = $_REQUEST['idFormato'];
	 
	$desde = $_REQUEST['Desde'];
	  
	$hasta = $_REQUEST['Hasta'];
	
	$id_marca=$_REQUEST['idMarca'];
	
	$tiposProd=$_REQUEST['tiposProd'];
	
	$idUsuario=$_SESSION['idUser'];

	
	$filtro="";
	$n_for=count($id_formato);
	$k=0;
	if($n_for>0)
	{
		foreach($id_formato as $formatos)
		{
		   if($k==0)
		  {
			$filtro.=" where (idFormato='".$formatos."'";
		  }
		else
		{
			$filtro.=" or idFormato='".$formatos."'";
			}
			$k++;
	    }
		$filtro.=")";
	}
	
	
	$filtro2="";
	
	$n_tip=count($tiposProd);
	$j=0;
	if($n_tip>0)
	{
		foreach($tiposProd as $tipos_prod)
		{
			
			if($i==0)
			{
				$filtro2.=" and (p.tipo='".$tipos_prod."'";	
			}
			else
			{
				$filtro2.=" or p.tipo='".$tipos_prod."'";	
			}
			$i++;
		}	
		$filtro2.=")";
		
		}

	$fecha_ini=explode('-',$_REQUEST['Desde']);
	
    $manager = new bdManager();
	
	$mes=$manager->mesNombre($fecha_ini[1]);
	
	$query_marca="select nombre from Marca where idMarca='".$id_marca."'";
	$resul_marca=$manager->ejecutarConsulta($query_marca);
	$datos_marca=mysqli_fetch_array($resul_marca);
	
	$titulo=$mes.' '.$fecha_ini[2];


// Set properties
$objPHPExcel->getProperties()->setCreator("Codpaa Web");
$objPHPExcel->getProperties()->setLastModifiedBy("Codpaa Web");
$objPHPExcel->getProperties()->setTitle("Inteligencia de Mercado ".$titulo);
$objPHPExcel->getProperties()->setSubject("Inteligencia de Mercado ".$datos_marca['nombre']);
$objPHPExcel->getProperties()->setDescription("Archivo de Inteligencia de Mercado creado por Codpaa Web");


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
	
	$styleArray3 = array(
	'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => '58D3F7')
        )
	);
	$styleArray4 = array(
	'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => '2E2EFE')
        )
	);
	$linea=3;
	
	
			$objPHPExcel->getActiveSheet()->SetCellValue('A'.$linea, 'ID PROD');
			$objPHPExcel->getActiveSheet()->getStyle('A'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('B'.$linea, 'NOMBRE');
			$objPHPExcel->getActiveSheet()->getStyle('B'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('C'.$linea, 'CODIGO');
			$objPHPExcel->getActiveSheet()->getStyle('C'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('D'.$linea, 'TIPO');
			$objPHPExcel->getActiveSheet()->getStyle('D'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('E'.$linea, 'MODELO');
			$objPHPExcel->getActiveSheet()->getStyle('E'.$linea)->applyFromArray($styleArray);
			
			$sql_formatos="select idFormato,concat(cadena,' ',grupo) as nom_formato from tiendas_formatos ".$filtro."";
		
			$rs_formatos=$manager->ejecutarConsulta($sql_formatos);
		
			$col_tit='F';
			while($dat_formatos=mysqli_fetch_array($rs_formatos))
			{
				$objPHPExcel->getActiveSheet()->SetCellValue($col_tit.$linea, $dat_formatos['nom_formato']);
				$objPHPExcel->getActiveSheet()->getStyle($col_tit.$linea)->applyFromArray($styleArray);
				$objPHPExcel->getActiveSheet()->getStyle($col_tit.$linea)->getAlignment()->setWrapText(true);
				//$objPHPExcel->getActiveSheet()->getColumnDimension($col_tit)->setAutoSize(true);
				$objPHPExcel->getActiveSheet()->getRowDimension($linea)->setRowHeight(60);
				
				$col_tit++;
				}
			
			$linea++;
	
	
	$sql_prods="select p.idProducto,p.idMarca,concat(p.nombre, ' ',p.presentacion) as prod_nom,p.codigoBarras,p.tipo,p.modelo from Producto p 
	where p.idMarca='".$id_marca."' ".$filtro2."";
	
	$rs_prods=$manager->ejecutarConsulta($sql_prods);
	while($dato_prods=mysqli_fetch_array($rs_prods))
	{
		
			$objPHPExcel->getActiveSheet()->SetCellValue('A'.$linea, $dato_prods['idProducto']);
			$objPHPExcel->getActiveSheet()->getStyle('A'.$linea)->applyFromArray($styleArray2);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('B'.$linea,$dato_prods['prod_nom']);
			$objPHPExcel->getActiveSheet()->getStyle('B'.$linea)->applyFromArray($styleArray2);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('C'.$linea, '\'',$dato_prods['codigoBarras']);
			$objPHPExcel->getActiveSheet()->getStyle('C'.$linea)->applyFromArray($styleArray2);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('D'.$linea, $dato_prods['tipo']);
			$objPHPExcel->getActiveSheet()->getStyle('D'.$linea)->applyFromArray($styleArray2);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('E'.$linea, $dato_prods['modelo']);
			$objPHPExcel->getActiveSheet()->getStyle('E'.$linea)->applyFromArray($styleArray2);
			
			
			$col_dat='F';
			$sql_formatos="select idFormato,concat(cadena,' ',grupo) as nom_formato from tiendas_formatos ".$filtro."";
		
			$rs_formatos=$manager->ejecutarConsulta($sql_formatos);
			
		
		///// **********************************Ciclo Formatos
			while($dato_formatos=mysqli_fetch_array($rs_formatos))
			{
				$sql_normal = "select SUM(im.precioNormal) as tot_norm
				,count(idInteligencia) as tot_num
				from inteligenciaMercado im
				inner join maestroTiendas t on (im.idTienda=t.idTienda)
				where im.precioOferta='---' and im.idProducto='".$dato_prods['idProducto']."' and 
				(STR_TO_DATE(im.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) 
				and t.idFormato='".$dato_formatos['idFormato']."' 
				and (((im.precioNormal*100)/(select avg(precioNormal) from inteligenciaMercado where idProducto='".$dato_prods['idProducto']."'))<=140
				or ((im.precioNormal*100)/(select avg(precioNormal) from inteligenciaMercado where idProducto='".$dato_prods['idProducto']."'))>=80)
				GROUP BY im.idProducto";
				
				$rs_normal=$manager->ejecutarConsulta($sql_normal);
				
				$dato_normal=mysqli_fetch_array($rs_normal);
				
				$sql_oferta = "select SUM(im.precioOferta) as tot_ofer
				,count(idInteligencia) as tot_num
				from inteligenciaMercado im
				inner join maestroTiendas t on (im.idTienda=t.idTienda)
				where im.precioOferta!='---' and im.idProducto='".$dato_prods['idProducto']."' and 
				(STR_TO_DATE(im.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) 
				and t.idFormato='".$dato_formatos['idFormato']."' 
				and (((im.precioNormal*100)/(select avg(precioNormal) from inteligenciaMercado where idProducto='".$dato_prods['idProducto']."'))<=140
				or ((im.precioNormal*100)/(select avg(precioNormal) from inteligenciaMercado where idProducto='".$dato_prods['idProducto']."'))>=80)
				GROUP BY im.idProducto";
				
				$rs_oferta=$manager->ejecutarConsulta($sql_oferta);
				
				$dato_oferta=mysqli_fetch_array($rs_oferta);
				
				
				$tot_precio=$dato_normal['tot_norm']+$dato_oferta['tot_ofer'];
				
				$tot_im=$dato_normal['tot_num']+$dato_oferta['tot_num'];
				
				if($tot_im>0)
				{
					$prom_im=$tot_precio/$tot_im;
				}
				else
				{
					$prom_im=0;
					}
				
				if($prom_im>0)
				{
					$objPHPExcel->getActiveSheet()->SetCellValue($col_dat.$linea,round($prom_im,2));
					$objPHPExcel->getActiveSheet()->getStyle($col_dat.$linea)->applyFromArray($styleArray3);
				
				}
				else
				{
					$objPHPExcel->getActiveSheet()->SetCellValue($col_dat.$linea, '---');
					$objPHPExcel->getActiveSheet()->getStyle($col_dat.$linea)->applyFromArray($styleArray4);				
				}
					
				
				$col_dat++;
			}
			
			
			
			$linea++;
		
	}////********Final While Productos
	
	
	//***********Si se tiene el logo de la marca ******//
	if(file_exists('../imagenes/logos/'.$datos_marca['nombre'].'.png'))
	{
		//  " Add a drawing to the worksheet\n";
		$objDrawing = new PHPExcel_Worksheet_Drawing();
		$objDrawing->setName('Logo Image');
		$objDrawing->setDescription('Logo');
		$logo = '../imagenes/logos/'.$datos_marca['nombre'].'.png';
		$objDrawing->setPath($logo);
		$objDrawing->setCoordinates('A1');
		$objDrawing->setHeight(120);
		$objDrawing->setWorksheet($objPHPExcel->getActiveSheet());
		
	}
// Rename sheet
$objPHPExcel->getActiveSheet()->setTitle('Inteligencia de Mercado');

		
// Save Excel 2007 file
$objWriter = new PHPExcel_Writer_Excel2007($objPHPExcel);

$path_file='Inteligencia de Mercado '.$datos_marca['nombre'].' '.$mes.'.xlsx';
//str_replace('.php', '.xlsx', __FILE__);
echo $objWriter->save('../archivos/'.$path_file);

echo $path_file;

}else{

    echo '0';

}
?>


