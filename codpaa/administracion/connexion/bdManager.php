<?php

include_once('configuraciones.php');

class bdManager
{

    public function ejecutarConsulta($sql)
    {
        $con = mysqli_connect(config::getBBDDServer(), config::getBBDDUser(), config::getBBDDPwd(), config::getBBDDName());

        if (!$con) {
            die('Error al conectarse: ' . mysqli_error($con));
        }

        //mysql_select_db(config::getBBDDName(), $con);

        $result = mysqli_query($con, $sql);

        mysqli_close($con);

        return $result;
    }

    public function ejecutarMultiConsulta($sql)
    {
        $con = mysqli_connect(config::getBBDDServer(), config::getBBDDUser(), config::getBBDDPwd(), config::getBBDDName());

        if (!$con) {
            die('Error al conectarse: ' . mysqli_error());
        }

        //mysql_select_db(config::getBBDDName(), $con);


        if (mysqli_multi_query($con, $sql)) {
            $result = mysqli_store_result($con);
            //mysqli_free_result($result);
            mysqli_next_result($con);
            $result = mysqli_store_result($con);
        }

        mysqli_close($con);

        return $result;
    }


    public function verificarExistencia($sql)
    {
        $con = mysqli_connect(config::getBBDDServer(), config::getBBDDUser(), config::getBBDDPwd(), config::getBBDDName());

        if (!$con) {
            die('Error al conectarse: ' . mysqli_error());
        }

        //mysql_select_db(config::getBBDDName(), $con);

        $result = mysqli_query($sql);

        if (mysqli_num_rows($result) > 0) {
            $json = "{\"E\":[{\"log\":1}]}";
        } else {
            $json = "{\"E\":[{\"log\":0}]}";
        }

        mysqli_close($con);

        return $json;
    }

    public function verificarExistenciaRegis($sql)
    {
        $con = mysqli_connect(config::getBBDDServer(), config::getBBDDUser(), config::getBBDDPwd(), config::getBBDDName());
        if (!$con) {
            die('Error de con: ' . mysqli_error($con));
        }

        //mysql_select_db(config::getBBDDName(), $con);
        $result = mysqli_query($sql);

        if (mysqli_num_rows($result) > 0) {
            $json = "{\"log\":1}";
        } else {
            $json = "{\"log\":0}";
        }
        mysqli_close($con);

        return $json;

    }

    public function diaSemana($dia, $mes, $anio)
    {
        // 0->domingo     | 6->sabado
        $dia = date("w", mktime(0, 0, 0, $mes, $dia, $anio));
        return $dia;
    }

    public function calculaSemanas($fecha_desde, $fecha_hasta)
    {
        $dividefecha = explode("-", $fecha_desde);
        $dividefecha1 = explode("-", $fecha_hasta);
        // $dividefecha[0] = Dia
        // $dividefecha[1] = Mes
        // $dividefecha[2] = Ano

        $fecha_previa = mktime(0, 0, 0, $dividefecha[1], $dividefecha[0], $dividefecha[2]); //Convertimos $fecha_desde en formato timestamp
        $fecha_ultima = mktime(0, 0, 0, $dividefecha1[1], $dividefecha1[0], $dividefecha1[2]); //Convertimos $fecha_desde en formato timestamp

        $segundos = $fecha_ultima - $fecha_previa; // Obtenemos los segundos entre esas dos fechas
        $segundos = abs($segundos); //en caso de errores

        $semanas = floor($segundos / 604800);

        return $semanas;

    }

    public function diasSemana($anio, $semana)
    {
        $enero = mktime(1, 1, 1, 1, 1, $anio);
        $mos = (11 - date('w', $enero)) % 7 - 3;
        $inicios = strtotime(($semana - 1) . ' weeks ' . $mos . ' days', $enero);
        for ($x = 0; $x <= 6; $x++) {
            $dias[] = date('d-m-Y', strtotime("+ $x day", $inicios));
        }


        return $dias;
    }

    public function diaNombre($dia)
    {
        switch ($dia) {
            case 1:
                $nom_dia = "lunes";
                break;
            case 2:
                $nom_dia = "martes";
                break;
            case 3:
                $nom_dia = "miercoles";
                break;
            case 4:
                $nom_dia = "jueves";
                break;
            case 5:
                $nom_dia = "viernes";
                break;
            case 6:
                $nom_dia = "sabado";
                break;
            case 7:
                $nom_dia = "domingo";
                break;


        }

        return $nom_dia;

    }

    public function mesNombre($mes)
    {
        $nom_mes = $mes;
        $mes = (string)$mes;

        switch ($mes) {
            case '01':
                $nom_mes = "Enero";
                break;
            case '02':
                $nom_mes = "Febrero";
                break;
            case '03':
                $nom_mes = "Marzo";
                break;
            case '04':
                $nom_mes = "Abril";
                break;
            case '05':
                $nom_mes = "Mayo";
                break;
            case '06':
                $nom_mes = "Junio";
                break;

            case '07':
                $nom_mes = "Julio";
                break;

            case '08':
                $nom_mes = "Agosto";
                break;

            case '09':
                $nom_mes = "Septiembre";
                break;

            case '10':
                $nom_mes = "Octubre";
                break;

            case '11':
                $nom_mes = "Noviembre";
                break;

            case '12':
                $nom_mes = "Diciembre";
                break;


        }

        return $nom_mes;

    }

    public function Distancia($lat1, $lon1, $lat2, $lon2, $unit)
    {

        try {
            $lat1 = (double)$lat1;
            $lat2 = (double)$lat2;
            $lon1 = (double)$lon1;
            $lon2 = (double)$lon2;

            $radius = 6378.137; // earth mean radius defined by WGS84
            $dlon = $lon1 - $lon2;
            $distance = acos(sin(deg2rad($lat1)) * sin(deg2rad($lat2)) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($dlon))) * $radius;
        } catch (Exception $e) {

            $distance = 0;
        }
        if ($unit == "K") {
            return ($distance);
        } else if ($unit == "M") {
            return ($distance * 0.621371192);
        } else if ($unit == "N") {
            return ($distance * 0.539956803);
        } else {
            return 0;
        }
    }

}

