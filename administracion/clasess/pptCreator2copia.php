<?php

use PhpOffice\PhpPresentation\PhpPresentation;
use PhpOffice\PhpPresentation\Style\Alignment;
use PhpOffice\PhpPresentation\Style\Color;


include_once '../clasess/Sample_Header2.php';

include_once('../connexion/bdManager.php');
 

if (isset($_POST['dataFoto']) && isset($_POST['fechaIni']) && isset($_POST['idMarca'])) {


    $manager = new bdManager();

    $colorBlack = new Color('FF000000');
    $colorRed = new Color('FF0000');
    $fontName = "Lucida Console";


    //*** Settings
    $nombrePromotor = $_POST['chkPromotor'];
    $logo = $_POST['logo'];
    $fechaLabel = $_POST['chkFecha'];
    $estadoLabel = $_POST['chkEstado'];

    $nEconominco = $_POST['chkEconomico'];

    $comments = $_REQUEST['comments'];

    $brandSelected = $_REQUEST['brand'];
    
    $asesorSumma = $_REQUEST['asesorSumma'];

///***************Recepcion de datos

    $array_fotos = json_decode($_POST['dataFoto']);
    $fecha_ini = explode('-', $_POST['fechaIni']);
    $id_marca = $_POST['idMarca'];

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
    
    
    //Configuraci贸n de tama帽o de las diapositivas
    $LAYOUT_A3 = 'A3';
    
    $objPHPPowerPoint->getLayout()->setDocumentLayout($LAYOUT_A3);
    
    //FONDOS DE IMÁGENES
    $strFondoFoto = "";
    $strFondoInfo = "";
    $strFondoComen = "";
    $strFondoPortada = "";
    
    if($id_marca == 249){
        $strFondoFoto = "fondo-ppt-clorox-2.png";
        $strFondoInfo = "fondo-ppt-info.png";
        $strFondoComen = "fondo-ppt-comen.png";
        $strFondoPortada = "FONDO-PORTADA.png";
    }
    else if($id_marca == 42){
        $strFondoFoto = "fondo-ppt-dlr.png";
        $strFondoInfo = "fondo-ppt-info-dlr.png";
        $strFondoComen = "fondo-ppt-comen-dlr.png";
        $strFondoPortada = "fondo-de-la-rosa.png";
    }
    else{
        $strFondoFoto = "fondo-ppt-default.png";
        $strFondoInfo = "fondo-ppt-info-default.png";
        $strFondoComen = "fondo-ppt-comen-default.png";
        $strFondoPortada = "fondo-vanguardia.png";
        $colorBlack = new Color('FFFFFF');
    }
    
    
    

///****************************************Crear Portada************************************//

    $currentSlide = createTemplatedSlide($objPHPPowerPoint); // local function
    
    //FONDO DE LA PORTADA
    $shape = $currentSlide->createDrawingShape();
    $shape->setName('FondoPortada')
        ->setDescription('FotoPortada')
        ->setPath('../imagenes/'.$strFondoPortada)
        ->setResizeProportional(false)
        ->setHeight(1121)
        ->setWidth(1587)
        ->setOffsetX(0)
        ->setOffsetY(0);


    /*
     * check if brand is selected
     * */

   if ($brandSelected == 'true'){

        //checking image available
         

        if (file_exists('../images/marcas/' . $datos_marca['idMarca'] . '.png')) {
            if ($id_marca == 1 || $id_marca == 77 || $id_marca == 78 || $id_marca == 79 || $id_marca == 80 || $id_marca == 81) {
                $datos_marca['idMarca'] = "HENKEL";
                $shape = $currentSlide->createDrawingShape();
                $shape->setName('FotoCat')
                    ->setDescription('Foto')
                    ->setPath('../images/marcas/' . $datos_marca['idMarca'] . '.png')
                    ->setHeight(200)
                    ->setOffsetX(500)
                    ->setOffsetY(70);
                $shape->getShadow()->setVisible(true)
                    ->setDirection(45)
                    ->setDistance(10);


            } else {
                $shape = $currentSlide->createDrawingShape();
                $shape->setName('FotoCat')
                    ->setDescription('Foto')
                    ->setPath('../images/marcas/' . $datos_marca['idMarca'] . '.png')
                    ->setHeight(130)
                    ->setOffsetX(700)
                    ->setOffsetY(800);
                $shape->getShadow()->setVisible(true)
                    ->setDirection(45)
                    ->setDistance(10);
            }

        }



    }


// Create a shape (text)
    $shape = $currentSlide->createRichTextShape();
    $shape->setHeight(200);
    $shape->setWidth(1587);
    $shape->setOffsetX(0);
    $shape->setOffsetY(400);
    $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_CENTER);


    $textRun = $shape->createTextRun('REPORTE DE FOTOS');
    $textRun->getFont()->setBold(true);
    $textRun->getFont()->setSize(30);
    $textRun->getFont()->setColor($colorBlack)->setName($fontName);

    $shape->createBreak();

    $textRun = $shape->createTextRun($titulo);
    $textRun->getFont()->setBold(true);
    $textRun->getFont()->setSize(70);
    $textRun->getFont()->setColor($colorBlack)->setName($fontName);
