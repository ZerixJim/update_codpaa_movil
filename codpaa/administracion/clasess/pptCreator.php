<?php

use PhpOffice\PhpPresentation\PhpPresentation;
use PhpOffice\PhpPresentation\Slide\Background\Image;
use PhpOffice\PhpPresentation\Style\Alignment;
use PhpOffice\PhpPresentation\Style\Color;


include_once '../clasess/Sample_Header2.php';

include_once('../connexion/bdManager.php');

$manager = new bdManager();

$colorBlack = new Color('FF000000');

$colorRed = new Color('FF0000');




///***************Recepcion de datos

$array_fotos = $_REQUEST['dataFoto'];

$fecha_ini = explode('-', $_REQUEST['fechaIni']);

$id_marca = $_REQUEST['idMarca'];

////********************************************

$mes = $manager->mesNombre($fecha_ini[1]);

$query_marca = "SELECT nombre,idMarca FROM Marca WHERE idMarca='" . $id_marca . "'";
$resul_marca = $manager->ejecutarConsulta($query_marca);
$datos_marca = mysqli_fetch_array($resul_marca);

$titulo = $mes . ' ' . $fecha_ini[2];

if ($id_marca == 1 || $id_marca == 77 || $id_marca == 78 || $id_marca == 79 || $id_marca == 80 || $id_marca == 81) {
    $datos_marca['nombre'] = "HENKEL";
}

// Create new PHPPowerPoint object
$objPHPPowerPoint = new PhpPresentation();

// Set properties
$objPHPPowerPoint->getProperties()->setCreator('CODPAA')
    ->setLastModifiedBy('CODPAA')
    ->setTitle('Fotografico ' . $titulo)
    ->setSubject($datos_marca['nombre'])
    ->setDescription('Fotografico Creado por Codpaa Web')
    ->setKeywords('office 2007')
    ->setCategory('Presentacion');

// Remove first slide
$objPHPPowerPoint->removeSlideByIndex(0);

///****************************************Crear Portada************************************//
// local function
$currentSlide = createTemplatedSlide($objPHPPowerPoint);

