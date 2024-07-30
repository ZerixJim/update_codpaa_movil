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
    $id_formato = $_REQUEST['idFormato'];
	
	$id_estado = $_REQUEST['idEstado'];
	 
	$desde = $_REQUEST['Desde'];
	  
	$hasta = $_REQUEST['Hasta'];
	
	$id_marca=$_REQUEST['idMarca'];
	
	$id_prod=$_REQUEST['idProd'];

	
	$filtro="";
	
	$n_for=count($id_formato);
	$k=0;
	if($n_for>0)
	{
		foreach($id_formato as $formatos)
		{
		   if($k==0)
		  {
			$filtro.=" and (t.idFormato='".$formatos."'";
		  }
		else
		{
			$filtro.=" or t.idFormato='".$formatos."'";
			}
			$k++;
	    }
		$filtro.=")";
	}
	if($id_estado!="" && $id_estado!="all")
	{
		$filtro.=" and t.idEstado";
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
$objPHPExcel->getProperties()->setTitle("Frentes ".$titulo);
$objPHPExcel->getProperties()->setSubject("Frentes ".$datos_marca['nombre']);
$objPHPExcel->getProperties()->setDescription("Archivo de Frentes creado por Codpaa Web");


	//******************************************************************************************//
	$objPHPExcel->setActiveSheetIndex(0);
		
	$sql_prods="select p.idProducto,p.idMarca,concat(p.nombre, ' ',p.presentacion) as prod_nom from Producto p 
	inner join Marca m on (p.idMarca=m.idMarca)
	where p.idMarca='".$id_marca."' ".$filtro2."";
	
	$rs_prods=$manager->ejecutarConsulta($sql_prods);
	
	$styleArray = array(
    'font'  => array(
        'bold'  => true,
        'size'  => 15
    ),
	'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => 'FF8000')
        )
	);
	
	$styleArray2 = array(
	'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => '02E92B')
        )
	);
	
	$styleArray3 = array(
	'fill' => array(
            'type' => PHPExcel_Style_Fill::FILL_SOLID,
            'color' => array('rgb' => 'FF0308')
        )
	);
	$linea=3;
	while($dato_prods=mysqli_fetch_array($rs_prods))
	{
		
		$sql_tienda="select t.*,fre.idMarca,f.grupo as formato,f.cadena as cadenaf,e.nombre as estadoMex from maestroTiendas t 
		inner join frentesCharola fre on (t.idTienda=fre.idTienda)
		left join tiendas_formatos f on (t.idFormato=f.idFormato)
		left join estados e on (e.id=t.idEstado)
		where fre.idMarca='".$id_marca."' ".$filtro."
		GROUP BY t.idTienda,fre.idMarca";
		
		
		$rs_tienda=$manager->ejecutarConsulta($sql_tienda);
		
			
			$objPHPExcel->getActiveSheet()->SetCellValue('A'.$linea, 'PRODUCTO: '.$dato_prods['prod_nom']);
			$objPHPExcel->getActiveSheet()->getStyle('A'.$linea)->applyFromArray($styleArray);
			$linea++;
			
			$objPHPExcel->getActiveSheet()->SetCellValue('A'.$linea, 'ID');
			$objPHPExcel->getActiveSheet()->getStyle('A'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('B'.$linea, 'CADENA');
			$objPHPExcel->getActiveSheet()->getStyle('B'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('C'.$linea, 'NO.');
			$objPHPExcel->getActiveSheet()->getStyle('C'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('D'.$linea, 'TIENDA');
			$objPHPExcel->getActiveSheet()->getStyle('D'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('E'.$linea, 'CIUDAD');
			$objPHPExcel->getActiveSheet()->getStyle('E'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('F'.$linea, 'FORMATO');
			$objPHPExcel->getActiveSheet()->getStyle('F'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('G'.$linea, 'ESTADO');
			$objPHPExcel->getActiveSheet()->getStyle('G'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('H'.$linea, 'FRENTES');
			$objPHPExcel->getActiveSheet()->getStyle('H'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('I'.$linea, 'FECHA');
			$objPHPExcel->getActiveSheet()->getStyle('I'.$linea)->applyFromArray($styleArray);
			
			$objPHPExcel->getActiveSheet()->SetCellValue('J'.$linea, '');
			$objPHPExcel->getActiveSheet()->getStyle('J'.$linea)->applyFromArray($styleArray);
			
			$linea++;
		
		///// **********************************Ciclo Tiendas
			while($dato_tienda=mysqli_fetch_array($rs_tienda))
			{
				
				$objPHPExcel->getActiveSheet()->SetCellValue('A'.$linea, $dato_tienda['idTienda']);
				$objPHPExcel->getActiveSheet()->SetCellValue('B'.$linea, $dato_tienda['cadenaf']);
				$objPHPExcel->getActiveSheet()->SetCellValue('C'.$linea, $dato_tienda['numeroEconomico']);
				$objPHPExcel->getActiveSheet()->SetCellValue('D'.$linea, $dato_tienda['sucursal']);
				$objPHPExcel->getActiveSheet()->SetCellValue('E'.$linea, $dato_tienda['municipio']);
				$objPHPExcel->getActiveSheet()->SetCellValue('F'.$linea, $dato_tienda['formato']);
				$objPHPExcel->getActiveSheet()->SetCellValue('G'.$linea, utf8_encode($dato_tienda['estadoMex']));
				
				$sql_fren = "select f.fecha,sum(cha1+cha2+cha3+cha4+cha5+cha6) as frentes_tot,
				f.idFrentesCharola,f.idTienda,f.idCelular,
				if(DATEDIFF(CURDATE(),STR_TO_DATE(f.fecha,'%d-%m-%Y'))>0,DATEDIFF(CURDATE(),STR_TO_DATE(f.fecha,'%d-%m-%Y')),0) as dias_dif 
				from frentesCharola f 
				inner join Producto p on (f.idProducto=p.idProducto) 
				where f.idProducto='".$dato_prods['idProducto']."' and 
				(STR_TO_DATE(f.fecha,'%d-%m-%Y') BETWEEN STR_TO_DATE('".$desde."','%d-%m-%Y') and STR_TO_DATE('".$hasta."','%d-%m-%Y') ) 
				and f.idTienda='".$dato_tienda['idTienda']."' 
				GROUP BY f.idTienda,f.fecha
				ORDER BY f.idTienda,f.idFrentesCharola DESC";
				
				$rs_fren=$manager->ejecutarConsulta($sql_fren);
				
				$dato_fren=mysqli_fetch_array($rs_fren);
				if($dato_fren['dias_dif']>0)
				{
				
				$objPHPExcel->getActiveSheet()->SetCellValue('H'.$linea, $dato_fren['frentes_tot']);
				$objPHPExcel->getActiveSheet()->SetCellValue('I'.$linea, $dato_fren['fecha']);
				$objPHPExcel->getActiveSheet()->SetCellValue('J'.$linea, "Fue tomado hace ".$dato_fren['dias_dif']." dias");
				
				$objPHPExcel->getActiveSheet()->getStyle('J'.$linea)->applyFromArray($styleArray2);
				}
				else
				{
				
				$objPHPExcel->getActiveSheet()->SetCellValue('H'.$linea, 'NO SE HA TOMADO FRENTES');
				$objPHPExcel->getActiveSheet()->SetCellValue('I'.$linea, 'NO SE HA TOMADO FRENTES');
				$objPHPExcel->getActiveSheet()->SetCellValue('J'.$linea, '');
				
				$objPHPExcel->getActiveSheet()->getStyle('J'.$linea)->applyFromArray($styleArray3);

					}
					
					
			$linea++;
		//******************************************** Final While Inventarios Tienda-Producto*******************************************************//
		
			
		}/////******Final While Tiendas
		
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
$objPHPExcel->getActiveSheet()->setTitle('Frentes');

		
// Save Excel 2007 file
$objWriter = new PHPExcel_Writer_Excel2007($objPHPExcel);

$path_file='Frentes '.$datos_marca['nombre'].' '.$mes.'.xlsx';
//str_replace('.php', '.xlsx', __FILE__);
echo $objWriter->save('../archivos/'.$path_file);

echo $path_file;

}else{

    echo '0';

}
?>