//***********************Si es marca ****************************/
    if ($logo == 'true') {
        $shape = $currentSlide->createDrawingShape();

        $shape->setName('VangLogo')
            ->setDescription('Vanguardia')
            ->setPath('../imagenes/LogoVang.png')
            ->setHeight(250)
            ->setOffsetX(350)
            ->setOffsetY(0);
    }

////********************************************Final Portada**********************************/

/***************************Crear Hojas con las imagenes*****************************************/
    
    $i=0;
    $width_fondo = 0;
    $recolectorfotos = array();
    $contador=0;
    $sizeArray = count($array_fotos);
    $counter = 0;
    
    //VALORES DE POSICIONES DE FOTOS
    $posFondoX = 70;
    $posFondoInfoX = 95;
    $posFondoComenX = 95;
    $posFotoX = 200;
    $posInfoX = 95;
    $posComenX = 90;
    
    foreach ($array_fotos as $fotos) {
        $counter = $counter + 1;
        //Si existe la imagen para descargar
        if (file_exists('../../../' . $fotos->imagen)) {
           
            array_push($recolectorfotos, $fotos);
            
            $contador = count($recolectorfotos);
            
            //******Fecha de la foto
            $dateTime = new DateTime($fotos->fecha);

            $fecha_foto = explode('-', $dateTime->format('d-m-Y'));

            $mes_foto = $manager->mesNombre($fecha_foto[1]);
            $fecha_txt = $fecha_foto[0] . ' de ' . $mes_foto . ' ' . $fecha_foto[2];
            //*************************
            //**$currentSlide = createTemplatedSlide($objPHPPowerPoint); // local function
            
           
            if($contador == 2 || $counter == $sizeArray){
                
                $currentSlide = createTemplatedSlide($objPHPPowerPoint); // local function
                
                for($i=0; $i<$contador; $i++){
                    
                    $fotos2 = $recolectorfotos[$i];
                    
                    if($i == 0){
                        //VALORES DE POSICIONES DE FOTOS
                        $posFondoX = 70;
                        $posFondoInfoX = 95;
                        $posFondoComenX = 95;
                        $posFotoX = 200;
                        $posInfoX = 95;
                        $posComenX = 90;
                    }
                    else{
                        $posFondoX = 812;
                        $posFondoInfoX = 837;
                        $posFondoComenX = 837;
                        $posFotoX = 942;
                        $posInfoX = 837;
                        $posComenX = 837;
                    }
                
                    //****FONDO DE LA FOTO*****/
                    $shape = $currentSlide->createDrawingShape();
                    $shape->setName('FondoFoto')
                        ->setDescription('Fondo')
                        ->setPath('../imagenes/'.$strFondoFoto)
                        ->setResizeProportional(false)
                        ->setHeight(922)
                        ->setWidth(702)
                        ->setOffsetX($posFondoX)
                        ->setOffsetY(90);
                    //**************************
            
                    /**************FOTO*****************/
                    $shape = $currentSlide->createDrawingShape();
                    $shape->setName('FotoCat')
                        ->setDescription('Foto')
                        ->setPath('../../../' . $fotos2->imagen)
                        ->setResizeProportional(false)
                        ->setHeight(471)
                        ->setWidth(432)
                        ->setOffsetX($posFotoX)
                        ->setOffsetY(105);
                    $shape->getShadow()->setVisible(true)
                        ->setDirection(45)
                        ->setDistance(10);
                     //***********************************
                     
                     /*************FONDO INFO**************/
                    $shape = $currentSlide->createDrawingShape();
                    $shape->setName('FondoInfo')
                        ->setDescription('Fondo')
                        ->setPath('../imagenes/'.$strFondoInfo)
                        ->setResizeProportional(false)
                        ->setHeight(165)
                        ->setWidth(650)
                        ->setOffsetX($posFondoInfoX)
                        ->setOffsetY(586);
                     //*************************************
             
                     /*************FONDO COMENTARIO**************/
                    $shape = $currentSlide->createDrawingShape();
                    $shape->setName('FondoInfo')
                        ->setDescription('Fondo')
                        ->setPath('../imagenes/'.$strFondoComen)
                        ->setResizeProportional(false)
                        ->setHeight(220)
                        ->setWidth(650)
                        ->setOffsetX( $posFondoComenX)
                        ->setOffsetY(760);
                     //*************************************/
        
                    //*************Titulo de la hoja
                    $shape = $currentSlide->createRichTextShape();
        
                    $titulo_slide = $fotos2->nombreTienda;

            
                    if ($id_marca > 0) {
                        $shape->setHeight(200)
                            ->setWidth(650)
                            ->setOffsetX($posInfoX)
                            ->setOffsetY(570);
                        $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_CENTER);
                        
                        //$textDat = $shape->createParagraph()->createTextRun('Tienda: '. $fotos->cadena .' '.$fotos->nombreTienda);
                        //$textDat->getFont()->setSize(15)->setColor($colorBlack);
        
                        $textDat = $shape->createParagraph()->createTextRun($fotos2->nombreTienda);
                        $textDat->getFont()->setSize(13)->setColor($colorBlack)->setName($fontName)->setBold(true);
        
        
                        $textDat = $shape->createParagraph()->createTextRun(strtoupper($fotos2->nombre)); //Exhibición
                        $textDat->getFont()->setSize(13)->setName($fontName)
                            ->setColor($colorBlack);
        
                        if ($nombrePromotor == 'true'){
        
                            $textDat = $shape->createParagraph()->createTextRun($fotos2->nombrePromo);
                            $textDat->getFont()->setSize(13)->setName($fontName)
                                ->setColor($colorBlack);
        
                        }
        
        
        
        
                        if ($fechaLabel == 'true'){
        
                            $textDat = $shape->createParagraph()->createTextRun($fecha_txt);
                            $textDat->getFont()->setSize(13)->setName($fontName)
                                ->setColor($colorBlack);
                        }
                        
                        
                        if($asesorSumma == 'true'){
        
                            $textDat = $shape->createParagraph()->createTextRun('Asesor: ' . $fotos2->asesor);
                            $textDat->getFont()->setSize(13)->setName($fontName)
                                ->setColor($colorBlack);
        
                        }
        
        
        
                        //if($fotos->asesor != null){
        
                            //$textDat = $shape->createParagraph()->createTextRun('Asesor: ' . $fotos->asesor);
                            //$textDat->getFont()->setSize(15)
                                //->setColor($colorBlack);
        
                        //}
        
                        $query_tien = "SELECT mt.cadena,mt.idEstado, es.nombre, mt.numeroEconomico FROM photoCatalogo ph
                                        INNER JOIN maestroTiendas mt ON (mt.idTienda=ph.id_tienda)
                                        INNER JOIN estados as es on (es.id=mt.idEstado)
                                        WHERE ph.idphotoCatalogo='" . $fotos2->idphotoCatalogo . "'";
        
        
                        $rs_est = $manager->ejecutarConsulta($query_tien);
                        $dat_est = mysqli_fetch_array($rs_est);
        
                        if ($estadoLabel == 'true'){
        
        
        
                            $textDat = $shape->createParagraph()->createTextRun(strtoupper($dat_est['nombre']));
                            $textDat->getFont()->setBold(false)->setName($fontName)
                                ->setSize(13)
                                ->setColor($colorBlack);
        
        
                        }
        
                        if ($nEconominco == 'true'){
        
        
                            //numero economico
                            $textDat = $shape->createParagraph()->createTextRun($dat_est['numeroEconomico']);
                            $textDat->getFont()->setSize(13)->setName($fontName);
                        }
                    }
                    
                    if($logo == 'true'){
                        //En la primera vuelta del for
                        if($i == 0){
                            $shape = $currentSlide->createDrawingShape();
                            $shape->setName('VangLogo')
                                ->setDescription('Vanguardia')
                                ->setPath('../imagenes/LogoVang.png')
                                ->setHeight(99)
                                ->setWidth(160)
                                ->setOffsetX(40)
                                ->setOffsetY(20);
                        }
                    }
                    
            /*****************************************************************************/
                    /*$shape = $currentSlide->createRichTextShape();
                    $shape->setHeight(600);
                    $shape->setWidth(930);
                    $shape->setOffsetX(10);
                    $shape->setOffsetY(400);
                    $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_LEFT);*/
                    
                    if ($comments == 'true'){
        
                        $shape = $currentSlide->createRichTextShape();
        
                        $shape->setHeight(200)
                            ->setWidth(650)
                            ->setOffsetX($posInfoX)
                            ->setOffsetY(733);
                        $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_LEFT);
        
                        $textDat = $shape->createParagraph()->createTextRun("COMENTARIOS: " . $fotos2->comentarios . " ". $fotos2->comentariosGenerales);
        
                        $textDat->getFont()
                            ->setSize(12)
                            ->setName($fontName)
                            ->setColor($colorBlack);
                    }
                    
                    if ($brandSelected == 'true'){
                        
                      if($i == 0){
                          if ($id_marca == 1 || $id_marca == 77 || $id_marca == 78 || $id_marca == 79 || $id_marca == 80 || $id_marca == 81) {
                            $shape = $currentSlide->createRichTextShape();
                            $shape->setHeight(50)
                                ->setWidth(930)
                                ->setOffsetX(10)
                                ->setOffsetY(640);
                            $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_CENTER | Alignment::HORIZONTAL_JUSTIFY);
        
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
                          }
                          else {
                              if (file_exists('../images/marcas/' . $datos_marca['idMarca'] . '.png')) {
                                    $shape = $currentSlide->createDrawingShape();
                                    $shape->setName('FotoCat')
                                        ->setDescription('Foto')
                                        ->setPath('../images/marcas/' . $datos_marca['idMarca'] . '.png')
                                        ->setHeight(50)
                                        ->setOffsetX(1410)
                                        ->setOffsetY(20);
                                    $shape->getShadow()->setVisible(true)
                                        ->setDirection(280)
                                        ->setDistance(8);
            
                                }
                            }//  Final de logo marca en slide
                      }
                    }
                    
                    
                }
                
                $recolectorfotos = array();
            
            }

        }
    }//Final del foreach imagenes

////*****************************************Final Imagenes


// Save file
    $escribio = write($objPHPPowerPoint, basename($datos_marca['nombre'] . ' ' . $titulo, '.php'), $writers);


    if ($escribio == '11') {
        $path = $datos_marca['nombre'] . ' ' . $titulo;

        $respuesta = $path;

        echo json_encode(array('success' => true, 'ruta' => $respuesta), JSON_NUMERIC_CHECK | JSON_PARTIAL_OUTPUT_ON_ERROR | JSON_PRETTY_PRINT);

    } else {


        echo json_encode(array('success' => false), JSON_NUMERIC_CHECK | JSON_PRETTY_PRINT | JSON_PARTIAL_OUTPUT_ON_ERROR);
    }


} else {
    echo "no hay datos";
}

