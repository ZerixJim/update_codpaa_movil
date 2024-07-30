<?php

use PhpOffice\PhpPowerpoint\PhpPowerpoint;
use PhpOffice\PhpPowerpoint\Style\Alignment;
use PhpOffice\PhpPowerpoint\Style\Bullet;
use PhpOffice\PhpPowerpoint\Style\Color;
use PhpOffice\PhpPowerpoint\Slide\Background\Image;


include_once '../clasess/Sample_Header.php';

include_once('../connexion/bdManager.php');

$manager = new bdManager();

$colorBlack = new Color( 'FF000000' );

$colorRed= new Color('FF0000');

///***************Recepcion de datos

$array_fotos = $_REQUEST['dataFoto'];

$fecha_ini=explode('-',$_REQUEST['fechaIni']);

$id_marca=$_REQUEST['idMarca'];

////********************************************

$mes=$manager->mesNombre($fecha_ini[1]);

$query_marca="select nombre from Marca where idMarca='".$id_marca."'";
$resul_marca=$manager->ejecutarConsulta($query_marca);
$datos_marca=mysqli_fetch_array($resul_marca);

$titulo=$mes.' '.$fecha_ini[2];

if($id_marca==1 || $id_marca==77 || $id_marca==78 || $id_marca==79 || $id_marca==80 || $id_marca==81)
{
	$datos_marca['nombre']="HENKEL";
}

// Create new PHPPowerPoint object
$objPHPPowerPoint = new PhpPowerpoint();

// Set properties
$objPHPPowerPoint->getProperties()->setCreator('CODPAA')
                                  ->setLastModifiedBy('CODPAA')
                                  ->setTitle('Fotografico '.$titulo)
                                  ->setSubject($datos_marca['nombre'])
                                  ->setDescription('Fotografico Creado por Codpaa Web')
                                  ->setKeywords('office 2007')
                                  ->setCategory('Presentacion');

// Remove first slide
$objPHPPowerPoint->removeSlideByIndex(0);

///****************************************Crear Portada************************************//

$currentSlide = createTemplatedSlide($objPHPPowerPoint); // local function

if(file_exists('../imagenes/logos/'.$datos_marca['nombre'].'.png'))
{
	$shape = $currentSlide->createDrawingShape();
	$shape->setName('FotoCat')
	->setDescription('Foto')
	->setPath('../imagenes/logos/'.$datos_marca['nombre'].'.png')
	->setHeight(250)
	->setOffsetX(400)
	->setOffsetY(100);
	$shape->getShadow()->setVisible(true)
	->setDirection(45)
	->setDistance(10);
	
	}

// Create a shape (text)
$shape = $currentSlide->createRichTextShape();
$shape->setHeight(200);
$shape->setWidth(600);
$shape->setOffsetX(10);
$shape->setOffsetY(400);
$shape->getActiveParagraph()->getAlignment()->setHorizontal( Alignment::HORIZONTAL_LEFT ); 

	


$textRun = $shape->createTextRun('Fotografico');
$textRun->getFont()->setBold(true);
$textRun->getFont()->setSize(28);
$textRun->getFont()->setColor($colorBlack);

$shape->createBreak();

$textRun = $shape->createTextRun($titulo);
$textRun->getFont()->setBold(true);
$textRun->getFont()->setSize(60);
$textRun->getFont()->setColor($colorBlack);

////********************************************Final Portada

