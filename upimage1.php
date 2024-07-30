<?php

require('codpaa/webservice/bdManager.php');



if(isset($_POST['idtienda']) && isset($_POST['idpromo']) && isset($_POST['idex']) && isset($_POST['idmarca']) && isset($_POST['fecha']) && isset($_POST['ano']) && isset($_POST['mes']) && isset($_POST['dia']) ){

	$response=array();

    $file_path = "images/photos/".$_POST['idtienda']."/";

    if(!is_dir($file_path)){

        mkdir($file_path);

    }



    $file_path = $file_path . basename( $_FILES['file']['name'] );

    if(!file_exists($file_path)){



        //objecto bd

        $bd = new bdManager();


        if(move_uploaded_file($_FILES['file']['tmp_name'], $file_path)) {

            //$marker = new Watermaker($file_path,'(C)Codpaa');

            if($bd->ejecutarConsulta("insert into photoCatalogo(id_tienda,id_promotor,id_marca,id_exhibicion,fecha,ano,mes,dia,imagen) values(".$_POST['idtienda'].",".$_POST['idpromo'].",".$_POST['idmarca'].", ".$_POST['idex'].",'".$_POST['fecha']."',".$_POST['ano'].",".$_POST['mes'].",".$_POST['dia'].",'".$file_path."')")){

				$response['insert']="Imagen Recibida";
				$response['bol']=true;
				$response['idFotoCel']=$_POST['idFotoCel'];
                $response['idTienda'] = $_POST['idtienda'];
                $response['idMarca'] = $_POST['idmarca'];
                $response['fecha'] = $_POST['fecha'];

				

            }else{
				
				$response['insert']="Fallo Base";
				$response['bol']=false;
				$response['code']=1;

            }

        }else{

			$response['insert']="Fallo";
			$response['bol']=false;
			$response['code']=2;

        }



    }else{

		$response['insert']="Fallo, Imagen ya Existe";
		$response['bol']=false;
		$response['code']=3;
        $response['idFotoCel']=$_POST['idFotoCel'];



    }



}else{
	
	$response['insert']="Fallo, Error de Imagen";
	$response['bol']=false;


}

$response_json=json_encode($response);

echo $response_json;





