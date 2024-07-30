<?php



require('../administracion/connexion/BDM.php');





$foto = $_FILES['foto'];

if(isset($_POST['fecha']) && isset($_POST['idtienda']) && isset($_POST['idpromo']) && isset($_POST['idex']) && isset($_POST['idmarca']) && isset($_POST['ano']) && isset($_POST['mes']) && isset($_POST['dia']) ){





    $file_path = "images/photos/".$_POST['idtienda']."/";

    if(!is_dir($file_path)){

        mkdir($file_path);

    }



    $file_path = $file_path . basename( $foto);



    if(!file_exists($file_path)){



        //objecto bd

        $bd = new BDM();



        

            if(move_uploaded_file($_FILES['foto']['tmp_name'], $file_path)) {





                if($bd->ejecutarConsulta("insert into photoCatalogo(id_tienda,id_promotor,id_marca,id_exhibicion,fecha,ano,mes,dia,imagen) values(".$_POST['idtienda'].",".$_POST['idpromo'].",".$_POST['idmarca'].", ".$_POST['idex'].",'".$_POST['fecha']."',".$_POST['ano'].",".$_POST['mes'].",".$_POST['dia'].",'".$file_path."')")){

                    echo "{\"insert\":\"Imagen Recibida\",\"bol\":true,\"code\":5}";

                }else{

                    echo "{\"insert\":\"Fallo base\",\"bol\":false,\"code\":1}";

                }



            }else{

                echo "{\"insert\":\"fallo\",\"bol\":false,\"code\":2}";

            }



        



    }else{



        echo "{\"insert\":\"fallo, Imagen ya Existe\",\"bol\":false,\"code\":3}";



    }









}else{

    echo "{\"insert\":\"fallo, Error de imagen\",\"bol\":false,\"code\":5}";

}





?>