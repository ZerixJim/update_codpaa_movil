<?php

/**
 * Created by PhpStorm.
 * User: grim
 * Date: 27/01/16
 * Time: 01:10 PM
 */


class Encuesta{

    private function getEncuesta($idPromotor){
        $db = new bdManager();

        $sql = "select pre.id_pregunta as idpre, pre.contenido as contenido, en.id_encuesta as id_encuesta,
                ma.idMarca as id_marca
                FROM preguntas as pre
                left join encuesta_preguntas as ep
                on pre.id_pregunta=ep.id_pregunta
                left join encuestas as en
                on ep.id_encuesta=en.id_encuesta
                left join marcaAsignadaPromotor as ma
                on ma.idMarca=en.id_marca where ma.idPromotor=".$idPromotor;

        return $db->ejecutarConsulta($sql);
    }

    public function getEncuestaJSON($idPromotor){
        $json = array();
        $items = array();


        $reponse = $this->getEncuesta($idPromotor);

        while($item = mysqli_fetch_array($reponse))
            $items[] = $item;



        foreach($items as $item){



            $array2 = array('id_pregunta' => $item['idpre'], 'contenido' => utf8_encode($item['contenido']),
                'id_encuesta' => $item['id_encuesta'], 'id_marca' => $item['id_marca']);
            array_push($json, $array2);

        }

        $response['questions'] = $json;




        header('Content-Type: application/json; charset=utf-8');
        return json_encode($response,JSON_PARTIAL_OUTPUT_ON_ERROR);


    }



}