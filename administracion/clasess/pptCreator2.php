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

///****************************************Crear Portada************************************//

    $currentSlide = createTemplatedSlide($objPHPPowerPoint); // local function


    /*
     * check if brand is selected
     * */

    if ($brandSelected == 'true'){

        /*
         * checking image available
         *
         * */

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
                    ->setHeight(200)
                    ->setOffsetX(400)
                    ->setOffsetY(100);
                $shape->getShadow()->setVisible(true)
                    ->setDirection(45)
                    ->setDistance(10);
            }

        }



    }




// Create a shape (text)
    $shape = $currentSlide->createRichTextShape();
    $shape->setHeight(200);
    $shape->setWidth(600);
    $shape->setOffsetX(10);
    $shape->setOffsetY(350);
    $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_LEFT);


    $textRun = $shape->createTextRun('Fotografico');
    $textRun->getFont()->setBold(true);
    $textRun->getFont()->setSize(30);
    $textRun->getFont()->setColor($colorBlack);

    $shape->createBreak();

    $textRun = $shape->createTextRun($titulo);
    $textRun->getFont()->setBold(true);
    $textRun->getFont()->setSize(70);
    $textRun->getFont()->setColor($colorBlack);
//**************Si es marca Henkel
    if ($logo == 'true') {
        $shape = $currentSlide->createDrawingShape();

        $shape->setName('VangLogo')
            ->setDescription('Vanguardia')
            ->setPath('../imagenes/LogoSVSsinfondo(opacity58).png')
            ->setHeight(125)
            ->setOffsetX(40)
            ->setOffsetY(500);
    }

////********************************************Final Portada

///*****************************Crear Hojas con las imagenes
    foreach ($array_fotos as $fotos) {

        //Si existe la imagen para descargar
        if (file_exists('../../../' . $fotos->imagen)) {

            //******Fecha de la foto
            $dateTime = new DateTime($fotos->fecha);

            $fecha_foto = explode('-', $dateTime->format('d-m-Y'));

            $mes_foto = $manager->mesNombre($fecha_foto[1]);
            $fecha_txt = $fecha_foto[0] . ' de ' . $mes_foto . ' ' . $fecha_foto[2];
            //*************************
            $currentSlide = createTemplatedSlide($objPHPPowerPoint); // local function

            //*************Titulo de la hoja
            $shape = $currentSlide->createRichTextShape();

            $titulo_slide = $fotos->nombreTienda;




            if ($id_marca > 0) {
                $shape->setHeight(100)
                    ->setWidth(930)
                    ->setOffsetX(10)
                    ->setOffsetY(35);
                $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_RIGHT);


                $textDat = $shape->createParagraph()->createTextRun('Tienda: '. $fotos->cadena .' '.$fotos->nombreTienda);
                $textDat->getFont()->setSize(15)->setColor($colorBlack);


                $textDat = $shape->createParagraph()->createTextRun('Exhibicion: ' . $fotos->nombre);
                $textDat->getFont()->setSize(15)
                    ->setColor($colorBlack);

                if ($nombrePromotor == 'true'){

                    $textDat = $shape->createParagraph()->createTextRun('Promotor: ' . $fotos->nombrePromo);
                    $textDat->getFont()->setSize(15)
                        ->setColor($colorBlack);

                }




                if ($fechaLabel == 'true'){

                    $textDat = $shape->createParagraph()->createTextRun('Fecha: ' . $fecha_txt);
                    $textDat->getFont()->setSize(15)
                        ->setColor($colorBlack);
                }
                
                
                if($asesorSumma == 'true'){

                    $textDat = $shape->createParagraph()->createTextRun('Asesor: ' . $fotos->asesor);
                    $textDat->getFont()->setSize(15)
                        ->setColor($colorBlack);

                }



                /*if($fotos->asesor != null){

                    $textDat = $shape->createParagraph()->createTextRun('Asesor: ' . $fotos->asesor);
                    $textDat->getFont()->setSize(15)
                        ->setColor($colorBlack);

                }*/








                $query_tien = "SELECT mt.cadena,mt.idEstado, es.nombre, mt.numeroEconomico FROM photoCatalogo ph
                                INNER JOIN maestroTiendas mt ON (mt.idTienda=ph.id_tienda)
                                INNER JOIN estados as es on (es.id=mt.idEstado)
                                WHERE ph.idphotoCatalogo='" . $fotos->idphotoCatalogo . "'";


                $rs_est = $manager->ejecutarConsulta($query_tien);
                $dat_est = mysqli_fetch_array($rs_est);

                if ($estadoLabel == 'true'){



                    $textDat = $shape->createParagraph()->createTextRun('Estado: ' .$dat_est['nombre']);
                    $textDat->getFont()->setBold(true)
                        ->setSize(15)
                        ->setColor($colorBlack);




                }

                if ($nEconominco == 'true'){


                    /*numero economico*/
                    $textDat = $shape->createParagraph()->createTextRun('N.Economico ' . $dat_est['numeroEconomico']);
                    $textDat->getFont()->setSize(12);
                }




                if($logo == 'true'){

                    $shape = $currentSlide->createDrawingShape();
                    $shape->setName('VangLogo')
                        ->setDescription('Vanguardia')
                        ->setPath('../imagenes/LogoSVSsinfondo(opacity58).png')
                        ->setHeight(50)
                        ->setOffsetX(40)
                        ->setOffsetY(640);
                }




            }
            $shape = $currentSlide->createRichTextShape();
            $shape->setHeight(600);
            $shape->setWidth(930);
            $shape->setOffsetX(10);
            $shape->setOffsetY(400);
            $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_LEFT);

            $shape = $currentSlide->createDrawingShape();
            $shape->setName('FotoCat')
                ->setDescription('Foto')
                ->setPath('../../../' . $fotos->imagen)
                ->setHeight(350)
                ->setOffsetX(260)
                ->setOffsetY(200);
            $shape->getShadow()->setVisible(true)
                ->setDirection(45)
                ->setDistance(10);


            if ($comments == 'true'){

                $shape = $currentSlide->createRichTextShape();

                $shape->setHeight(50)
                    ->setWidth(930)
                    ->setOffsetX(10)
                    ->setOffsetY(520);
                $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_CENTER);

                $textDat = $shape->createParagraph()->createTextRun("Comentarios: " . $fotos->comentarios . " ". $fotos->comentariosGenerales);

                $textDat->getFont()
                    ->setSize(16)
                    ->setBold(true);

            }


            ///*********************Si son marcas de Cliente HENKEL**** Logo Slide ******
            ///
            /// brand selected


            if ($brandSelected == 'true'){


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

                } else {
                    if (file_exists('../images/marcas/' . $datos_marca['idMarca'] . '.png')) {
                        $shape = $currentSlide->createDrawingShape();
                        $shape->setName('FotoCat')
                            ->setDescription('Foto')
                            ->setPath('../images/marcas/' . $datos_marca['idMarca'] . '.png')
                            ->setHeight(80)
                            ->setOffsetX(20)
                            ->setOffsetY(20);
                        $shape->getShadow()->setVisible(true)
                            ->setDirection(45)
                            ->setDistance(10);

                    }
                }/// **** Final de logo marca en slide




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

