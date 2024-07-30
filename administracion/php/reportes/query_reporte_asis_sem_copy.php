<?php
/**
 * Created by PhpStorm.
 * User: grim
 * Date: 13/08/2018
 * Time: 10:18 AM
 */



ob_start();
session_start();


if (isset($_SESSION['usuario']) && isset($_SESSION['permiso'])) {

    include_once('../../connexion/DataBase.php');

    $idSupervisor = $_REQUEST['Supervisor'];

    // $id_formato = $_REQUEST['idFormato'];

    $id_estado = $_REQUEST['idEstado'];

    $SemanaAsis = $_REQUEST['SemanaAsis'];

    $idProm = $_REQUEST['idPromotor'];


    $id_tipoT = $_REQUEST['idTipoTie'];


    $filtro = "";
    if ($idSupervisor != "") {
        $filtro = " and p.Supervisor='" . $idSupervisor . "'";
    }
    if ($idProm != "") {
        $filtro = " and p.idCelular='" . $idProm . "'";
    }





    $n_estado = count($id_estado);
    $k = 0;
    if ($n_estado > 0) {
        foreach ($id_estado as $estados) {
            if ($estados != "all") {
                if ($k == 0) {
                    $filtro .= " and (p.idEstado='" . $estados . "' ";
                } else {
                    $filtro .= " or p.idEstado='" . $estados . "'";
                }
                $k++;
            }
        }
        if ($k != 0) {
            $filtro .= ")";
        }
    }


    if (!empty($SemanaAsis)){


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



    }



    $manager = DataBase::getInstance();

    $sql_sem = "SELECT semana,anio,idPromotor FROM supervisionRutas 
                WHERE idSupervision in " . $semanas ;



    $res_sem = $manager->ejecutarConsulta($sql_sem);




    ////*********************Recorre las fechas **********************//



    $diasSemana = array();


    $frozenColums = array();



    array_push($frozenColums, array("field"=>"idTienda", "title"=>"IDTienda", "sortable"=>true));
    array_push($frozenColums, array("field"=>"promotor", "title"=>"Promotor", "sortable"=>true));
    array_push($frozenColums, array("field"=> "tienda", "title"=>"Tienda", "sortable"=> true));


    // columns days generator

    $visitas = array();

    while($dat_sem = mysqli_fetch_object($res_sem)){

        $week = $dat_sem->semana;



        for ($i = -1; $i < 6; $i++) {

            $fecha_dia = date('Y-m-d', strtotime('01/01/' . $dat_sem->anio. ' +' . ($week - 1) . ' weeks first day +' . $i . ' day')) . '';
            //$nom_dia = strtoupper($manager->diaNombre($i - 1));

            $nom_dia = $manager->getDayName($fecha_dia);








            array_push($diasSemana,  /*$nom_dia .' '.*/array("field"=>$fecha_dia,"title"=> $nom_dia ." ". $fecha_dia, "formatter"=>"cellVisitFormat"));


            //we making a query to search each visit by day
            $sqlVisitas = " select date(tv.fecha_captura) fecha_captura , 
                        concat(min(time(tv.fecha_captura)),' - ', 
                        max(time(tv.fecha_captura)),' tiempo:', TIMEDIFF(MAX(TIME(tv.fecha_captura)), MIN(TIME(tv.fecha_captura))) ) checkin , 
                        tv.tipo, tv.idTienda, p.nombre, tv.idCelular, " . $nom_dia . " visita, concat(mt.grupo,' ', mt.sucursal) tienda 
                        from tiendasVisitadas tv
                        right join maestroTiendas mt on mt.idTienda=tv.idTienda
                        right join Promotores p on tv.idCelular = p.idCelular
                        
                        RIGHT JOIN supervisionRutas sr ON sr.`idTienda`=tv.`idTienda` AND sr.`semana` = ". $week ."

                        where date(tv.fecha_captura) = '" . $fecha_dia . "' and year(tv.fecha_captura)  ". $filtro ."  
                        
                        
                        
                        
                        group by tv.idTienda, tv.idCelular";




            $response = $manager->ejecutarConsulta($sqlVisitas);




            while ($row = mysqli_fetch_array($response)){



                array_push($visitas, array($row['fecha_captura'] => $row['checkin'],
                    "tipo"=>$row["tipo"], "idTienda"=>$row["idTienda"],"tienda"=>$row["tienda"], "promotor"=>$row["nombre"], "visita"=>$row["visita"]));


            }




        }







    }

    //we need to







    // array to storage the visits






    $response = $manager->ejecutarConsulta($sqlVisitas);



    while($row = mysqli_fetch_object($response)){

        array_push($visitas, array($row->fecha_captura => $row->fecha_captura));


    }


    /*foreach ($diasSemana as $dia){







        $visita = array();


        array_push($visitas, $visita);

    }*/





    echo json_encode(array('data'=> $visitas, 'columns'=> $diasSemana, 'frozenColumns'=> $frozenColums), JSON_PRETTY_PRINT | JSON_NUMERIC_CHECK);





    //***************************************** Seleccionar Promotores*************************************************//

    /*$sql_promo = "SELECT * FROM Promotores p INNER JOIN marcaAsignadaPromotor mp
	ON (p.idCelular=mp.idPromotor) WHERE p.status='a' " . $filtro . " GROUP BY p.idCelular";


    $rs_promo = $manager->ejecutarConsulta($sql_promo);*/


    ///// **********************************Ciclo promotores
    /*while ($dato_promo = mysqli_fetch_array($rs_promo)) {

        $i = 0;


        $a_promotor = "SELECT p.nombre ,p.Supervisor,e.nombre AS nomEstado
		FROM Promotores p 
		INNER JOIN estados e ON (p.idEstado=e.id)
		WHERE idCelular='" . $dato_promo['idPromotor'] . "'";
        $b_promotor = $manager->ejecutarConsulta($a_promotor);
        $c_promotor = mysqli_fetch_array($b_promotor);


        $a_super = "SELECT  concat(nombreSupervisor,' ',apellidoSupervisor) AS nom_super FROM Supervisores 
		WHERE idSupervisores='" . $c_promotor['Supervisor'] . "'";
        $b_super = $manager->ejecutarConsulta($a_super);

        $c_super = mysqli_fetch_array($b_super);


        $sql_tiend = "SELECT * FROM supervisionRutas r  
		WHERE idPromotor='" . $dato_promo['idPromotor'] . "' AND semana='" . $dat_sem['semana'] . "' AND anio='" . $dat_sem['anio'] . "'
		GROUP BY r.idTienda,r.idPromotor
		ORDER BY r.idTienda DESC ";


        $rs_tiend = $manager->ejecutarConsulta($sql_tiend);

        $total_prom_visit = 0;
        $total_prom_fil = 0;

        while ($datos_tiend = mysqli_fetch_array($rs_tiend)) {


            $sql_ruta = "SELECT idTienda,grupo,numeroEconomico,cadena, sucursal,x AS lat ,y AS lon 
			FROM maestroTiendas  
			WHERE idTienda='" . $datos_tiend['idTienda'] . "' ORDER BY idTienda ASC";

            $rs_ruta = $manager->ejecutarConsulta($sql_ruta);

            $dato_ruta = mysqli_fetch_array($rs_ruta);



            for ($i = 0; $i < 7; $i++) {

                $fecha_dia = date('d-m-Y', strtotime('01/01/' . $dat_sem['anio'] . ' +' . ($week - 1) . ' weeks first day +' . $i . ' day')) . '';
                $nom_dia = $manager->getDayName($fecha_dia);

                $query2 = "SELECT * FROM  tiendasVisitadas  
				WHERE idTienda='" . $dato_ruta['idTienda'] . "' AND fecha='" . $fecha_dia . "'
				AND idCelular='" . $datos_tiend['idPromotor'] . "'
				ORDER BY idTiendasVisitadas ASC ";

                $result2 = $manager->ejecutarConsulta($query2);

                $n_vis = mysqli_num_rows($result2);
                if ($n_vis > 0) {

                    while ($visitas_a = mysqli_fetch_array($result2)) {
                        $distancia_k = $manager->Distancia($dato_ruta['lat'], $dato_ruta['lon'], $visitas_a['latitud'], $visitas_a['longitud'], "K");

                        if ($visitas_a['tipo'] == 'E') {
                            $visitas_a['tipo'] = 'Entrada';
                        } else {
                            $visitas_a['tipo'] = 'Salida';
                        }


                    }


                } else {

                }


            }





        }




        for ($i = 0; $i < 7; $i++) {
            $fecha_dia = date('d-m-Y', strtotime('01/01/' . $dat_sem['anio'] . ' +' . ($week - 1) . ' weeks first day +' . $i . ' day')) . '';

            $sql_horas = "SELECT TIMEDIFF((SELECT STR_TO_DATE(hora,'%H:%i:%s')  AS hora_conv FROM tiendasVisitadas 
                WHERE fecha='" . $fecha_dia . "' AND idCelular='" . $dato_promo['idPromotor'] . "' AND tipo='S' 
                ORDER BY hora DESC LIMIT 1 ) ,
                (SELECT STR_TO_DATE(hora,'%H:%i:%s')  AS hora_conv FROM tiendasVisitadas 
                WHERE fecha='" . $fecha_dia . "' AND idCelular='" . $dato_promo['idPromotor'] . "' AND tipo='E' 
                ORDER BY hora ASC LIMIT 1 )) AS horas_lab";
            $rs_horas = $manager->ejecutarConsulta($sql_horas);

            $dat_horas = mysqli_fetch_array($rs_horas);


        }

    }*/



} else {

    echo 'no has iniciado sesion';

    //header('refresh:2,../index.php');

}