if (file_exists('../imagenes/logos/' . $datos_marca['idMarca'] . '.png')) {
    if ($id_marca == 1 || $id_marca == 77 || $id_marca == 78 || $id_marca == 79 || $id_marca == 80 || $id_marca == 81) {
        $datos_marca['idMarca'] = "HENKEL";
    }
    $shape = $currentSlide->createDrawingShape();
    $shape->setName('FotoCat')
        ->setDescription('Foto')
        ->setPath('../imagenes/logos/' . $datos_marca['idMarca'] . '.png')
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
$shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_LEFT);


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
foreach ($array_fotos as $fotos) {

    //Si existe la imagen para descargar
    if (file_exists('../../../' . $fotos['imagen'])) {
        //******Fecha de la foto
        $fecha_foto = explode('-', $fotos['fecha']);

        $mes_foto = $manager->mesNombre($fecha_foto[1]);

        $fecha_txt = $fecha_foto[0] . ' de ' . $mes_foto . ' ' . $fecha_foto[2];
        //*************************

        $currentSlide = createTemplatedSlide($objPHPPowerPoint); // local function

        //*************Titulo de la hoja
        $shape = $currentSlide->createRichTextShape();


        $titulo_slide = $fotos['nombreTienda'];
        if ($id_marca == 1 || $id_marca == 77 || $id_marca == 78 || $id_marca == 79 || $id_marca == 80 || $id_marca == 81) {
            $query_mar = "SELECT m.nombre FROM photoCatalogo pho
			INNER JOIN Marca m ON (pho.id_marca=m.idMarca)
			WHERE pho.idphotoCatalogo='" . $fotos['idphotoCatalogo'] . "'";

            $rs_mar = $manager->ejecutarConsulta($query_mar);

            $dat_mar = mysqli_fetch_array($rs_mar);

            $titulo_slide = $fotos['nombre'] . " " . $dat_mar['nombre'];
        }

        //Ibarra
        if ($id_marca == 53) {
            $oBkgImage = new Image();
            $oBkgImage->setPath('../imagenes/ibarra_back.png');
            $currentSlide->setBackground($oBkgImage);
        }

        //Choco choco
        if ($id_marca == 87) {
            $oBkgImage = new Image();
            $oBkgImage->setPath('../imagenes/choco_back.jpg');
            $currentSlide->setBackground($oBkgImage);


        }
        //Splenda
        if ($id_marca == 121) {
            $oBkgImage = new Image();
            $oBkgImage->setPath('../imagenes/splenda_back.jpg');
            $currentSlide->setBackground($oBkgImage);

        }
        //************Si son marcas de Autoservicio Compartido(Body,Puro,SanG,Rosa,Gust,Sabro,Huichol)


        if($logo == true){

            $shape = $currentSlide->createDrawingShape();
            $shape->setName('VangLogo')
                ->setDescription('Vanguardia')
                ->setPath('../imagenes/LogoSVSsinfondo(opacity58).png')
                ->setHeight(40)
                ->setOffsetX(40)
                ->setOffsetY(670);
            $shape->getShadow()->setVisible(true)
                ->setDirection(45)
                ->setDistance(10);



        }




        ///***********Imagen

        // Create a shape (text)
        $shape = $currentSlide->createRichTextShape();
        $shape->setHeight(600);
        $shape->setWidth(930);
        $shape->setOffsetX(10);
        $shape->setOffsetY(400);
        $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_LEFT);

        $shape = $currentSlide->createDrawingShape();
        $shape->setName('FotoCat')
            ->setDescription('Foto')
            ->setPath('../../../' . $fotos['imagen'])
            ->setHeight(350)
            ->setOffsetX(260)
            ->setOffsetY(150);
        $shape->getShadow()->setVisible(true)
            ->setDirection(45)
            ->setDistance(10);


        ///*********************Si son marcas de Cliente HENKEL**** Logo Slide ******
        if ($id_marca == 1 || $id_marca == 77 || $id_marca == 78 || $id_marca == 79 || $id_marca == 80 || $id_marca == 81) {
            $shape = $currentSlide->createRichTextShape();
            $shape->setHeight(50)
                ->setWidth(930)
                ->setOffsetX(10)
                ->setOffsetY(670);
            $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_CENTER);

            $textRun = $shape->createTextRun($mes . ' Henkel Beauty Care');
            $textRun->getFont()->setBold(true)
                ->setSize(13)
                ->setColor($colorRed);

            $shape = $currentSlide->createDrawingShape();

            $shape->setName('FotoCat')
                ->setDescription('Foto')
                ->setPath('../imagenes/logos/' . $datos_marca['nombre'] . '.png')
                ->setHeight(40)
                ->setOffsetX(860)
                ->setOffsetY(670);
            $shape->getShadow()->setVisible(true)
                ->setDirection(45)
                ->setDistance(10);

        } else {
            if (file_exists('../imagenes/logos/' . $datos_marca['idMarca'] . '.png')) {
                $shape = $currentSlide->createDrawingShape();
                $shape->setName('FotoCat')
                    ->setDescription('Foto')
                    ->setPath('../imagenes/logos/' . $datos_marca['idMarca'] . '.png')
                    ->setHeight(120)
                    ->setOffsetX(40)
                    ->setOffsetY(50);
                $shape->getShadow()->setVisible(true)
                    ->setDirection(45)
                    ->setDistance(10);

            }
        }/// **** Final de logo marca en slide
    }
}//Final del foreach imagenes

////*****************************************Final Imagenes


// Save file
$escribio = write($objPHPPowerPoint, basename($datos_marca['nombre'] . ' ' . $titulo, '.php'), $writers);

if ($escribio == '11') {
    $path = $datos_marca['nombre'] . ' ' . $titulo;

    $respuesta = $path;
    echo $respuesta;
} else {
    $respuesta = '0';
    echo $respuesta;
}

