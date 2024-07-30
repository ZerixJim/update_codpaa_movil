<?php



$datos = json_encode($_POST['data'],true);

$array = json_decode($datos);





require('PDF.php');



$imagen = "../imagenes/LogoSVSsinfondo(opacity58).png";

$fondo = "../imagenes/fondo.jpg";

$marcaDeAgua = "../imagenes/marca.png";

$grados = 90;





$pdf = new PDF();



$pdf->setImagen($imagen);

$pdf->AliasNbPages();

$pdf->SetFont('Times','',12);











foreach($array as $objeto ){



    $pdf->setTitle($objeto->nombreTienda);

    $pdf->setTipo($objeto->nombre);

    $pdf->AddPage("L");

    $orientation = 1;

    $orientation = $objeto->orientationImage;

    if($orientation == 6 ){

        $pdf->RotatedImage("../../".$objeto->imagen,125,45,120,100,-90);

    }else{

        $pdf->Image("../../".$objeto->imagen,30,45,120,100);

    }





    $pdf->Body($objeto->nombrePromo,$objeto->fecha);

}



$fecha = getdate();



$fileName = $fecha['mday'].$fecha['mon'].$fecha['seconds'].".pdf";

$path = "archivos/".$fileName;



$pdf->Output("../".$path,'F');





    //echo $datos;

echo '{"mensaje":"PDF creado","status":1,"ruta":"'.$path.'"}';



















