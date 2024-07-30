<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 13/08/2018
 * Time: 10:18 AM
 */


/*ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);*/



ob_start();
session_start();


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');

    //$idSupervisor = $_REQUEST['Supervisor'];

    // $id_formato = $_REQUEST['idFormato'];

    $id_estado = $_REQUEST['idEstado'];

    $SemanaAsis = $_REQUEST['SemanaAsis'];

    $idProm = $_REQUEST['idPromotor'];




    /*if (!empty($SemanaAsis)){

        $countSemanas = count($SemanaAsis);
        $r = 0;

        if ($countSemanas > 0){

            $semanas .= "(";
            foreach ( $SemanaAsis as $semana ){

                if (is_numeric($semana)){

                    $semanas .= $semana;

                    if ($r != $countSemanas - 1 ){

                        $semanas .= ",";

                    }

                }

                $r++;

            }

            $semanas .= ")";

        }
    }*/


    $manager = DataBase::getInstance();

    /*

    $sql_sem = "SELECT semana,anio,idPromotor FROM supervisionRutas 
                WHERE idSupervision in " . $semanas ;



    $res_sem = $manager->ejecutarConsulta($sql_sem);*/

    ////*********************Recorre las fechas **********************//

    $diasSemana = array();


    $frozenColums = array();



    array_push($frozenColums, array("field"=>"idTienda", "title"=>"IDT", "sortable"=>true));
    array_push($frozenColums, array("field"=>"logo", "title"=>"logo", "sortable"=>true, "formatter"=>"tiendaFormatter"));
    array_push($frozenColums, array("field"=> "tienda", "title"=>"Tienda", "sortable"=> true));


    // columns days generator



    $temp = array();

    sort($SemanaAsis);

    foreach($SemanaAsis as $week){


        $label = explode('-', $week);

        $week = $label[0];



        //$week = $dat_sem->semana;

        $sqlVisitas = "SELECT sr.*,mt.grupo, mt.sucursal, p.nombre, mt.idFormato, mt.cadena FROM supervisionRutas sr 
	
	    RIGHT JOIN (
		    SELECT MAX(sr2.`fecha_captura`) fecha2, sr2.`idPromotor` , sr2.`semana`
		    FROM supervisionRutas sr2 WHERE 
		    sr2.`anio` =". date('Y') ." AND sr2.`semana` = ". $week ." AND sr2.`idPromotor` = ". $idProm ."
		    GROUP BY sr2.`semana`, sr2.`idPromotor`
	      ) AS sr1 ON date(sr1.fecha2) = date(sr.fecha_captura) 
	                and hour(sr1.fecha2) = hour(sr.fecha_captura) 
	                and minute(sr1.fecha2) = minute(sr.fecha_captura) 
	                and sr1.idPromotor = sr.idPromotor and sr1.semana = sr.semana
	    left join maestroTiendas mt on mt.idTienda=sr.idTienda 
	    left join Promotores p on p.idCelular=sr.idPromotor order by sr.idTienda";



        $query = $manager->ejecutarConsulta($sqlVisitas);


        $semanasCount = 0;
        while ($row = mysqli_fetch_array($query)){




            for ($i = 1; $i <= 7; $i++) {

                $fecha_dia = date('Y-m-d', strtotime( date('Y').'/01/01 +' . ($week - 1) . ' weeks monday this week +' . ($i - 1) . ' day')) . '';
                //$nom_dia = strtoupper($manager->diaNombre($i - 1));

                $nom_dia = $manager->getDayName($fecha_dia);
                $nomLowerDay = strtolower($nom_dia);

                $solicitada = $row[$nomLowerDay] > 0;

                $sqlCheck = "SELECT CONCAT(MIN(TIME(tv.fecha_captura)),'-', MAX(TIME(tv.fecha_captura))) visita,
		                      TIMEDIFF(MAX(TIME(tv.fecha_captura)), MIN(TIME(tv.fecha_captura))) tiempo, 
                              DATE(tv.fecha_captura) fecha_captura, 
                                    (SELECT COUNT(*) FROM frentesCharola fc WHERE fc.idTienda = tv.idTienda AND
                                      fc.fecha_captura = DATE(tv.`fecha_captura`) AND fc.idCelular=tv.`idCelular`
                                    ) frentes,
                                    ( SELECT COUNT(*) FROM photoCatalogo pc WHERE DATE(pc.fecha_captura) = DATE(tv.`fecha_captura`)
                                       AND pc.id_promotor = tv.`idCelular` AND pc.id_tienda = tv.`idTienda`
                                    ) photos
                            
                                FROM tiendasVisitadas AS tv
              
            
                                WHERE tv.idCelular=". $row["idPromotor"] ."
                                AND tv.idTienda=". $row["idTienda"] ." AND DATE(tv.fecha_captura)='".$fecha_dia."' 
                                
                                GROUP BY tv.idTienda, tv.idCelular, DATE(tv.fecha_captura)";


                $query2 = $manager->ejecutarConsulta($sqlCheck);




                $arrayTest = array();

                $arrayTest["idTienda"] = $row["idTienda"];
                $arrayTest["idPromotor"] = $row["idPromotor"];
                $arrayTest["promotor"] = $row["nombre"];
                $arrayTest["tienda"] = $row["grupo"] ." ".$row["sucursal"];
                $arrayTest["idFormato"] = $row["idFormato"];

                $position = $manager->search_item_array($row["idTienda"], $temp, "idTienda");



                if (mysqli_num_rows($query2) > 0){




                    $resul = mysqli_fetch_array($query2);

                    //array encontrado

                    if($position >= 0){


                        $temp[$position][$fecha_dia] = array("check"=>$resul["visita"],"tiempo"=>$resul["tiempo"], "frentes"=> $resul["frentes"], "fotos"=> $resul["photos"] ,"visita"=> $solicitada);


                    }else{

                        //array_push($arrayTest, array( => $resul["visita"],"value"=>$row[$nom_dia]));

                        $arrayTest[$fecha_dia] = array("check"=>$resul["visita"],"tiempo"=>$resul["tiempo"], "frentes"=> $resul["frentes"], "fotos"=> $resul["photos"] , "visita"=> $solicitada);
                        array_push($temp, $arrayTest);

                    }




                }else if($row[$nomLowerDay] > 0){


                    if($position >= 0){

                        $temp[$position][$fecha_dia] = array("check"=>"visita", "visita"=> $solicitada);

                    }else{


                        $arrayTest[$fecha_dia] = array("check"=>"visita", "visita"=> $solicitada);
                        array_push($temp, $arrayTest);
                    }

                }

                if($semanasCount==0){

                    $dateFormat = date('d/m/Y', strtotime($fecha_dia));

                    array_push($diasSemana,array("field"=>$fecha_dia,"title"=> $nom_dia ." <br> ".
                        $dateFormat, "formatter"=>"cellVisitFormat"));

                }


            }



            $semanasCount++;


        }


    }


    echo json_encode(array('data'=>$temp,'columns'=> $diasSemana,'frozenColumns'=> $frozenColums), JSON_PRETTY_PRINT | JSON_NUMERIC_CHECK);


} else {

    echo 'no has iniciado sesion';

    //header('refresh:2,../index.php');

}








