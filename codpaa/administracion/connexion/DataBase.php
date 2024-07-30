<?php

/**
 * Created by PhpStorm.
 * User: grim
 * Date: 06/03/2017
 * Time: 06:19 PM
 */
include_once('configuraciones.php');

class DataBase{

    private static $_instance;
    private $_connection;



    private function __construct(){
        $this->_connection = mysqli_connect(config::getBBDDServer(), config::getBBDDUser(), config::getBBDDPwd(), config::getBBDDName());
    }


    public static function getInstance(){
        if (!self::$_instance){
            self::$_instance = new self();
        }

        return self::$_instance;


    }


    public function ejecutarConsulta($sql){
        $con = mysqli_connect(config::getBBDDServer(), config::getBBDDUser(), config::getBBDDPwd(), config::getBBDDName());

        if (!$con) {
            die('Error al conectarse: ' . mysqli_error($con));
        }

        //mysql_select_db(config::getBBDDName(), $con);

        $result = mysqli_query($con, $sql);

        mysqli_close($con);

        return $result;

    }

    public function ejecuarConsultaErrorMessage($sql){

        $con = mysqli_connect(config::getBBDDServer(), config::getBBDDUser(), config::getBBDDPwd(), config::getBBDDName());

        if (!$con) {
            die('Error al conectarse: ' . mysqli_error($con));
        }

        //mysql_select_db(config::getBBDDName(), $con);

        $result = mysqli_query($con, $sql) or die(mysqli_error($con));



        mysqli_close($con);

        return $result;

    }

    public function diaSemana($dia, $mes, $anio){
        // 0->domingo     | 6->sabado
        $dia = date("w", mktime(0, 0, 0, $mes, $dia, $anio));
        return $dia;
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


    public function getIdRow($sql){

        $con = mysqli_connect(config::getBBDDServer(), config::getBBDDUser(), config::getBBDDPwd(), config::getBBDDName());

        if (!$con) {
            die('Error al conectarse: ' . mysqli_error($con));
        }

        //mysql_select_db(config::getBBDDName(), $con);

        $result = mysqli_query($con, $sql);

        $id = mysqli_insert_id($con);

        mysqli_close($con);

        return $id;


    }



    public function ejecutarConsultaID($sql){

        $idInsert = 0;
        $con = mysqli_connect(config::getBBDDServer(), config::getBBDDUser(), config::getBBDDPwd(), config::getBBDDName());

        if (!$con) {
            die('Error al conectarse: ' . mysqli_error($con));
        }

        //mysql_select_db(config::getBBDDName(), $con);



        if (mysqli_query($con, $sql)){

            $idInsert = mysqli_insert_id($con);

        }

        mysqli_close($con);

        return $idInsert;


    }



    public function getDayName($date){

        $day = date('w', strtotime($date));


        switch ($day) {
            case 1:
                $nomDia = "Lunes";
                break;
            case 2:
                $nomDia = "Martes";
                break;
            case 3:
                $nomDia = "Miercoles";
                break;
            case 4:
                $nomDia = "Jueves";
                break;
            case 5:
                $nomDia = "Viernes";
                break;
            case 6:
                $nomDia = "Sabado";
                break;
            case 0:
                $nomDia = "Domingo";
                break;

        }

        return $nomDia;

    }

    public function getMonthByNumberSpanish($number){

        $month = "";

        switch ($number){

            case '01':$month = "Ene"; break;
            case '02':$month = "Feb";break;
            case '03':$month = "Mar";break;
            case '04':$month = "Abr";break;
            case '05':$month = "May";break;
            case '06':$month = "Jun";break;
            case '07':$month = "Jul";break;
            case '08':$month = "Ago";break;
            case '09':$month = "Sep";break;
            case '10':$month = "Oct";break;
            case '11':$month = "Nov";break;
            case '12':$month = "Dic";break;

        }

        return $month;

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





    function search_item_array($value, $array, $key){

        $position = 0;
        foreach($array as $item){

            //echo $item[$key] ." == " . $value . " \n ";



            if($item[$key] == $value){

                //echo "entro ". " position " . $position . "  \n ";

                return $position;

            }

            $position++;
        }

        return -1;
    }


    function array_to_tuple($array){

        $tupla = "";
        if (!empty($array)){

            $countArray = count($array);
            $k = 0;
            if ($countArray > 0) {
                $tupla .= "(";
                foreach ($array as $item) {

                    if (is_numeric($item)) {

                        $tupla .= $item;

                        if ($k != $countArray - 1) {

                            $tupla .= ",";
                        }
                    }
                    $k++;
                }
                $tupla .= ")";
            }
        }

        return $tupla;
    }




    private function __clone(){ }


}