///*****************************Crear Hojas con las imagenes
foreach($array_fotos as $fotos ){
	
	
	if(file_exists('../../../'.$fotos['imagen']))//Si existe la imagen para descargar
	{
		//******Fecha de la foto
		$fecha_foto=explode('-',$fotos['fecha']);
		
		$mes_foto=$manager->mesNombre($fecha_foto[1]);
		
		$fecha_txt=$fecha_foto[0].' de '.$mes_foto.' '.$fecha_foto[2];
		//*************************
		
		$currentSlide = createTemplatedSlide($objPHPPowerPoint); // local function
	
		//*************Titulo de la hoja
		$shape = $currentSlide->createRichTextShape();
		$shape->setHeight(100)
			  ->setWidth(930)
			  ->setOffsetX(10)
			  ->setOffsetY(50);
		$shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_RIGHT);
		
		$titulo_slide=$fotos['nombreTienda'];
		if($id_marca==1 || $id_marca==77 || $id_marca==78 || $id_marca==79 || $id_marca==80 || $id_marca==81)
		{
			$query_mar="select m.nombre from photoCatalogo pho 
			inner join Marca m on (pho.id_marca=m.idMarca)
			where pho.idphotoCatalogo='".$fotos['idphotoCatalogo']."'";
			
			$rs_mar=$manager->ejecutarConsulta($query_mar);
			
			$dat_mar=mysqli_fetch_array($rs_mar);
			
			$titulo_slide=$fotos['nombre']." ".$dat_mar['nombre'];
		}
		if($id_marca==53)
		{
			$oBkgImage = new Image();
			$oBkgImage->setPath('../imagenes/ibarra_back.png');
			$currentSlide->setBackground($oBkgImage);
			}
		$textRun = $shape->createTextRun($titulo_slide);
		$textRun->getFont()->setBold(true)
						   ->setSize(30)
						   ->setColor($colorBlack);
						   
	
		$shape->createParagraph()->createTextRun('Id Photo: '.$fotos['idphotoCatalogo']);
		$shape->createParagraph()->createTextRun('Exhibicion: '.$fotos['nombre']);
		$shape->createParagraph()->createTextRun('Tienda: '.$fotos['nombreTienda']);
		$shape->createParagraph()->createTextRun('Promotor: '.$fotos['nombrePromo']);
		$shape->createParagraph()->createTextRun('Supervisor: '.$fotos['nombreSuper']);
		$shape->createParagraph()->createTextRun('Fecha: '.$fecha_txt);
		$shape->createParagraph()->createTextRun('Tipo Promotor: '.$fotos['nombreTipo']);
		
		
		///***********Imagen
	
		// Create a shape (text)
		$shape = $currentSlide->createRichTextShape();
		$shape->setHeight(600);
		$shape->setWidth(930);
		$shape->setOffsetX(10);
		$shape->setOffsetY(400);
		$shape->getActiveParagraph()->getAlignment()->setHorizontal( Alignment::HORIZONTAL_LEFT ); 
		
		$shape = $currentSlide->createDrawingShape();
		$shape->setName('FotoCat')
			->setDescription('Foto')
			->setPath('../../../'.$fotos['imagen'])
			->setHeight(350)
			->setOffsetX(250)
			->setOffsetY(150);
		$shape->getShadow()->setVisible(true)
			->setDirection(45)
			->setDistance(10);
			
			
		///*********************Si son marcas de Cliente HENKEL**** Logo Slide ******
		if($id_marca==1 || $id_marca==77 || $id_marca==78 || $id_marca==79 || $id_marca==80 || $id_marca==81)
		{
			$shape = $currentSlide->createRichTextShape();
			$shape->setHeight(50)
				  ->setWidth(930)
				  ->setOffsetX(10)
				  ->setOffsetY(670);
			$shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_CENTER);
			
			$textRun = $shape->createTextRun($mes.' Henkel Beauty Care');
			$textRun->getFont()->setBold(true)
							   ->setSize(13)
							   ->setColor($colorRed);
									   
			$shape = $currentSlide->createDrawingShape();
			
			$shape->setName('FotoCat')
			->setDescription('Foto')
			->setPath('../imagenes/logos/'.$datos_marca['nombre'].'.png')
			->setHeight(40)
			->setOffsetX(860)
			->setOffsetY(670);
			$shape->getShadow()->setVisible(true)
			->setDirection(45)
			->setDistance(10);
			
			}	
		else
		{	
			if(file_exists('../imagenes/logos/'.$datos_marca['nombre'].'.png'))
			{
				$shape = $currentSlide->createDrawingShape();
				$shape->setName('FotoCat')
				->setDescription('Foto')
				->setPath('../imagenes/logos/'.$datos_marca['nombre'].'.png')
				->setHeight(150)
				->setOffsetX(600)
				->setOffsetY(480);
				$shape->getShadow()->setVisible(true)
				->setDirection(45)
				->setDistance(10);
				
				}
		}/// **** Final de logo marca en slide
	}
}//Final del foreach imagenes

////*****************************************Final Imagenes


// Save file
$escribio=write($objPHPPowerPoint, basename($datos_marca['nombre'].' '.$titulo, '.php'), $writers);

if($escribio=='11')
{
$path=utf8_encode($datos_marca['nombre'].' '.$titulo);

$respuesta=$path;
echo $respuesta;
}
else
{
	$respuesta='0';
	echo $respuesta;
	}

