<?php

namespace classes;

use ConexionPDO;
use DateTime;
use PhpOffice\PhpPresentation\PhpPresentation;
use PhpOffice\PhpPresentation\Style\Alignment;
use PhpOffice\PhpPresentation\Style\Color;



class PptCreator{


    static function deleteAtTime(){

        $dir = opendir('../../archivos/pptxreporter/');

        while($f = readdir($dir)){

            try{

                $horas48 = 3600*24*1;

                if((time()-filemtime('../../archivos/pptxreporter/'.$f) > $horas48) and !(is_dir('../../archivos/pptxreporter/'.$f)));


            }catch (\Exception $e){

                echo $e;

            }



        }



        closedir($dir);


    }

    static function createPpt($fecha, $idMarca, $array, $grupo){

        self::deleteAtTime();

        $manager = ConexionPDO::getInstance()->getDB();

        $colorBlack = new Color('FF000000');
        $colorRed = new Color('FF0000');


        //*** Settings
        $nombrePromotor = 'true';
        $logo = 'true';
        $fechaLabel = 'true';
        $estadoLabel = 'true';

        $nEconominco = 'true';


        $brandSelected = 'true';

        ///***************Recepcion de datos

        $array_fotos = $array;
        $id_marca = $idMarca;

        $mes = date('Y-m-d', strtotime($fecha));

        ////********************************************


        $titulo = $mes;


        // Create new PHPPowerPoint object
        $objPHPPowerPoint = new PhpPresentation();

        // Set properties
        $objPHPPowerPoint->getProperties()->setCreator('CODPAA')
            ->setLastModifiedBy('CODPAA')
            ->setTitle('Fotografico ' . $titulo)
            ->setSubject('Helix')
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

        if ($brandSelected == 'true') {

            /*
             * checking image available
             *
             * */

            if (file_exists('../images/marcas/' . $idMarca. '.png')) {
                $shape = $currentSlide->createDrawingShape();
                $shape->setName('FotoCat')
                    ->setDescription('Foto')
                    ->setPath('../images/marcas/' . $idMarca . '.png')
                    ->setHeight(200)
                    ->setOffsetX(400)
                    ->setOffsetY(100);
                $shape->getShadow()->setVisible(true)
                    ->setDirection(45)
                    ->setDistance(10);

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
                ->setPath('../../imagenes/LogoSVSsinfondo(opacity58).png')
                ->setHeight(125)
                ->setOffsetX(40)
                ->setOffsetY(500);
        }

        ////********************************************Final Portada

        ///*****************************Crear Hojas con las imagenes
        foreach ($array_fotos as $fotos) {

            //Si existe la imagen para descargar


            if (file_exists('../../../../' . $fotos->imagen)) {

                //******Fecha de la foto
                $dateTime = new DateTime($fotos->fecha);

                $fecha_txt = $dateTime->format('d-m-Y');
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


                    $textDat = $shape->createParagraph()->createTextRun('Tienda: ' . $fotos->cadena . ' ' . $fotos->nombreTienda);
                    $textDat->getFont()->setSize(15)->setColor($colorBlack);


                    $textDat = $shape->createParagraph()->createTextRun('Exhibicion: ' . $fotos->nombre);
                    $textDat->getFont()->setSize(15)
                        ->setColor($colorBlack);

                    if ($nombrePromotor == 'true') {

                        $textDat = $shape->createParagraph()->createTextRun('Promotor: ' . $fotos->nombrePromo);
                        $textDat->getFont()->setSize(15)
                            ->setColor($colorBlack);

                    }


                    if ($fechaLabel == 'true') {

                        $textDat = $shape->createParagraph()->createTextRun('Fecha: ' . $fecha_txt);
                        $textDat->getFont()->setSize(15)
                            ->setColor($colorBlack);
                    }


                   /* if ($fotos->asesor != null) {

                        $textDat = $shape->createParagraph()->createTextRun('Asesor: ' . $fotos->asesor);
                        $textDat->getFont()->setSize(15)
                            ->setColor($colorBlack);

                    }*/


                    if ($fotos->comentario != null){

                        $textDat = $shape->createParagraph()->createTextRun('Comentario foto: ' . $fotos->comentario);
                        $textDat->getFont()->setSize(15)
                            ->setBold(true)
                            ->setColor($colorBlack);


                    }


                    if ($fotos->comentarioGeneral != null){

                        $textDat = $shape->createParagraph()->createTextRun('Comentario Gral: ' . $fotos->comentarioGeneral);
                        $textDat->getFont()->setSize(15)
                            ->setBold(true)
                            ->setColor($colorBlack);


                    }



                    $query_tien = "SELECT mt.cadena,mt.idEstado, es.nombre, mt.numeroEconomico FROM photoCatalogo ph
                                INNER JOIN maestroTiendas mt ON (mt.idTienda=ph.id_tienda)
                                INNER JOIN estados as es on (es.id=mt.idEstado)
                                WHERE ph.idphotoCatalogo= :idPhoto";



                    $sentense = $manager->prepare($query_tien);

                    $sentense->bindParam(':idPhoto', $fotos->idphotoCatalogo, \PDO::PARAM_INT);
                    $sentense->execute();


                    $dat_est = $sentense->fetch(\PDO::FETCH_ASSOC);



                    if ($estadoLabel == 'true') {


                        $textDat = $shape->createParagraph()->createTextRun('Estado: ' . $dat_est['nombre']);
                        $textDat->getFont()->setBold(true)
                            ->setSize(15)
                            ->setColor($colorBlack);


                    }

                    if ($nEconominco == 'true') {


                        /*numero economico*/
                        $textDat = $shape->createParagraph()->createTextRun('N.Economico ' . $dat_est['numeroEconomico']);
                        $textDat->getFont()->setSize(12);
                    }


                    if ($logo == 'true') {

                        $shape = $currentSlide->createDrawingShape();
                        $shape->setName('VangLogo')
                            ->setDescription('Vanguardia')
                            ->setPath('../../imagenes/LogoSVSsinfondo(opacity58).png')
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
                    ->setPath('../../../../' . $fotos->imagen)
                    ->setHeight(350)
                    ->setOffsetX(260)
                    ->setOffsetY(200);
                $shape->getShadow()->setVisible(true)
                    ->setDirection(45)
                    ->setDistance(10);



                /*
                 *
                 * comments
                 *
                 */

                $shape = $currentSlide->createRichTextShape();

                $shape->setHeight(50)
                    ->setWidth(930)
                    ->setOffsetX(10)
                    ->setOffsetY(520);
                $shape->getActiveParagraph()->getAlignment()->setHorizontal(Alignment::HORIZONTAL_CENTER);

                $textDat = $shape->createParagraph()->createTextRun("Comentarios ". $fotos->comentarios . " ". $fotos->comentariosGenerales);
                $textDat->getFont()
                    ->setSize(16)
                    ->setBold(true);


                ///*********************Si son marcas de Cliente HENKEL**** Logo Slide ******
                ///
                /// brand selected


            }
        }//Final del foreach imagenes

        ////*****************************************Final Imagenes

        $writers = array('PowerPoint2007' => 'pptx');

        // Save file
        $escribio = write($objPHPPowerPoint, 'helix' . $fecha .'_'.$grupo, $writers);


        if ($escribio != -1) {

            return array('success' => true, 'ruta' => $escribio);

        } else {

            return array('success' => false);
        }


    }


}

