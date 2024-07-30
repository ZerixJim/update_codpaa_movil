<?php
session_start();


if (isset($_SESSION['idUser'])) {


    include_once('../../connexion/DataBase.php');

    $manager = DataBase::getInstance();
    $idUsuario = $_SESSION['idUser'];


////****WEEK(CURDATE(),2)

    $anio = date('Y');

    $mes = date('m');

    $dia = date('d');

///***************Recepcion de datos
//$array_rutas=array();

    $semana_ruta = $_REQUEST['semana_ruta'];
    $array_rutas = $_REQUEST['dataRutas'];

    for ($i = 0; $i <= 31; ++$i) {
        $array_rutas = str_replace(chr($i), "", $array_rutas);
    }
    $array_rutas = str_replace(chr(127), "", $array_rutas);

// This is the most common part
// Some file begins with 'efbbbf' to mark the beginning of the file. (binary level)
// here we detect it and we remove it, basically it's the first 3 characters
    if (0 === strpos(bin2hex($array_rutas), 'efbbbf')) {
        $array_rutas = substr($array_rutas, 3);
    }
    $array_rutas = stripslashes($array_rutas);

    $array_rutas2 = json_decode($array_rutas);


////********************************************

    sort($array_rutas2);

    $idPromotor = 0;

///*****************************Crear Hojas con las imagenes

    $inserted = array();
    $fail = array();

    foreach ($array_rutas2 as $rutas) {


        // if user is an admin

        if ($_SESSION['id_perfil'] == '1') {


            $sql = "delete from rutasPromotores  where idPromotor=" . $rutas->A . " AND 
                      ((UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(fecha_carga)) > 300 ) ";


            $manager->ejecutarConsulta($sql);


            $sql = "INSERT INTO rutasPromotores (idPromotor,idTienda,lunes,martes,miercoles,jueves,
                    viernes,sabado,domingo,rol,idUsuario_carga, idModo)
		            VALUES  ('" . $rutas->A . "','" . $rutas->B . "','" . $rutas->C . "','" . $rutas->D . "','" .
                $rutas->E . "','" . $rutas->F . "','" . $rutas->G . "','" . $rutas->H . "','" . $rutas->I . "','" .
                $rutas->J . "','" . $idUsuario . "', " . $rutas->idTipo . ")";


            $ins = $manager->ejecutarConsulta($sql);
            if($ins){



                array_push($inserted, $rutas);

            }else{

                $rutas-> loadError = 'error duplicated primary key';

                $rutas->error = $ins;
                array_push($fail, $rutas);
            }


            /*for ($i = -1; $i < 6; $i++) {

                $fecha_dia = date('Y-m-d', strtotime('01/01/' . $anio. ' +' . ($semana_ruta - 1) . ' weeks first day +' . $i . ' day')) . '';
                //$nom_dia = strtoupper($manager->diaNombre($i - 1));

                $nomDia = $manager->getDayName($fecha_dia);

            }*/


            /*if ($rutas->C >= 1){

                $sqlRequestVisit = "insert into visitas_solicitadas(id_tienda, id_promotor, visita, id_user, id_tipo) values 
                                      (". $rutas->B .", ". $rutas->A .",'".  ."', ".$idUsuario.", ". $rutas->idTipo ." )";


            }*/


        } else {

            //******************************Revisar que el Promotor sea asignado al usuario *******************************/

            $promotorAprobado = 0;

            $revisa_prom = "SELECT idCelular FROM Promotores WHERE idCelular IN (SELECT P.idCelular
            FROM Promotores AS P 
            INNER JOIN  marcaAsignadaPromotor M ON (M.idPromotor=P.idCelular) 
            WHERE M.idMarca IN (SELECT idMarca FROM usuariosMarcaAsignada WHERE idUsuario='" . $idUsuario . "')
            GROUP BY P.idCelular) AND idCelular='" . $rutas->A . "'";

            $resul_prom = $manager->ejecutarConsulta($revisa_prom);

            $dato_prom = mysqli_fetch_array($resul_prom);

            if ($dato_prom['idCelular'] > 0) {
                $promotorAprobado = $dato_prom['idCelular'];

            }


            if ($promotorAprobado != 0) {

                $elimina_ruta = "DELETE FROM rutasPromotores  WHERE idPromotor='" . $rutas->A . "' 
                  AND ((UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(fecha_carga)) > 300 ) ";

                $manager->ejecutarConsulta($elimina_ruta);

                $suma_sem = 0;

                $query_ruta = "INSERT INTO rutasPromotores 
                (idPromotor,idTienda,lunes,martes,miercoles,jueves,viernes,sabado,domingo,rol,idUsuario_carga, idModo)
		        VALUES  ('" . $rutas->A . "','" . $rutas->B . "','" . $rutas->C . "','" . $rutas->D . "','" .
                    $rutas->E . "','" . $rutas->F . "','" . $rutas->G . "','" . $rutas->H . "','" . $rutas->I . "','" .
                    $rutas->J . "','" . $idUsuario . "', $rutas->idTipo)";

                $ins = $manager->ejecutarConsulta($query_ruta);

               /* $num_dia = $manager->diaSemana($dia, $mes, $anio);

                if ($num_dia == 6 || $num_dia == 0) {
                    $suma_sem = 1;
                }*/

                //Calcula Ruta mysql (WEEK(CURDATE(),3)+$suma_sem)


                if($ins){



                    array_push($inserted, $rutas);

                }else{

                    $rutas-> loadError = 'error duplicated primary key';

                    $rutas->error = $ins;
                    array_push($fail, $rutas);
                }




            }


        }


    }



    http_response_code(200);


    echo json_encode(array("inserted"=> $inserted, "fail"=>$fail, "total"=>count($array_rutas2), "insert"=> true));



////*****************************************Final Imagenes

    /*if ($idPromotor != 0) {
        $query_rutaup = "INSERT INTO rutasUp (idUsuario,fecha) VALUES ('" . $_SESSION['idUser'] . "',now())";

        $manager->ejecutarConsulta($query_rutaup);

        echo $respuesta = '1';
    } else {
        $respuesta = '0';
        echo $respuesta;
    }*/


} else {

    http_response_code(422);

    echo 'acceso denegado';

}





